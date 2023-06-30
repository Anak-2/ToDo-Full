package my.todo.schedule.controller;

import lombok.RequiredArgsConstructor;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.service.ScheduleService;
import my.todo.todo.domain.todo.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
//    add schedule
//    find schedule
//    get to_do list from schedule
//    modify schedule
//    delete schedule
//    switch to public
//    switch to private
//    finish check
//    cancel finish check
}
