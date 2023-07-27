function sendEmail() {
    let email = $("#emailBox").val();
    if (email == "" || email == " ") {
        document.querySelector(".emailError").innerText = "이메일을 입력해주세요";
        document.querySelector(".emailError").style.display = "inline-block";
        return;
    }
    email = email + "@gmail.com";
    $.ajax({
        url: `http://localhost:8080/mail/username?to=${email}`,
        type: "GET",
        success: function (data) {
            alert("전송 완료");
        },
        error: function (jqXhr, errorThrown) {
            // TODO: Email이 존재하지 않을 때 에러와 Email 이 이미 인증됐을 때 에러를 구분하는 좋은 방법
            console.log(jqXhr.responseText);
        }
    })
}

function sendUsername() {
    let username = $("#usernameBox").val();
    if (username == "" || username == " ") {
        document.querySelector(".usernameError").innerText = "아이디를 입력해주세요";
        document.querySelector(".usernameError").style.display = "inline-block";
        return;
    }
    $.ajax({
        url: `http://localhost:8080/mail/password?username=${username}`,
        type: "GET",
        success: function (data) {
            alert("전송 완료");
        },
        error: function (jqXhr, errorThrown) {
            console.log(jqXhr.responseText);
        }
    })
}

gsap.fromTo(
    "#findContainer",
    { opacity: 0, x: 300 },
    { opacity: 1, x: 0, duration: 0.5 }
);
