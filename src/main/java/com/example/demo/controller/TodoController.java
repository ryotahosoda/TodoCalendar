package com.example.demo.controller;

import com.example.demo.Enum.SearchScheduleEnum;
import com.example.demo.Enum.SearchTodoEnum;
import com.example.demo.Enum.TodoEnum;
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
public class TodoController {
    public static final int TODOMODE = 1;
    public static final int SCHEDULEMODE = 2;
    @Autowired
    TodoService todoService;

    @Autowired
    ScheduleService scheduleService;

    //最初の呼び出し
    @RequestMapping(value = "/")
    public String index(Model model) {
        Todo todo = new Todo();
        int mode = TODOMODE;
        model.addAttribute("todo", todo);
        model.addAttribute("mode",mode);
        return "index";

    }

    //Todo→scheduleの切り替え
    @RequestMapping(value = "/",params = "change-to-schedule")
    public String ChangeToSchedule(Model model){
        Schedule schedule = new Schedule();
        int mode = SCHEDULEMODE;
        model.addAttribute("schedule", schedule);
        model.addAttribute("mode", mode);
        return "index";
    }


    //ToDoの追加を押した時
    @PostMapping(value = "/", params = "addTodo")
    public String todoRegister(@Valid @ModelAttribute Todo todo,
                               BindingResult result,
                               Model model,
                               @RequestParam("todoName") String name,
                               @RequestParam("limitDate") String limit_date,
                               @RequestParam("importance") String importance) throws ParseException {

        if (result.hasErrors()) {
            boolean errflag=true;
            int mode=TODOMODE;
            model.addAttribute("errflag", errflag);
            model.addAttribute("mode", mode);
            return "index";
        }
        String str = TodoUtil.htmlEscape(name);
        int errorcheck = errorcheck(str);
        if (errorcheck == -1) {
            int mode=TODOMODE;
            model.addAttribute("mode", mode);
            String err = "既に作成されています。";
            model.addAttribute("err", err);
            return "index";
        }
        //追加して保存
        TodoEnum imp = todoService.change(importance);
        todoService.AddSave(str,limit_date,imp);
        int mode=TODOMODE;
        Todo todoform = new Todo();
        model.addAttribute("mode", mode);
        model.addAttribute("todo", todoform);//@validのとこでmodeladdしてるからいらんかも
        return "index";

    }

    //TODOの一覧を表示
    @RequestMapping(value = "/alltodo")
    public String alltodo(Model model){
        List alltodo = todoService.MakedateDesc();
        Todo todoform = new Todo();
        if (CollectionUtils.isEmpty(alltodo)) {
            model.addAttribute("alltodo", null);
            model.addAttribute("todo", todoform);
            return "todolist";
        } else {
            model.addAttribute("alltodo", alltodo);
            model.addAttribute("todo", todoform);
            return "todolist";
        }
    }

    //TODOの追加を押したとき（TODO一覧画面で）
    @PostMapping(value = "/alltodo", params = "addTodo_list")
    public String addtodo_list(@Valid @ModelAttribute Todo todo,
                               BindingResult result,
                               Model model,
                               @RequestParam("todoName") String name,
                               @RequestParam("limitDate") String limit_date,
                               @RequestParam("importance") String importance) throws ParseException{
        if (result.hasErrors()) {//空入力とか
            List alltodo = todoService.MakedateDesc();
            int isTodoNameERROR=0;
            if (todoService.SameName(name)>0){
                String errmessage = "既に作成されています。";
                model.addAttribute("errmessage", errmessage);
            }
            model.addAttribute("isTodoNameERROR", isTodoNameERROR);
            model.addAttribute("alltodo", alltodo);
            return "todolist";
        }
        String str = TodoUtil.htmlEscape(name);
        int errorcheck = errorcheck(str);
        if (errorcheck == -1) {
            //同じ名前のTODO入力かつ時間は入力してる
            List alltodo = todoService.All();
            String errmessage = "既に作成されています。";
            int isTodoNameERROR=1;//TOP画面にも実装
            model.addAttribute("isTodoNameERROR", isTodoNameERROR);
            model.addAttribute("errmessage", errmessage);
            model.addAttribute("alltodo", alltodo);
            return "todolist";
        }
        //追加して保存+全部のTODOを表示させる
        todoService.AddSave(str,limit_date,todoService.change(importance));
        List alltodo = todoService.MakedateDesc();
        Todo todoform = new Todo();
        model.addAttribute("alltodo", alltodo);
        model.addAttribute("todo", todoform);//同じ
        return "todolist";
    }

