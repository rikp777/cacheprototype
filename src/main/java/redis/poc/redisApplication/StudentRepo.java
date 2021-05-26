package redis.poc.redisApplication;

import org.springframework.data.jpa.repository.JpaRepository;

interface StudentRepo extends JpaRepository<Student, Long> {

}