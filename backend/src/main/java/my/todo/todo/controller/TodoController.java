package my.todo.todo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.todo.domain.dto.request.TodoRequestDto;
import my.todo.todo.domain.dto.request.TodoUpdateRequestDto;
import my.todo.todo.domain.dto.response.TodoResponseDto;
import my.todo.todo.service.StorageService;
import my.todo.todo.service.TodoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value="/todo")
@Slf4j
@RequiredArgsConstructor
public class TodoController {

    private final StorageService storageService;
    private final TodoService todoService;

    @PostMapping(value="/upload-file")
    public ResponseEntity<String> uploadFile(MultipartFile file) throws IllegalStateException, IOException{
//        if(!file.isEmpty()){
//            log.debug("file org name = {}", file.getOriginalFilename());
//            log.debug("file content type = {}", file.getContentType());
//            file.transferTo(new File(file.getOriginalFilename()));
//        }
        storageService.store(file);
        return new ResponseEntity<>("Upload Success", HttpStatus.OK);
    }

    @GetMapping(value="/download-file")
    public ResponseEntity<Resource> downloadFile(String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename()+"\"").body(file);
    }

    @DeleteMapping(value="/delete-file")
    public ResponseEntity<String> deleteFile(String filename){
        boolean result = storageService.delete(filename);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Delete the file successfully: "+filename);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete file: "+filename);
        }
    }

    @PostMapping(value="/add")
    public void addTodo(@RequestBody TodoRequestDto todoRequestDto){
        todoService.addTodo(todoRequestDto);
    }

//    todoId 를 pathVariable 로 받게 된 이유 : API 방식이기 때문에 RequestMapping(...?id=id값) 을 이용해 현재 주소를 사용하기엔 API 스럽지 않다 (쿼리 스트링),
//    todo 특징 상 PK 는 todoId 뿐이므로 URL 을 통해 전달받아야한다.
    @GetMapping(value="/{todoId}")
    public TodoResponseDto getTodo(@PathVariable Long todoId){
        return todoService.getTodo(todoId);
    }

//    delete todo
    @DeleteMapping(value = "/{todoId}")
    public void deleteTodo(@PathVariable Long todoId){
        todoService.deleteTodo(todoId);
    }
//    update todo
//    TODO: 근데 수정 권한이 없는 사용자가 바꾸려는 시도를 막기 위해 암호화한 ID 를 사용해야겠는데... uuid?
    @PutMapping(value = "/{todoId}")
    public void updateTodo(@PathVariable Long todoId, @RequestBody TodoUpdateRequestDto todoUpdateRequestDto){
        todoService.updateTodo(todoId, todoUpdateRequestDto);
    }
//
}
