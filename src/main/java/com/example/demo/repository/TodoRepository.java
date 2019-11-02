package com.example.demo.repository;


import com.example.demo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByOrderByMakeDateDesc();
    List<Todo> findByTodoNameLikeAndFinishOrderByMakeDateDesc(String name, Boolean finish);
    List<Todo> findByTodoNameLikeOrderByMakeDateDesc(String name);
    List<Todo> findByTodoNameOrderByMakeDateDesc(String name);
}
