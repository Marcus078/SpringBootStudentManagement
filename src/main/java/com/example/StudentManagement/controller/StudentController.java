package com.example.StudentManagement.controller;


import com.example.StudentManagement.Services.StudentService;
import com.example.StudentManagement.exception.ModuleNotFoundException;
import com.example.StudentManagement.model.Course;
import com.example.StudentManagement.model.Grade;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.model.Module;
import com.example.StudentManagement.response.ApiResponse;
import jakarta.validation.Valid;
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

    // Create a student with validation on Student object
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Student>> createStudent(@RequestBody @Valid Student student, @RequestParam String courseName) {
        Student createdStudent = studentService.createStudent(student, courseName);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student created successfully", createdStudent));
    }

    // Get all students
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all students", students));
    }

    // Get student by student number
    @GetMapping("/{studentNumber}")
    public ResponseEntity<ApiResponse<Student>> getStudentByStudentNumber(@PathVariable String studentNumber) {
        Student student = studentService.getStudentByStudentNumber(studentNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student found", student));
    }


    // Search students by name
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Student>>> searchStudentsByName(@RequestParam String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", students));
    }


    // Delete a student
    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Student deleted successfully", null));
    }

    //get all courses
    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = studentService.getAllCourses();
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all courses", courses));
    }

    //get course by name
    @GetMapping("/courses/{name}")
    public ResponseEntity<ApiResponse<Course>> getCourseWithModules(@PathVariable String name) {
        Course course = studentService.getCourseWithModules(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Course retrieved with modules", course));
    }


    //get all modules
    @GetMapping("/modules")
    public ResponseEntity<ApiResponse<List<Module>>> getAllModules() {
        List<Module> modules = studentService.getAllModules();
        if (modules.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "No modules found", modules)); // Empty list still returns 200
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all modules", modules));
    }

    //get module by name
    @GetMapping("/modules/{name}")
    public ResponseEntity<ApiResponse<Module>> getModuleWithCourse(@PathVariable String name) {
        Module module = studentService.getModuleWithCourse(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Module retrieved with course", module));
    }

    //get course and associated students
    @GetMapping("/courses/{courseName}/students")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByCourse(@PathVariable String courseName) {
        List<Student> students = studentService.getStudentsByCourse(courseName);
        return ResponseEntity.ok(new ApiResponse<>(true, "Students in course retrieved", students));
    }

//Update Grade for a Module
    @PutMapping("/{studentNumber}/grades/{moduleName}")
    public ResponseEntity<ApiResponse<Grade>> updateGrade(
            @PathVariable String studentNumber,
            @PathVariable String moduleName,
            @RequestParam double grade) {

        Grade updatedGrade = studentService.updateStudentGrade(studentNumber, moduleName, grade);
        return ResponseEntity.ok(new ApiResponse<>(true, "Grade updated successfully", updatedGrade));
    }

}


