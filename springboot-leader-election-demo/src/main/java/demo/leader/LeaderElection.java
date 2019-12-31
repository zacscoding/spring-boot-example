package demo.leader;

/**
 * LeaderElection interface
 */
public interface LeaderElection {

    void start();

    void shutdown();

    boolean isLeader();
}