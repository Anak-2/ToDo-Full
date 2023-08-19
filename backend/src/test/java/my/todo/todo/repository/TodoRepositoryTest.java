package my.todo.todo.repository;

import jakarta.persistence.EntityManager;
import my.todo.todo.domain.todo.Todo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@Slf4j
@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    EntityManager em;

    public TodoRepositoryTest(EntityManager em){
        this.em = em;
    }

    @Test
    @DisplayName("Todo의 Content 업데이트")
    void find_todo_and_content_update(){
        String UPDATE_MESSAGE = "Content Updated";

        //given
        System.out.println("업데이트1");
        Todo todo = make_todo();
        Todo savedTodo = todoRepository.save(todo);
        em.flush();
        Todo findTodo = todoRepository.findById(savedTodo.getId()).orElseThrow();

        //when
        System.out.println("업데이트2");
        findTodo.updateContent(UPDATE_MESSAGE);
        em.flush();
        Todo findUpdatedTodo = todoRepository.findById(findTodo.getId()).orElseThrow();

        //then
        System.out.println("업데이트3");
        String updatedMessage = findUpdatedTodo.getContent();
        Assertions.assertEquals(UPDATE_MESSAGE, updatedMessage);
    }

    public Todo make_todo(){
        return Todo.builder()
                .isFinished(false)
                .schedule(null)
                .title("title1")
                .content("Content1")
                .build();
    }
}