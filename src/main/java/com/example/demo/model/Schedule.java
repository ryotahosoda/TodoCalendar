package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @Column(name="schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleid;

    @Size(min = 1,max = 30,message = "1文字以上30文字以下で入力してください")
    @Column(name="schedule_name")
    private String scheduleName;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy年MM月dd日")
    @Column(name = "start_time")
    private Date startTime;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy年MM月dd日")
    @Column(name = "end_time")
    private Date endTime;



    public Schedule(){
        super();
    }

    public Schedule(String name,Date start_time, Date end_time){
        super();
        this.scheduleName=name;
        this.startTime=start_time;
        this.endTime=end_time;
    }

    public Long getScheduleid(){
        return scheduleid;
    }

    public void setScheduleid(Long scheduleid){
        this.scheduleid=scheduleid;
    }

    public String getScheduleName(){
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


}
