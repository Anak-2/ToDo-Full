package my.todo.todo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.todo.domain.dto.TodoRequestDto;
import my.todo.todo.service.StorageService;
import my.todo.todo.service.TodoService;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @DeleteMapping(value="delete-file")
    public ResponseEntity<String> deleteFile(String filename){
        boolean result = storageService.delete(filename);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Delete the file successfully: "+filename);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete file: "+filename);
        }
    }

    @PostMapping(value="add")
    public void add(@RequestBody TodoRequestDto todoRequestDto){
        todoService.addTodo(todoRequestDto);
    }
}
