package com.khi.server.mainLogic.repository;

import com.khi.server.mainLogic.entity.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPageRepository extends JpaRepository<MyPage, Long> {
}
