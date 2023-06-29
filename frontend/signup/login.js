let accessToken = null;
let refreshToken = null;
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

function join(){
  var username = $("#idBox").val();
  var password = $("#pwBox").val();
  let flag = true;
  if(username == '') {
    $('#username').addClass("error-input");
    flag = false;
  }
  if(password == '') {
    $('#password').addClass("error-input");
    flag = false;
  }
  if(!flag){
    return;
  }
  var foo = {
      "username":username,
      "password":password
  }
  $.ajax({
    url:"http://localhost:8080/user/join",
    type: "POST",
    data: JSON.stringify(foo),
    contentType:'application/json',
    success:function(result){
      alert("회원가입 성공!");
      location.href="/app.html";
    },
    error:function(){
      alert("회원가입 실패");
      location.href="/signup/sign.html";
    }
  })
}

gsap.fromTo(
  "#login",
  { opacity: 0, x: 300 },
  { opacity: 1, x: 0, duration: 0.5 }
);

// google login
$(".google-btn").on('click', function () {
  location.href = "http://localhost:8080/oauth2/authorization/google";
});
