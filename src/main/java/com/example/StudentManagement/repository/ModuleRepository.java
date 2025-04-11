package com.example.StudentManagement.repository;

import com.example.StudentManagement.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    // find module by name
    Optional<Module> findByNameIgnoreCase(String name);


}

