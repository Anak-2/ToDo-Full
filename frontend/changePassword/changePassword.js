const url = new URL(window.location.href);
const params = new URLSearchParams(url.search);

const username = params.get("username");
const tempToken = params.get("tempToken");
console.log(tempToken);

function reviseUser() {

    let password = null;
    const temp1 = $("#pw1").val();
    const temp2 = $("#pw2").val();

    if (temp1 != "" && temp1 == temp2) password = temp1;
    else {
        console.log("does not match");
        return;
    }

    var user = {
        "username": username,
        "password": password
    }
    $.ajax({
        url: 'http://localhost:8080/user/update',
        type: 'POST',
        headers: { 'Authorization': tempToken, 'Content-Type': 'application/json' },
        data: JSON.stringify(user),
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        success: function () {
            alert("회원 정보가 수정됐습니다!");
            location.href = "/app.html";
        },
        error: function (jqXHR) {
            alert(jqXHR.responseText);
        }
    })
}

gsap.fromTo(
    "#changePasswordContainer",
    { opacity: 0, x: 300 },
    { opacity: 1, x: 0, duration: 0.5 }
);
