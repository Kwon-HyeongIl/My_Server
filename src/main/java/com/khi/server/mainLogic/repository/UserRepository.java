package com.khi.server.mainLogic.repository;

import com.khi.server.mainLogic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByEmail(String email);

    public Optional<User> findUserByOauth2Key(String oauth2Key);
}
