package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Event;
import com.example.demo.model.Schedule;
import com.example.demo.model.Todo;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.example.demo.util.TodoUtil.DateToString;

@RestController
@RequestMapping("/api")
public class RestEventController {
    /**
     * カレンダーに表示するEvent情報を取得
     * @return Event情報をjsonエンコードした文字列
     */

    @Autowired
    TodoService todoService;
    @Autowired
    ScheduleService scheduleService;

    @GetMapping("/events")
    public String getEvents() {
        String jsonMsg = null;
        try {
            List<Todo> todos = todoService.MakedateDesc();
            List<Schedule> schedules = scheduleService.StarttimeDesc();
            List<Event> events = new ArrayList<Event>();

            //TODO
            for(Todo todo:todos){
                //TODOは期限=end
                Event event = new Event();
                event.setTitle(todo.getTodoName());
                event.setStart(DateToString(todo.getLimitDate()) );
                event.setAllDay(true);
                event.setColor("red");
                events.add(event);
            }
            //Schedule
            for(Schedule schedule:schedules){
                Event event = new Event();
                event.setTitle(schedule.getScheduleName());
                event.setStart(DateToString(schedule.getStartTime()));
                event.setEnd(DateToString(schedule.getEndTime()));
                event.setAllDay(true);
                event.setColor("blue");
                events.add(event);

            }

            // FullCalendarにエンコード済み文字列を渡す
            ObjectMapper mapper = new ObjectMapper();
            jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonMsg;
    }
}
