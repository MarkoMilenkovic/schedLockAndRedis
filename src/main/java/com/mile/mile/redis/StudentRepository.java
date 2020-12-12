package com.mile.mile.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {

    List<Student> findAll();

    List<Student> findAllByTenant(String tenant);

}
