package redis.poc.redisApplication;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
public class StudentController {

    @Autowired
    private RedisTemplate<String, Student> redisTemplate;


    @PostMapping("/create")
    public String create(@RequestParam long id, @RequestParam String name, @RequestParam int age) {
        Student student = new Student(id, name, age);

        var key = "student_" + id;
        final ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        operations.set(key, student, 10, TimeUnit.SECONDS);

        return "saved";
    }

    @GetMapping("/get/{id}")
    public String getStudent(@PathVariable Long id) {
        var key = "student_" + id;
        final ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        Student student1 = operations.get(key);

        return student1.getName();
    }

    @GetMapping("/get")
    public String getStudents() {

        return "saved";
    }

}
