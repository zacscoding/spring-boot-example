package raftdemo.guide;

import ch.qos.logback.classic.Level;

import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.StateMachine;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.TimerManager;
import com.alipay.sofa.jraft.entity.LeaderChangeContext;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.RaftException;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for raft basic server
 */
public class BasicServer {

    @BeforeEach
    public void setUp() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)
                LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
    }

    @Test
    public void runNodes() throws Exception {
        final int electionTimeoutMs = 3000;
        final String[] serverIds = {
                "127.0.0.1:8081"
                , "127.0.0.1:8082"
                //, "127.0.0.1:8083"
        };
        final String serverIdsStr = Arrays.asList(serverIds).stream().collect(Collectors.joining(","));
        final ClusterServer[] servers = new ClusterServer[serverIds.length];

        try {
            for (int i = 0; i < serverIds.length; i++) {
                final String dataPath = "/tmp/raft/server" + i;
                final String groupId = "cluster";
                final String serverId = serverIds[i];
                final NodeOptions nodeOptions = new NodeOptions();

                final File dataFile = new File(dataPath);
                if (dataFile.exists()) {
                    FileUtils.forceDelete(dataFile);
                }

                // Adjust arguments such as the snapshot interval for testing.
                nodeOptions.setElectionTimeoutMs(electionTimeoutMs);
                nodeOptions.setDisableCli(false);
                nodeOptions.setSnapshotIntervalSecs(30);

                // Parse the parameter.
                PeerId peerId = new PeerId();
                if (!peerId.parse(serverId)) {
                    throw new IllegalArgumentException("Fail to parse serverId:" + serverId);
                }
                final Configuration initConf = new Configuration();
                if (!initConf.parse(serverIdsStr)) {
                    throw new IllegalArgumentException("Fail to parse initConf:" + serverIdsStr);
                }
                // Set the initial cluster configuration.
                nodeOptions.setInitialConf(initConf);

                // Startup
                servers[i] = new ClusterServer(dataPath, groupId, peerId, nodeOptions);

                // if started first cluster, start to console reporter
                if (i == 0) {
                    Thread nodeCheckTask = new Thread(() -> {
                        try {
                            while (!Thread.currentThread().isInterrupted()) {
                                StringBuilder result = new StringBuilder(
                                        "// #############################################################\n");
                                for (int j = 0; j < servers.length; j++) {
                                    ClusterServer s = servers[j];

                                    if (s == null) {
                                        continue;
                                    }

                                    Node node = s.getNode();
                                    result.append(
                                            String.format("## >>> check nodes in %s <<<\n", node.getNodeId()));
                                    result.append("> is started : " + s.isStarted()).append("\n");
                                    if (s.isStarted()) {
                                        result.append("> is leader : " + node.isLeader()).append("\n");
                                        result.append("> leader id : " + node.getLeaderId()).append("\n");
                                        if (node.isLeader()) {
                                            result.append("> peers :")
                                                  .append(
                                                          node.listPeers().stream().map(p -> p.toString())
                                                              .collect(Collectors.joining(","))
                                                  ).append("\n> active peers : ")
                                                  .append(
                                                          node.listAlivePeers().stream().map(p -> p.toString())
                                                              .collect(Collectors.joining(","))
                                                  ).append("\n");
                                        }
                                    }
                                }
                                result.append(
                                        "################################################################ //");
                                System.out.println(result);
                                TimeUnit.SECONDS.sleep(5L);
                                // Output
                                /*
                                 */
                            }
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                        }
                    });
                    nodeCheckTask.setDaemon(true);
                    nodeCheckTask.start();
                }
                TimeUnit.MILLISECONDS.sleep(electionTimeoutMs + 1000);
            }

            TimeUnit.SECONDS.sleep(30L);
            servers[0].terminate();
            TimeUnit.SECONDS.sleep(5L);
            PeerId peerId = PeerId.ANY_PEER;
            servers[1].getNode().transferLeadershipTo(peerId);
            System.out.println(">> after terminate <<");
            TimeUnit.SECONDS.sleep(30L);
        } catch (InterruptedException e) {
            if (servers != null) {
                for (ClusterServer server : servers) {
                    if (server != null && server.isStarted()) {
                        server.terminate();
                    }
                }
            }
        }

    }

    public static class ClusterServer {

        // JRaft server service framework
        private RaftGroupService raftGroupService;
        // Raft node
        private Node node;
        private ConsoleStateMachine fsm;

        public ClusterServer(String dataPath, String groupId, PeerId peerId, NodeOptions nodeOptions)
                throws IOException {
            // Initialize the path.
            FileUtils.forceMkdir(new File(dataPath));

            //new TimerManager().init(50);

            // Here Raft RPC and business RPC share the same RPC server. They can use different RPC servers, too.
            final RpcServer rpcServer = new RpcServer(peerId.getPort());
            RaftRpcServerFactory.addRaftRequestProcessors(rpcServer);

            fsm = new ConsoleStateMachine(peerId.toString());
            // Set the state machine to the startup parameters.
            nodeOptions.setFsm(fsm);
            // Set the storage path.
            // Required. Specify the log.
            nodeOptions.setLogUri(dataPath + File.separator + "log");
            // Required. Specify the metadata.
            nodeOptions.setRaftMetaUri(dataPath + File.separator + "raft_meta");
            // Recommended. Specify the snapshot.
            nodeOptions.setSnapshotUri(dataPath + File.separator + "snapshot");
            // Initialize the Raft group service framework.
            raftGroupService = new RaftGroupService(groupId, peerId, nodeOptions, rpcServer);
            // Startup
            node = raftGroupService.start();
        }

        public Node getNode() {
            return node;
        }

        public void terminate() {
            raftGroupService.shutdown();
        }

        public boolean isStarted() {
            return raftGroupService.isStarted();
        }
    }

    public static class ConsoleStateMachine implements StateMachine {

        private static final Logger logger = LoggerFactory.getLogger(ConsoleStateMachine.class);
        private String id;

        public ConsoleStateMachine(String id) {
            this.id = id;
        }

        @Override
        public void onApply(Iterator iter) {
            System.out.printf("## [%s] onApply is called\n", id);
            while (iter.hasNext()) {
                if (iter.done() != null) {
                    Closure done = iter.done();
                    System.out.printf("## [%s] iter.done != null > %s\n", id, done.getClass().getName());
                } else {
                    ByteBuffer data = iter.getData();
                    System.out.printf("## [%s] iter.done == null > %s\n", id, data);
                }
            }
        }

        @Override
        public void onShutdown() {
            System.out.printf("## onShutdown() is called\n");
        }

        @Override
        public void onSnapshotSave(SnapshotWriter writer, Closure done) {
            System.out.printf("## onSnapshotSave() is called\n");
        }

        @Override
        public boolean onSnapshotLoad(SnapshotReader reader) {
            System.out.printf("## onSnapshotLoad() is called\n");
            return false;
        }

        @Override
        public void onLeaderStart(long term) {
            System.out.printf("## onLeaderStart(%d) is called\n", term);
        }

        @Override
        public void onLeaderStop(Status status) {
            System.out.printf("## onLeaderStop(%s) is called\n", status);
        }

        @Override
        public void onError(RaftException e) {
            System.out.printf("## onError(%s) is called\n", e.getMessage());
        }

        @Override
        public void onConfigurationCommitted(Configuration conf) {
            System.out.printf("## onConfigurationCommitted(%s) is called\n", conf);
        }

        @Override
        public void onStopFollowing(LeaderChangeContext ctx) {
            System.out.printf("## onStopFollowing(%s) is called\n", ctx);
        }

        @Override
        public void onStartFollowing(LeaderChangeContext ctx) {
            System.out.printf("## onStartFollowing(%s) is called\n", ctx);
        }
    }
}
