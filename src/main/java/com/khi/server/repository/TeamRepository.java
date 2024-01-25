package com.khi.server.repository;

import com.khi.server.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    public Optional<Team> findTeamByTeamName(String teamName);
}
