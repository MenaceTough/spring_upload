package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<BootFile, Long> {
    @Query(value = "SELECT id, name, size, content, upload_time FROM files", nativeQuery = true)
    List<BootFile> findAll();
}
