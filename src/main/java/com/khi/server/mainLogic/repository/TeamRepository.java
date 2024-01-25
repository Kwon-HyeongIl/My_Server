package com.khi.server.mainLogic.repository;

import com.khi.server.mainLogic.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    public Optional<Team> findTeamByTeamName(String teamName);
}
