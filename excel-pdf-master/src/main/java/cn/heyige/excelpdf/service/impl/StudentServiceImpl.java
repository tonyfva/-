package cn.heyige.excelpdf.service.impl;

import cn.heyige.excelpdf.anno.MethodCache;
import cn.heyige.excelpdf.domain.Student;
import cn.heyige.excelpdf.repository.StudentRepository;
import cn.heyige.excelpdf.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl implements StudentService {

    final StudentRepository studentRepository;

    @Override
    // @MethodCache(value = "student:all",ttl = "30")
     @MethodCache
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

}
