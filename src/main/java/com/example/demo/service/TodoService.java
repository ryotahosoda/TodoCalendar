package com.example.demo.service;
import com.example.demo.model.Todo;
import com.example.demo.Enum.TodoEnum;
import com.example.demo.repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


@Service
@Transactional
public class TodoService {
    @Autowired
    TodoRepository todoRepository;

    @Transactional
    public Todo getTodo(Long id){
        Todo todo = todoRepository.getOne(id);
        if (todo==null){
            return null;
            //未実装
        }
        return todo;
    }

    @Transactional
    public void updateNameandLimitDateImp(Long id, String name, String limit_date, TodoEnum importance) throws ParseException {
        Todo todo = getTodo(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date limitdate = sdf.parse(limit_date);
        todo.setTodoName(name);
        todo.setLimitDate(limitdate);
        todo.setImportance(importance);
        todoRepository.saveAndFlush(todo);
    }

    //作成日で降順に並べる
    @Transactional
    public List MakedateDesc(){
        return todoRepository.findAllByOrderByMakeDateDesc();
    }

    //名前部分一致と未完了を作成日の降順
    @Transactional
    public List NameLikeFinishMakedateDesc(String name){
        StringBuilder buf = new StringBuilder();
        buf.append("%"+name+"%");
        return todoRepository.findByTodoNameLikeAndFinishOrderByMakeDateDesc(buf.toString(), false);
    }

    //名前一致を作成日の降順
    @Transactional
    public List NameMakedateDesc(String name){
        return todoRepository.findByTodoNameOrderByMakeDateDesc(name);
    }

    //名前部分一致を作成日で降順
    @Transactional
    public List NameLikeMakedateDesc(String name){
        StringBuilder buf = new StringBuilder();
        buf.append("%" + name + "%");
        return todoRepository.findByTodoNameLikeOrderByMakeDateDesc(buf.toString());
    }

    @Transactional
    public List All(){
        return todoRepository.findAll();
    }

    @Transactional
    public void AddSave(String str, String limit_date, TodoEnum importance) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String make_date = sdf.format(new Date());
        Date makedate = sdf.parse(make_date);
        Date limitdate = sdf.parse(limit_date);
        Todo addtodo = new Todo(str, limitdate, makedate, false,importance);
        todoRepository.saveAndFlush(addtodo);
    }

    @Transactional
    public Todo updateFinish(Long id){
        Todo todo = getTodo(id);
        if (todo.getFinish()==false){
            todo.setFinish(true);
        }
        else {
            todo.setFinish(false);
        }
        return todoRepository.saveAndFlush(todo);
    }

    public TodoEnum change(String value){
        if(value.equals("low")){
            return TodoEnum.low;
        }
        else if(value.equals("medium")){
            return TodoEnum.medium;
        }
        else {
            return TodoEnum.high;
        }
    }

    public int SameName(String name){
        List<Todo> todos = MakedateDesc();
        int isSameNameERROR = 0;
        for(Todo todo:todos){
            if(name.equals(todo.getTodoName())){
                isSameNameERROR++;
            }

        }
        return isSameNameERROR;
    }

}

