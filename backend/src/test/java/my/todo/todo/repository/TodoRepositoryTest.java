package my.todo.todo.repository;

import jakarta.persistence.EntityManager;
import my.todo.todo.domain.todo.Todo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void find_todo_and_content_update(){

        String UPDATE_MESSAGE = "Content Updated";

        //given
        Todo todo = make_todo();
        Todo savedTodo = todoRepository.save(todo);
        testEntityManager.flush();
        Todo findTodo = todoRepository.findById(savedTodo.getId()).orElseThrow();

        //when
        findTodo.updateContent(UPDATE_MESSAGE);
        testEntityManager.flush();
        Todo findUpdatedTodo = todoRepository.findById(findTodo.getId()).orElseThrow();

        //then
        String updatedMessage = findUpdatedTodo.getContent();
        Assertions.assertThat(updatedMessage).isEqualTo(UPDATE_MESSAGE);
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