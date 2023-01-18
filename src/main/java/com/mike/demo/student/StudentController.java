package com.mike.demo.student;

import com.mike.demo.exception.ApiRequestException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents(){
//        throw new IllegalStateException("Ooops cannot load students");
//        throw new ApiRequestException("Ooops cannot load students with custom exception");
        return studentService.getAllStudents();
    }

    @PostMapping
    public void addNewStudent(@RequestBody @Valid Student student){
        studentService.addNewStudent(student);
    }

    @GetMapping(path = "{studentId}/courses")
    public List<StudentCourse> getAllStudentCourses(
            @PathVariable("studentId") UUID studentId){
        return studentService.getAllStudentCourses(studentId);
    }

}
