package com.mile.mile.redis;

import com.mile.mile.redis.pub_sub.MessagePublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentRepository studentRepository;
    private final MessagePublisher messagePublisher;

    public StudentController(StudentRepository studentRepository, MessagePublisher messagePublisher) {
        this.studentRepository = studentRepository;
        this.messagePublisher = messagePublisher;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        List<Student> all = studentRepository.findAll();
        all.removeIf(Objects::isNull);
        String message = "Message " + UUID.randomUUID();
        messagePublisher.publish(message);
        return all;
//                .stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{tenant}")
    public List<Student> geStudentsByTenant(@PathVariable("tenant") String tenant) {
        return studentRepository.findAllByTenant(tenant)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "{tenant}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student saveStudent(@PathVariable("tenant") String tenant) {
        Student student = new Student();
        student.setTenant(tenant);
        student.setExpiration(3L);
        return studentRepository.save(student);
    }

}
