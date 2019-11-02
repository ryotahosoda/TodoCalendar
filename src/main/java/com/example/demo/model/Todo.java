package com.example.demo.model;

import com.example.demo.Enum.TodoEnum;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.*;


@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @Column(name="todo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @NotNull
    @Size(min = 1,max = 30,message = "1文字以上30文字以下で入力してください")
    @Column(name="todo_name")
    private String todoName;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy年MM月dd日")
    @Column(name = "limit_date")
    private Date limitDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "make_date")
    private Date makeDate;

    private Boolean finish;

    @Enumerated(EnumType.STRING)
    private TodoEnum importance;

    public Todo() {
        super();
    }

    public Todo(String name, Date limit_date, Date make_date, Boolean finish, TodoEnum importance) {
        super();
        this.todoName=name;
        this.limitDate=limit_date;
        this.makeDate=make_date;
        this.finish=finish;
        this.importance=importance;
    }

    public Long getTodoId(){
        return todoId;
    }
    public void setTodoId(Long id){
        this.todoId=id;
    }
    public String getTodoName(){
        return todoName;
    }
    public void setTodoName(String name){
        this.todoName=name;
    }
    public Date getLimitDate(){
        return limitDate;
    }
    public void setLimitDate(Date limit_date){
        this.limitDate=limit_date;
    }
    public Date getMakeDate(){
        return makeDate;
    }
    public void setMakeDate(Date make_date){
        this.makeDate=make_date;
    }
    public Boolean getFinish(){ return finish; }
    public void setFinish(Boolean finish){
        this.finish=finish;
    }
    public TodoEnum getImportance() {
        return importance;
    }
    public void setImportance(TodoEnum importance) {
        this.importance = importance;
    }



}

