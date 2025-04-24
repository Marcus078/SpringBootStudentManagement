package com.example.StudentManagement;

import com.example.StudentManagement.Services.StudentService;
import com.example.StudentManagement.exception.CourseNotFoundException;
import com.example.StudentManagement.exception.ModuleNotFoundException;
import com.example.StudentManagement.exception.StudentNotFoundException;
import com.example.StudentManagement.model.*;
import com.example.StudentManagement.model.Module;
import com.example.StudentManagement.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudentService.
 * Covers student creation, retrieval, deletion, search, course/module access,
 * and grade updates, including validation and exception handling.
 */


public class StudentServiceTest {

    private final StudentRepository studentRepo = mock(StudentRepository.class);
    private final CourseRepository courseRepo = mock(CourseRepository.class);
    private final ModuleRepository moduleRepo = mock(ModuleRepository.class);
    private final GradeRepository gradeRepo = mock(GradeRepository.class);
    private StudentService studentService;


    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepo, courseRepo, moduleRepo, gradeRepo);
    }

    // Should create a student, generate school email and student number, and attach default grades
    @Test
    void createStudent_ShouldSaveStudentWithGeneratedEmailAndNumber() {
        Course course = new Course();
        Module module1 = new Module(); module1.setName("Math");
        Module module2 = new Module(); module2.setName("Programming");
        course.setModules(List.of(module1, module2));

        Student student = new Student();
        student.setFirstName("Mike");
        student.setLastName("Smith");

        when(courseRepo.findByNameIgnoreCase("Software Engineering")).thenReturn(Optional.of(course));
        when(studentRepo.findMaxStudentNumber()).thenReturn(null);
        when(studentRepo.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        Student saved = studentService.createStudent(student, "Software Engineering");

        assertTrue(saved.getStudentNumber().startsWith("ST"));
        assertEquals(saved.getStudentNumber() + "@school.co.za", saved.getSchoolEmail());
        assertEquals(2, saved.getGrades().size());
    }

    // Should throw CourseNotFoundException when course name is invalid
    @Test
    void createStudent_ShouldThrowWhenCourseNotFound() {
        Student student = new Student();
        when(courseRepo.findByNameIgnoreCase("Invalid")).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> studentService.createStudent(student, "Invalid"));
    }

    // Should return all students
    @Test
    void getAllStudents_ShouldReturnList() {
        when(studentRepo.findAll()).thenReturn(List.of(new Student(), new Student()));
        assertEquals(2, studentService.getAllStudents().size());
    }

    // Should return student by valid student number
    @Test
    void getStudentByStudentNumber_ShouldReturnStudent() {
        Student student = new Student(); student.setStudentNumber("ST123");
        when(studentRepo.findByStudentNumber("ST123")).thenReturn(Optional.of(student));
        assertEquals("ST123", studentService.getStudentByStudentNumber("ST123").getStudentNumber());
    }

    // Should throw StudentNotFoundException when student number is invalid
    @Test
    void getStudentByStudentNumber_ShouldThrowWhenNotFound() {
        when(studentRepo.findByStudentNumber("UNKNOWN")).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentByStudentNumber("UNKNOWN"));
    }

    // Should return list of students matching name search
    @Test
    void searchStudentsByName_ShouldReturnStudents() {
        Student student = new Student(); student.setFirstName("Mike"); student.setLastName("Smith");
        when(studentRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Mike", "Mike"))
                .thenReturn(List.of(student));

        List<Student> result = studentService.searchStudentsByName("Mike");
        assertEquals(1, result.size());
    }

    // Should throw StudentNotFoundException if no student matches the search query
    @Test
    void searchStudentsByName_ShouldThrowWhenNotFound() {
        when(studentRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Unknown", "Unknown"))
                .thenReturn(List.of());

        assertThrows(StudentNotFoundException.class, () -> studentService.searchStudentsByName("Unknown"));
    }

    // Should delete student when ID is valid
    @Test
    void deleteStudent_ShouldDeleteExisting() {
        Student student = new Student(); student.setId(1L);
        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        studentService.deleteStudent(1L);
        verify(studentRepo).delete(student);
    }

    // Should throw StudentNotFoundException when trying to delete unknown student
    @Test
    void deleteStudent_ShouldThrowWhenNotFound() {
        when(studentRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(99L));
    }

    // Should return all courses
    @Test
    void getAllCourses_ShouldReturnCourses() {
        when(courseRepo.findAll()).thenReturn(List.of(new Course(), new Course()));
        assertEquals(2, studentService.getAllCourses().size());
    }

    // Should return course with modules by name
    @Test
    void getCourseWithModules_ShouldReturnCourse() {
        Course course = new Course(); course.setName("IT");
        when(courseRepo.findByNameIgnoreCase("IT")).thenReturn(Optional.of(course));
        assertEquals("IT", studentService.getCourseWithModules("IT").getName());
    }

    // Should throw CourseNotFoundException for invalid course name
    @Test
    void getCourseWithModules_ShouldThrowWhenNotFound() {
        when(courseRepo.findByNameIgnoreCase("None")).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> studentService.getCourseWithModules("None"));
    }

    // Should return all modules
    @Test
    void getAllModules_ShouldReturnModules() {
        when(moduleRepo.findAll()).thenReturn(List.of(new Module(), new Module()));
        assertEquals(2, studentService.getAllModules().size());
    }

    // Should return module with course by name
    @Test
    void getModuleWithCourse_ShouldReturnModule() {
        Module module = new Module(); module.setName("Databases");
        when(moduleRepo.findByNameIgnoreCase("Databases")).thenReturn(Optional.of(module));
        assertEquals("Databases", studentService.getModuleWithCourse("Databases").getName());
    }

    // Should throw ModuleNotFoundException for unknown module
    @Test
    void getModuleWithCourse_ShouldThrowWhenNotFound() {
        when(moduleRepo.findByNameIgnoreCase("Ghost")).thenReturn(Optional.empty());
        assertThrows(ModuleNotFoundException.class, () -> studentService.getModuleWithCourse("Ghost"));
    }

    // Should return students by course name
    @Test
    void getStudentsByCourse_ShouldReturnList() {
        when(studentRepo.findByCourse_Name("Software")).thenReturn(List.of(new Student(), new Student()));
        assertEquals(2, studentService.getStudentsByCourse("Software").size());
    }

    // Should update student grade when valid
    @Test
    void updateStudentGrade_ShouldUpdateWhenValid() {
        Student student = new Student();
        Module module = new Module(); module.setName("Networking");

        Grade grade = new Grade(); grade.setModule(module); student.setGrades(List.of(grade));

        when(studentRepo.findByStudentNumber("ST100")).thenReturn(Optional.of(student));
        when(moduleRepo.findByNameIgnoreCase("Networking")).thenReturn(Optional.of(module));
        when(gradeRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Grade updated = studentService.updateStudentGrade("ST100", "Networking", 88.5);
        assertEquals(88.5, updated.getGrade());
    }

   // Should throw IllegalArgumentException when grade is out of valid range
    @Test
    void updateStudentGrade_ShouldThrowWhenGradeOutOfRange() {
        assertThrows(IllegalArgumentException.class, () ->
                studentService.updateStudentGrade("ST100", "Math", -5)
        );
    }

    // Should throw StudentNotFoundException when student does not exist
    @Test
    void updateStudentGrade_ShouldThrowWhenStudentNotFound() {
        when(studentRepo.findByStudentNumber("UNKNOWN")).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () ->
                studentService.updateStudentGrade("UNKNOWN", "Math", 50)
        );
    }

    // Should throw ModuleNotFoundException when module is unknown
    @Test
    void updateStudentGrade_ShouldThrowWhenModuleNotFound() {
        Student student = new Student(); student.setGrades(List.of());
        when(studentRepo.findByStudentNumber("ST100")).thenReturn(Optional.of(student));
        when(moduleRepo.findByNameIgnoreCase("Ghost")).thenReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () ->
                studentService.updateStudentGrade("ST100", "Ghost", 75)
        );
    }

    // Should throw RuntimeException when no grade found for given module
    @Test
    void updateStudentGrade_ShouldThrowWhenGradeNotFound() {
        Student student = new Student();
        student.setGrades(List.of()); // no grades

        Module module = new Module(); module.setName("Algorithms");

        when(studentRepo.findByStudentNumber("ST100")).thenReturn(Optional.of(student));
        when(moduleRepo.findByNameIgnoreCase("Algorithms")).thenReturn(Optional.of(module));

        assertThrows(RuntimeException.class, () ->
                studentService.updateStudentGrade("ST100", "Algorithms", 90)
        );
    }
}
