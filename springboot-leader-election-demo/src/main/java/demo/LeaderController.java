package demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.leader.LeaderElection;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LeaderController {

    private final LeaderElection leaderElection;

    @GetMapping("/leader")
    public Boolean isLeader() {
        return leaderElection.isLeader();
    }
}
