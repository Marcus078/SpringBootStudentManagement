package com.example.StudentManagement.Services;

import com.example.StudentManagement.model.Course;
import com.example.StudentManagement.repository.CourseRepository;
import com.example.StudentManagement.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.StudentManagement.model.Module;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public void run(String... args) {
        // Seed courses and modules only if they aren't already seeded
        if (courseRepository.count() == 0) {
            // Create courses
            Course cs = new Course();
            cs.setName("Computer Science");
            cs.setYear(2024);
            cs.setModules(new ArrayList<>());


            Course se = new Course();
            se.setName("Software Engineering");
            se.setYear(2024);
            se.setModules(new ArrayList<>());

            // Save courses
            courseRepository.saveAll(List.of(cs, se));

            // Create and save modules
            moduleRepository.saveAll(List.of(
                    new Module(null, "Algorithms", cs),
                    new Module(null, "Databases", cs),
                    new Module(null, "Operating Systems", cs),
                    new Module(null, "Software Design", se),
                    new Module(null, "Web Development", se),
                    new Module(null, "Testing & QA", se)
            ));
        }
    }
}
