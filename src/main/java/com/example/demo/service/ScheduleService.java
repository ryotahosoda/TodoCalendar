package com.example.demo.service;

import com.example.demo.model.Schedule;
import com.example.demo.model.Todo;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    //作成日で降順に並べる
    @Transactional
    public List StarttimeDesc(){
        return scheduleRepository.findAllByOrderByStartTimeDesc();
    }

    @Transactional
    public List All(){
        return scheduleRepository.findAll();
    }

    @Transactional
    public void AddSave(String str, String start_time, String end_time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date startTime = sdf.parse(start_time);
        Date endTime = sdf.parse(end_time);
        Schedule addschedule = new Schedule(str, startTime, endTime);
        scheduleRepository.saveAndFlush(addschedule);
    }

    @Transactional
    public Schedule getSchedule(Long scheduleId){
        Schedule schedule = scheduleRepository.getOne(scheduleId);
        //nullの時の対応未実装
        return schedule;
    }

    @Transactional
    public void updateScheduleNameStartTimeEndTime(Long scheduleId, String scheduleName, String startTime, String endTime)throws ParseException{
        Schedule schedule = getSchedule(scheduleId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date sTime = sdf.parse(startTime);
        Date eTime = sdf.parse(endTime);
        schedule.setScheduleName(scheduleName);
        schedule.setStartTime(sTime);
        schedule.setEndTime(eTime);
        scheduleRepository.saveAndFlush(schedule);
    }

    //名前の部分一致で開始時間で降順に
    @Transactional
    public List NameLikeStartTimeDesc(String scheduleName){
        StringBuilder buf = new StringBuilder();
        buf.append("%"+scheduleName+"%");
        return scheduleRepository.findByScheduleNameLikeOrderByStartTimeDesc(buf.toString());
    }


}
