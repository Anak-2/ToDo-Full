package my.todo.schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleWithTodoRequest;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.dto.response.ScheduleWithTodoResponse;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.service.ScheduleService;
import my.todo.todo.domain.dto.TodoResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

//    add schedule
    @PostMapping("/add")
    public void add(@RequestBody ScheduleRequestDto scheduleRequestDto){
        log.debug(scheduleRequestDto.toString());
        scheduleService.add(scheduleRequestDto);
    }

//    get schedule list by user id
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getScheduleList(UserRequestDto.LoginDTO loginDTO){
        return scheduleService.findScheduleList(loginDTO);
    }
//    get schedule info with todo list
    public ScheduleWithTodoResponse getScheduleWithToDoList(ScheduleWithTodoRequest scheduleWithToDoRequest){
        return scheduleService.findScheduleWithTodo(scheduleWithToDoRequest);
    }

//    modify schedule
//    delete schedule
//    switch to public
//    switch to private
//    finish check
//    cancel finish check
}
