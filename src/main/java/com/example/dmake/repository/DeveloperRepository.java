package com.example.dmake.repository;

import com.example.dmake.code.StatusCode;
import com.example.dmake.model.Developer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer,Long> {

    Optional<Developer> findByMemberId(String id);

    List<Developer> findDeveloperByStatusCodeEquals(StatusCode statusCode);

}
