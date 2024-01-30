package com.khi.server.mainLogic.controller;

import com.khi.server.dto.request.TeamRequestDto;
import com.khi.server.mainLogic.entity.Team;
import com.khi.server.mainLogic.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;

    @PostMapping("/team/create")
    public ResponseEntity<String> creatTeam(@RequestBody @Valid TeamRequestDto request) {

        Team team = service.createTeam(request);
        return new ResponseEntity<>(team.getName(), HttpStatus.CREATED);
    }

    @PostMapping("/team/set")
    public ResponseEntity<String> setTeam(@RequestBody @Valid TeamRequestDto request) {

        Team team = service.setTeam(request);
        return new ResponseEntity<>(team.getName(), HttpStatus.OK);
    }
}
