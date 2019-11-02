package com.example.demo.controller;

import com.example.demo.model.Schedule;
import com.example.demo.model.Todo;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.TodoService;
import com.example.demo.util.TodoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;


@Controller
public class ScheduleController {
    public static final int TODOMODE = 1;
    public static final int SCHEDULEMODE = 2;
    @Autowired
    TodoService todoService;
    @Autowired
    ScheduleService scheduleService;

    //schedule→Todoの切り替え
    @RequestMapping(value = "/",params = "change-to-todo")
    public String ChangeToTodo(Model model){
        Todo todo = new Todo();
        int mode = TODOMODE;
        model.addAttribute("todo", todo);
        model.addAttribute("mode", mode);
        return "index";
    }

    //予定の追加を押した時(TOPで)
    @PostMapping(value = "/", params = "addschedule")
    public String scheduleRegister(@Valid @ModelAttribute Schedule schedule,
                                   BindingResult result,
                                   Model model,
                                   @RequestParam("scheduleName") String scheduleName,
                                   @RequestParam("startTime") String startTime,
                                   @RequestParam("endTime") String endTime) throws ParseException {

        if (result.hasErrors()) {
            boolean isSchedulenameError = true;
            int mode = SCHEDULEMODE;
            model.addAttribute("isSchedulenameError", isSchedulenameError);
            model.addAttribute("mode", mode);
            return "index";
        }
        String str = TodoUtil.htmlEscape(scheduleName);
        int errorcheck = errorcheck(str);
        if (errorcheck == -1) {
            int mode = SCHEDULEMODE;
            model.addAttribute("mode", mode);
            String err = "既に作成されています。";
            model.addAttribute("err", err);
            return "index";
        }
        //追加して保存
        scheduleService.AddSave(str,startTime,endTime);
        int mode=SCHEDULEMODE;
        model.addAttribute("mode", mode);
        return "index";

    }

    //予定の一覧を表示
    @RequestMapping(value = "/allschedule")
    public String allschedule(Model model){
        List allschedule = scheduleService.StarttimeDesc();
        Schedule scheduleForm = new Schedule();
        if (CollectionUtils.isEmpty(allschedule)) {
            model.addAttribute("allschedule", null);
            model.addAttribute("schedule", scheduleForm);
            return "schedulelist";
        } else {
            model.addAttribute("allschedule", allschedule);
            model.addAttribute("schedule", scheduleForm);
            return "schedulelist";
        }
    }

    //予定の追加を押したとき（予定一覧内で）
    @PostMapping(value = "/allschedule", params = "addschedule_list")
    public String addschedule_list(@Valid @ModelAttribute Schedule schedule,
                                   BindingResult result,
                                   Model model,
                                   @RequestParam("scheduleName") String scheduleName,
                                   @RequestParam("startTime") String startTime,
                                   @RequestParam("endTime") String endTime) throws ParseException{
        if (result.hasErrors()) {
            List allschedule = scheduleService.StarttimeDesc();
            int errflag=1;//TOP画面にも実装しないといけない
            model.addAttribute("allschedule", allschedule);
            model.addAttribute("errflag", errflag);
            return "schedulelist";
        }
        String str = TodoUtil.htmlEscape(scheduleName);
        int errorcheck = errorcheck(str);
        if (errorcheck == -1) {
            //同じ名前のschedule入力かつ時間は入力してる
            List allschedule = scheduleService.All();
            model.addAttribute("allschedule", allschedule);
            String errmessage = "既に作成されています。";
            model.addAttribute("errmessage", errmessage);
            return "schedulelist";
        }
        //追加して保存+全部のSCHEDULEを表示させる
        scheduleService.AddSave(str,startTime,endTime);
        List allschedule = scheduleService.StarttimeDesc();
        model.addAttribute("allschedule", allschedule);
        return "schedulelist";
    }

    //編集ボタンを押したとき（予定一覧画面内で）
    @RequestMapping("/editSchedule")
    public String editSchedule(Model model, @RequestParam("editSchedule")Long scheduleId){
        Schedule scheduleForm = new Schedule();
        Schedule editSchedule = scheduleService.getSchedule(scheduleId);
        model.addAttribute("schedule", scheduleForm);
        model.addAttribute("editSchedule", editSchedule);
        return "scheduleedit";
    }

    //更新ボタンを押したとき(予定編集画面内で)
    @PostMapping(value = "/allschedule",params = "updateschedule")
    public String updateSchedule(@Valid @ModelAttribute Schedule schedule,
                                 BindingResult result,
                                 Model model,
                                 @RequestParam("updateschedule") Long scheduleId,
                                 @RequestParam("scheduleName")String scheduleName,
                                 @RequestParam("startTime")String startTime,
                                 @RequestParam("endTime")String endTime) throws ParseException{
        if(result.hasErrors()){
            Schedule editSchedule = scheduleService.getSchedule(scheduleId);
            model.addAttribute("editSchedule",editSchedule);
            return "scheduleedit";
        }
        String str = TodoUtil.htmlEscape(scheduleName);
        scheduleService.updateScheduleNameStartTimeEndTime(scheduleId,str,startTime,endTime);
        List allSchedule = scheduleService.StarttimeDesc();
        model.addAttribute("allschedule",allSchedule);
        return "schedulelist";
    }

    public int errorcheck(String name) {
        List todo = todoService.NameMakedateDesc(name);
        if (CollectionUtils.isEmpty(todo)) {
            int mes = 1;
            return mes;
        } else {
            int mes = -1;
            return mes;
        }
    }
}
