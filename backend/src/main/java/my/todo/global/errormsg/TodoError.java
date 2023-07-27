package my.todo.global.errormsg;

import lombok.Getter;

@Getter
public enum TodoError {
    TODO_NOT_FOUND("해당하는 TODO를 찾지 못했습니다");
    private final String msg;

    TodoError(String msg){
        this.msg = msg;
    }
}
