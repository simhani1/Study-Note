package com.example.nplusone.repository;

import com.example.nplusone.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select distinct t from Team t join fetch t.memberList")
    List<Team> findAllByFetchJoin();

    @Query("select t from Team t join fetch t.memberList")
    List<Team> findAllByFetchJoinWithoutDistinct();

    @Query("select distinct t from Team t join t.memberList")
    List<Team> findAllByJoin();

}