    //完了⇄未完了（完了・未完了ボタンを押した場合）
    @PostMapping(value = "/alltodo", params = "finish")//valueとパラメータをそろえる
    public String finish(@Valid @ModelAttribute Todo todo,
                         BindingResult result,
                         Model model, @RequestParam("finish") Long id) {
        todoService.updateFinish(id);
        List alltodo = todoService.MakedateDesc();
        Todo todoForm = new Todo();
        model.addAttribute("alltodo", alltodo);
        model.addAttribute("todo", todoForm);
        return "todolist";
    }

    //編集ボタンを押したとき(TODO一覧画面内で）
    @RequestMapping("/editTODO")
    public String edit(Model model, @RequestParam("editTodo") Long id) {
        Todo todoForm = new Todo();
        Todo edittodo = todoService.getTodo(id);
        Enum imp = edittodo.getImportance();
        model.addAttribute("edittodo", edittodo);
        model.addAttribute("todo", todoForm);
        model.addAttribute("imp",imp);
        return "todoedit";
    }

    //更新ボタンを押したとき(TODO編集画面内で)
    @PostMapping(value = "/alltodo", params = "updateTODO")
    public String update(@Valid @ModelAttribute Todo todo,
                         BindingResult result,
                         Model model,
                         @RequestParam("updateTODO") Long id,
                         @RequestParam("todoName") String name,
                         @RequestParam("limitDate") String limit_date,
                         @RequestParam("importance") String importance) throws ParseException {
        if (result.hasErrors()) {
            Todo edittodo = todoService.getTodo(id);
            Enum imp = edittodo.getImportance();
            model.addAttribute("imp",imp);
            model.addAttribute("edittodo", edittodo);
            return "todoedit";
        }
        String str = TodoUtil.htmlEscape(name);
        todoService.updateNameandLimitDateImp(id, str, limit_date, todoService.change(importance));
        List alltodo = todoService.MakedateDesc();
        Todo todoForm = new Todo();
        model.addAttribute("alltodo", alltodo);
        model.addAttribute("todo", todoForm);
        return "todolist";
    }


    //検索ページに飛んだ時
    @RequestMapping("/search")
    public String search(Model model) {
        Enum searchTodo = SearchTodoEnum.first;
        Enum searchSchedule = SearchScheduleEnum.first;
        model.addAttribute("searchTodo", searchTodo);
        model.addAttribute("searchSchedule",searchSchedule);
        return "search";
    }

