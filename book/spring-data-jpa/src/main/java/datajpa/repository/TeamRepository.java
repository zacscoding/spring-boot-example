package datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import datajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
