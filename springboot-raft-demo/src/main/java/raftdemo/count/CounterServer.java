package raftdemo.count;

import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.TimerManager;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class CounterServer {

    // JRaft server service framework
    private RaftGroupService raftGroupService;
    // Raft node
    private Node node;
    // Business state machine
    private CounterStateMachine fsm;

    public CounterServer(String dataPath, String groupId, PeerId serverId, NodeOptions nodeOptions) throws IOException {
        // Initialize the path.
        FileUtils.forceMkdir(new File(dataPath));
        // Initialize the global timer.
        //TimerManager.init(50);

        // Here Raft RPC and business RPC share the same RPC server. They can use different RPC servers, too.
        RpcServer rpcServer = new RpcServer(serverId.getPort());
        RaftRpcServerFactory.addRaftRequestProcessors(rpcServer);
        // Register the business processor.
        //rpcServer.registerUserProcessor(new GetValueRequestProcessor(this));
        //rpcServer.registerUserProcessor(new IncrementAndGetRequestProcessor(this));
        // Initialize the state machine.
        this.fsm = new CounterStateMachine();
        // Set the state machine to the startup parameters.
        nodeOptions.setFsm(this.fsm);
        // Set the storage path.
        // Required. Specify the log.
        nodeOptions.setLogUri(dataPath + File.separator + "log");
        // Required. Specify the metadata.
        nodeOptions.setRaftMetaUri(dataPath + File.separator + "raft_meta");
        // Recommended. Specify the snapshot.
        nodeOptions.setSnapshotUri(dataPath + File.separator + "snapshot");
        // Initialize the Raft group service framework.
        this.raftGroupService = new RaftGroupService(groupId, serverId, nodeOptions, rpcServer);
        // Startup
        this.node = this.raftGroupService.start();
    }

    public CounterStateMachine getFsm() {
        return this.fsm;
    }

    public Node getNode() {
        return this.node;
    }

    public RaftGroupService RaftGroupService() {
        return this.raftGroupService;
    }

    /**
     * Generate the redirect request.
     */
    public ValueResponse redirect() {
        ValueResponse response = new ValueResponse();
        response.setSuccess(false);
        if (node != null) {
            PeerId leader = node.getLeaderId();
            if (leader != null) {
                response.setRedirect(leader.toString());
            }
        }

        return response;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out
                .println("Useage : java com.alipay.jraft.example.counter.CounterServer {dataPath} {groupId} {serverId} {initConf}");
            System.out
                .println(
                    "Example: java com.alipay.jraft.example.counter.CounterServer /tmp/server1 counter 127.0.0.1:8081 127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
            System.exit(1);
        }
        String dataPath = args[0];
        String groupId = args[1];
        String serverIdStr = args[2];
        String initConfStr = args[3];

        NodeOptions nodeOptions = new NodeOptions();
        // Adjust arguments such as the snapshot interval for testing.
        nodeOptions.setElectionTimeoutMs(5000);
        nodeOptions.setDisableCli(false);
        nodeOptions.setSnapshotIntervalSecs(30);
        // Parse the parameter.
        PeerId serverId = new PeerId();
        if (!serverId.parse(serverIdStr)) {
            throw new IllegalArgumentException("Fail to parse serverId:" + serverIdStr);
        }
        Configuration initConf = new Configuration();
        if (!initConf.parse(initConfStr)) {
            throw new IllegalArgumentException("Fail to parse initConf:" + initConfStr);
        }
        // Set the initial cluster configuration.
        nodeOptions.setInitialConf(initConf);

        // Startup
        CounterServer counterServer = new CounterServer(dataPath, groupId, serverId, nodeOptions);
        System.out.println("Started counter server at port:"
            + counterServer.getNode().getNodeId().getPeerId().getPort());
    }
}