    //検索のボタンを押したとき
    @PostMapping(value = "/search", params = "search")
    public String search_preview(Model model, @RequestParam("searchName") String searchName) {
        //検索が空入力の時
        if(searchName==""){
            Enum searchTodo = SearchTodoEnum.notAvailable;
            Enum searchSchedule = SearchScheduleEnum.notAvailable;
            int countTodo = 0;
            model.addAttribute("data", null);
            model.addAttribute("countTodo", countTodo);
            model.addAttribute("searchData", searchName);
            model.addAttribute("searchTodo",searchTodo);
            model.addAttribute("searchSchedule",searchSchedule);
            return "search";
        }
        else{
            List todo = todoService.NameLikeFinishMakedateDesc(searchName);
            List schedule = scheduleService.NameLikeStartTimeDesc(searchName);
            int countTodo = todo.size();
            int countSchedule = schedule.size();

            //TODO有・予定なし
            if(countTodo > 0 && countSchedule == 0){
                Enum searchTodo = SearchTodoEnum.Available;
                Enum searchSchedule = SearchScheduleEnum.notAvailable;
                model.addAttribute("todoData", todo);
                model.addAttribute("countTodo", countTodo);
                model.addAttribute("searchData", searchName);
                model.addAttribute("searchTodo",searchTodo);
                model.addAttribute("searchSchedule",searchSchedule);
                return "search";
            }
            //TODOなし・予定あり
            else if(countTodo == 0 && countSchedule > 0){
                Enum searchTodo = SearchTodoEnum.notAvailable;
                Enum searchSchedule = SearchScheduleEnum.Available;
                model.addAttribute("scheduleData", schedule);
                model.addAttribute("countSchedule", countSchedule);
                model.addAttribute("searchData", searchName);
                model.addAttribute("searchTodo",searchTodo);
                model.addAttribute("searchSchedule",searchSchedule);
                return "search";
            }

            //なし・なし
            else if(countTodo == 0 && countSchedule ==0){
                Enum searchTodo = SearchTodoEnum.notAvailable;
                Enum searchSchedule = SearchScheduleEnum.notAvailable;
                model.addAttribute("todoData", todo);
                model.addAttribute("countTodo", countTodo);
                model.addAttribute("scheduleData", schedule);
                model.addAttribute("countSchedule", countSchedule);
                model.addAttribute("searchData", searchName);
                model.addAttribute("searchTodo",searchTodo);
                model.addAttribute("searchSchedule",searchSchedule);
                return "search";
            }
            //TODOあり・予定あり
            else{
                Enum searchTodo = SearchTodoEnum.Available;
                Enum searchSchedule = SearchScheduleEnum.Available;
                model.addAttribute("todoData", todo);
                model.addAttribute("countTodo", countTodo);
                model.addAttribute("scheduleData", schedule);
                model.addAttribute("countSchedule", countSchedule);
                model.addAttribute("searchData", searchName);
                model.addAttribute("searchTodo",searchTodo);
                model.addAttribute("searchSchedule",searchSchedule);
                return "search";
            }

        }

    }

    @PostMapping(value = "/search", params = "finish", produces = "text/plain;charset=UTF-8")
    public String search_finish(Model model,
                                @RequestParam("finish") Long id,
                                @RequestParam("searchName") String searchName) {
        todoService.updateFinish(id);
        List todo = todoService.NameLikeMakedateDesc(searchName);
        List schedule = scheduleService.NameLikeStartTimeDesc(searchName);
        int countTodo = todo.size();
        int countSchedule = schedule.size();
        //TODO有・予定なし
        if(countTodo > 0 && countSchedule == 0){
            Enum searchTodo = SearchTodoEnum.Available;
            Enum searchSchedule = SearchScheduleEnum.notAvailable;
            model.addAttribute("todoData", todo);
            model.addAttribute("countTodo", countTodo);
            model.addAttribute("searchData", searchName);
            model.addAttribute("searchTodo",searchTodo);
            model.addAttribute("searchSchedule",searchSchedule);
            return "search";
        }
        //TODOなし・予定あり
        else if(countTodo == 0 && countSchedule > 0){
            Enum searchTodo = SearchTodoEnum.notAvailable;
            Enum searchSchedule = SearchScheduleEnum.Available;
            model.addAttribute("scheduleData", schedule);
            model.addAttribute("countSchedule", countSchedule);
            model.addAttribute("searchData", searchName);
            model.addAttribute("searchTodo",searchTodo);
            model.addAttribute("searchSchedule",searchSchedule);
            return "search";
        }
        //TODOあり・予定あり
        else{
            Enum searchTodo = SearchTodoEnum.Available;
            Enum searchSchedule = SearchScheduleEnum.Available;
            model.addAttribute("todoData", todo);
            model.addAttribute("countTodo", countTodo);
            model.addAttribute("scheduleData", schedule);
            model.addAttribute("countSchedule", countSchedule);
            model.addAttribute("searchData", searchName);
            model.addAttribute("searchTodo",searchTodo);
            model.addAttribute("searchSchedule",searchSchedule);
            return "search";
        }


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


