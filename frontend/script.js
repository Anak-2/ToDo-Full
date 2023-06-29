var user_id = document.querySelector(".inputBox").innerText;

function loginProcess() {
  var username = $("#username").val();
  var password = $("#password").val();
  let flag = true;
  if (username == '') {
    $('#username').addClass("error-input");
    flag = false;
  }
  if (password == '') {
    $('#password').addClass("error-input");
    flag = false;
  }
  if (!flag) {
    return;
  }
  var foo = {
    "username": username,
    "password": password
  }
  $.ajax({
    url: "http://localhost:8080/user/login",
    type: "POST",
    async: false,
    headers: { 'Content-Type': 'application/json' },
    data: JSON.stringify(foo), // WEB 에서 정보를 보낼 때 문자열이어야 한다
    success: function (result) {
      // refresh token 은 back 에서 cookie 에 저장
      accessToken = result.accessToken;
      localStorage.setItem('accessToken', accessToken);
      // localStorage.setItem('accessTokenExpirationTime',result.accessTokenExpirationTime);
      location.href = "/mainPage/mainPage.html";
    },
    error: function () {
      window.alert("로그인 정보가 잘못 됐습니다");
      let url = document.location.href;
      location.replace(url);
    }
  })
}

let accessToken = null;
let refreshToken = null;
// $(document).ready(
// function () { 
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
// }
// );

// google login
$(".google-btn").on('click', function () {
  location.href = "http://localhost:8080/oauth2/authorization/google";
});


gsap.fromTo(
  "#login",
  { opacity: 0, x: 300 },
  { opacity: 1, x: 0, duration: 0.5 }
);
