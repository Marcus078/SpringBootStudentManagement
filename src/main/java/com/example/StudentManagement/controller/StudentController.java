package com.example.StudentManagement.controller;


import com.example.StudentManagement.Services.StudentService;
import com.example.StudentManagement.exception.ModuleNotFoundException;
import com.example.StudentManagement.model.Course;
import com.example.StudentManagement.model.Grade;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.StudentManagement.repository.ModuleRepository;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

/*
    @Autowired
    private StudentService studentService;
    @Autowired
    private ModuleRepository moduleRepository;

*/

    private final StudentService studentService;
    private final ModuleRepository moduleRepository;

    @Autowired // can work without @AutoWired on spring 4.3+
    public StudentController(StudentService studentService, ModuleRepository moduleRepository) {
        this.studentService = studentService;
        this.moduleRepository = moduleRepository;
    }

    // Create a student
    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(@RequestBody Student student, @RequestParam String courseName) {
        Student createdStudent = studentService.createStudent(student, courseName);
        return ResponseEntity.ok(createdStudent);
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get student by student number
    @GetMapping("/{studentNumber}")
    public ResponseEntity<Student> getStudentByStudentNumber(@PathVariable String studentNumber) {
        Student student = studentService.getStudentByStudentNumber(studentNumber);
        return ResponseEntity.ok(student);
    }

    // Search students by name
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudentsByName(@RequestParam String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(students);
    }

    // Delete a student
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    //get all courses
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(studentService.getAllCourses());
    }

    //get course by name
    @GetMapping("/courses/{name}")
    public ResponseEntity<Course> getCourseWithModules(@PathVariable String name) {
        return ResponseEntity.ok(studentService.getCourseWithModules(name));
    }

    //get all modules
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = studentService.getAllModules();
        if (modules.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 if no modules are found
        }
        return ResponseEntity.ok(modules); // Returns 200 with the list of modules
    }

    //get module by name
    @GetMapping("/modules/{name}")
    public ResponseEntity<Module> getModuleWithCourse(@PathVariable String name) {
        Module module = studentService.getModuleWithCourse(name);
        return ResponseEntity.ok(module); // Returns 200 with the module details
    }

    //get course and associated students
    @GetMapping("/courses/{courseName}/students")
    public ResponseEntity<List<Student>> getStudentsByCourse(@PathVariable String courseName) {
        return ResponseEntity.ok(studentService.getStudentsByCourse(courseName));
    }

    @PutMapping("/{studentNumber}/grades/{moduleName}")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable String studentNumber,
            @PathVariable String moduleName,
            @RequestParam double grade) {

        Grade updated = studentService.updateStudentGrade(studentNumber, moduleName, grade);
        return ResponseEntity.ok(updated);
    }

}


