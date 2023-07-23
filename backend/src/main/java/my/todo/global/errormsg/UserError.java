package my.todo.global.errormsg;

public enum UserError {
    USER_NOT_EXIST("유저를 찾지 못했습니다"), DUPLICATED("중복된 아이디 존재");
    final private String msg;

    UserError(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
