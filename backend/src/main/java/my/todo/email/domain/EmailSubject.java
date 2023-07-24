package my.todo.email.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailSubject {
    AUTH_SUBJECT("[no-reply] Master Of Todo 본인 인증 메일");

    private final String subject;

}
