let accessToken = null;
let refreshToken = null;
// 인증번호
let tempKey = null;
// 인증 완료된 이메일
let authCheck = false;

$(document).ready(function () {
  const searchParams = new URLSearchParams(location.search);
  for (const param of searchParams) {
    if (param[0] === 'accessToken') {
      accessToken = param[1];
      localStorage.setItem('accessToken', accessToken);
      console.log(accessToken);
    }
    else if (param[0] === 'refreshToken') {
      refreshToken = param[1];

      console.log(refreshToken);
    }
  }
  if (accessToken !== null) {
    location.href = "/mainPage/mainPage.html";
  }
}
);

function join() {

  var email = $("#emailBox").val();
  var username = $("#idBox").val();
  var password = $("#pwBox").val();

  let flag = true;
  if (username == '') {
    errorHandler("username");
    flag = false;
  }
  if (password == '') {
    errorHandler("password");
    flag = false;
  }
  if (email == '') {
    errorHandler("email");
    flag = false;
  }
  if (authCheck === false) {
    errorHandler("emailAuth");
    flag = false;
  }
  if (!flag) {
    return;
  }
  let foo = {
    "username": username,
    "password": password,
    "email": email,
    "provider": "google"
  }
  $.ajax({
    url: "http://localhost:8080/user/join",
    type: "POST",
    data: JSON.stringify(foo),
    contentType: 'application/json',
    success: function (result) {
      alert("회원가입 성공!");
      location.href = "/app.html";
    },
    error: function (jqXhr) {
      document.querySelector(".idError").innerText = jqXhr.responseText;
      document.querySelector(".idError").style.display = "inline-block";
    }
  })
}

function errorHandler(element) {
  switch (element) {
    case "username":
      document.querySelector(".idError").innerText = "아이디를 입력해주세요";
      document.querySelector(".idError").style.display = "inline-block";
      break;
    case "password":
      document.querySelector(".pwError").innerText = "비밀번호를 입력해주세요";
      document.querySelector(".pwError").style.display = "inline-block";
      break;
    case "email":
      document.querySelector(".emailError").innerText = "이메일을 입력해주세요";
      document.querySelector(".emailError").style.display = "inline-block";
      break;
    case "emailAuth":
      document.querySelector(".authError").innerText = "이메일 인증해주세요";
      document.querySelector(".authError").style.display = "inline-block";
      break;
  }
}

function getEmailBtn() {
  let emailBtn = null
  if (document.querySelector(".email-btn")) {
    emailBtn = document.querySelector(".email-btn");
  } else {
    emailBtn = document.querySelector(".resend-btn");
  }
  return emailBtn;
}

function disableEmailBtn(msg) {
  let emailBtn = getEmailBtn();
  emailBtn.classList.add("disabled-class");
  emailBtn.innerText = msg;
  emailBtn.onclick = null;
}

function activeEmailBtn(msg) {
  let emailBtn = getEmailBtn();
  if (emailBtn.classList.contains("resend-btn")) emailBtn.classList.remove("resend-btn");
  if (!emailBtn.classList.contains("email-btn")) emailBtn.classList.remove("eamil-btn");
  emailBtn.classList.remove("disabled-class");
  emailBtn.innerText = msg;
  emailBtn.disabled = false;
  emailBtn.onclick = sendEmail;
}

function activeResendEmailBtn(msg) {
  let emailBtn = getEmailBtn();
  if (emailBtn.classList.contains("email-btn")) emailBtn.classList.remove("email-btn");
  if (!emailBtn.classList.contains("resend-btn")) emailBtn.classList.remove("resend-btn");
  emailBtn.classList.remove("disabled-class");
  emailBtn.classList.add("resend-btn");
  emailBtn.innerText = msg;
  emailBtn.disabled = false;
  emailBtn.onclick = sendEmail;
}



function sendEmail() {
  var email = $("#emailBox").val();
  if (email == "" || email == " ") {
    document.querySelector(".emailError").innerText = "이메일을 입력해주세요";
    document.querySelector(".emailError").style.display = "inline-block";
    return;
  }
  email = email + "@gmail.com";
  disableEmailBtn("전송 중");
  $.ajax({
    url: `http://localhost:8080/mail/auth?to=${email}`,
    type: "GET",
    success: function (data) {
      activeResendEmailBtn("재전송");
      tempKey = data['tempKey'];
    },
    error: function (jqXhr) {
      // TODO: Email이 존재하지 않을 때 에러와 Email 이 이미 인증됐을 때 에러를 구분하는 좋은 방법
      console.log(jqXhr.responseText);
      document.querySelector(".authError").innerText = "이메일 전송 실패";
      document.querySelector(".authError").style.display = "inline-block";
      activeEmailBtn("인증번호 전송");
    }
  })
}

function checkAuth(event) {
  var authVal = document.querySelector("#authBox").value;
  let emailBtn = getEmailBtn();

  if (authVal == tempKey) {
    event.target.checked = true;
    document.querySelector(".authSuccess").innerText = "인증번호가 일치합니다";
    document.querySelector(".authSuccess").style.display = "inline-block";
    document.querySelector("#emailBox").disabled = true;
    document.querySelector("#authBox").disabled = true;
    emailBtn.classList.add("disabled-class");
    emailBtn.onclick = null;
    event.target.disabled = true;
    authCheck = true;
  } else {
    event.target.checked = false;
    document.querySelector("#authBox").value = "";
    document.querySelector(".authError").innerText = "인증번호가 일치하지 않습니다";
    document.querySelector(".authError").style.display = "inline-block";
  }
}

function hideIdError() {
  document.querySelector(".idError").style.display = "none";
}

function hidePwError() {
  document.querySelector(".pwError").style.display = "none";
}

function hideEmailError() {
  document.querySelector(".emailError").style.display = "none";
  document.querySelector(".authError").style.display = "none";
}

function hideAuthError() {
  document.querySelector(".authError").style.display = "none";
}

gsap.fromTo(
  "#signup",
  { opacity: 0, x: 300 },
  { opacity: 1, x: 0, duration: 0.5 }
);

// google login
$(".google-btn").on('click', function () {
  location.href = "http://localhost:8080/oauth2/authorization/google";
});
