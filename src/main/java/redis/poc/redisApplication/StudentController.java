package redis.poc.redisApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    private final StudentRepo repo;

    public StudentController(StudentRepo repo) {
        this.repo = repo;
    }


    @GetMapping("/create")
    public Student create(@RequestBody Student student) {
        return repo.save(student);
    }

    @GetMapping("/get/{id}")
    public Optional<Student> getStudent(@PathVariable Long id) {
        return repo.findById(id);
    }

    @GetMapping("/get")
    public List<Student> getStudents() {
        return repo.findAll();
    }

}
