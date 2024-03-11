package cn.heyige.excelpdf.repository;

import cn.heyige.excelpdf.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

}
