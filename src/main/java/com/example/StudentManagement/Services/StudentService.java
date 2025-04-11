package com.example.StudentManagement.Services;

import com.example.StudentManagement.exception.CourseNotFoundException;
import com.example.StudentManagement.exception.ModuleNotFoundException;
import com.example.StudentManagement.exception.StudentNotFoundException;
import com.example.StudentManagement.model.Course;
import com.example.StudentManagement.model.Grade;
import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.model.Module;
import com.example.StudentManagement.repository.CourseRepository;
import com.example.StudentManagement.repository.ModuleRepository;
import com.example.StudentManagement.repository.StudentRepository;
import com.example.StudentManagement.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final GradeRepository gradeRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, ModuleRepository moduleRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.gradeRepository = gradeRepository;
    }

    // Create and save a new student
    public Student createStudent(Student student, String courseName) {
        // Fetch the course by name
        Course course = courseRepository.findByNameIgnoreCase(courseName.trim())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with name: " + courseName));

        // Generate the student number
        String studentNumber = generateStudentNumber();
        student.setStudentNumber(studentNumber);  // Set the generated student number

        // Generate the student email based on student number
        student.setSchoolEmail(studentNumber + "@school.co.za");

        // Set default grades (0) for each module in the course
        for (Module module : course.getModules()) {
            Grade grade = new Grade(student, module, 0, LocalDateTime.now());


            student.getGrades().add(grade);
        }

        student.setCourse(course);  // Assign the course to the student
        student.setDateCreated(LocalDateTime.now());
        student.setDateModified(LocalDateTime.now());

        return studentRepository.save(student);
    }

    // Generate student number (e.g., ST1000001, ST1000002, etc.)
    private String generateStudentNumber() {
        String maxNumber = studentRepository.findMaxStudentNumber();

        if (maxNumber == null) {
            return "ST0000001";
        }

        // Extract the numeric part and increment
        int current = Integer.parseInt(maxNumber.substring(2));
        int next = current + 1;

        return "ST" + String.format("%07d", next);
    }


    // Retrieve all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Retrieve a student by their student number
    public Student getStudentByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with number: " + studentNumber));
    }

    // Search for students by name (first or last name)
    public List<Student> searchStudentsByName(String name) {
        String[] parts = name.trim().split("\\s+");

        List<Student> students;

        if (parts.length == 1) {
            students = studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(parts[0], parts[0]);
        } else if (parts.length >= 2) {
            students = studentRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(parts[0], parts[1]);
        } else {
            students = List.of();
        }

        if (students.isEmpty()) {
            throw new StudentNotFoundException("No students found with that name: " + name);
        }

        return students;
    }



    // method to delete a student generated id from the database
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));
        studentRepository.delete(student);
    }
    // method to get all courses from the database
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseWithModules(String name) {
        return courseRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with name: " + name));
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Module getModuleWithCourse(String name) {
        return moduleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found: " + name));  // Throw custom exception
    }

    public List<Student> getStudentsByCourse(String courseName) {
        return studentRepository.findByCourse_Name(courseName);
    }


    public Grade updateStudentGrade(String studentNumber, String moduleName, double newGrade) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentNumber));

        Module module = moduleRepository.findByNameIgnoreCase(moduleName)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found: " + moduleName));

        Grade grade = student.getGrades().stream()
                .filter(g -> g.getModule().getName().equalsIgnoreCase(moduleName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Grade not found for student in module"));

        grade.setGrade(newGrade);
        grade.setDateAssigned(LocalDateTime.now());

        return gradeRepository.save(grade);
    }

}
