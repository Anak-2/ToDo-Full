package my.todo.global.errormsg;

public enum UserError {
    USER_NOT_EXIST("회원을 찾지 못했습니다"), USERNAME_DUPLICATED("중복된 아이디 존재"), EMAIL_DUPLICATED("중복된 이메일 존재"),
    EMAIL_NOT_FOUND("이메일로 가입한 회원을 찾지 못했습니다");
    private final String msg;

    UserError(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
