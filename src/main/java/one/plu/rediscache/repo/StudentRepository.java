package one.plu.rediscache.repo;

import one.plu.rediscache.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository{
    Student findById(Integer id);
}
