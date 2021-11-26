package com.example.dmake.repository;

import com.example.dmake.model.Developer;
import com.example.dmake.model.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetiredDeveloperRepository extends JpaRepository<RetiredDeveloper,Long> {

}
