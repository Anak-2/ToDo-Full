package my.todo.schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.schedule.domain.dto.ScheduleRequestDto;
import my.todo.schedule.domain.dto.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.service.ScheduleService;
import my.todo.todo.domain.todo.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
//    get to_do list from schedule

//    modify schedule
//    delete schedule
//    switch to public
//    switch to private
//    finish check
//    cancel finish check
}
