let accessToken = localStorage.getItem("accessToken");
// export { accessToken };
if (accessToken == null) {
  alert("로그인이 필요합니다!");
  location.href = "/app.html";
}
$.ajax({
  url: "http://localhost:8080/user/user-info",
  type: "GET",
  headers: { 'Authorization': accessToken },
  // 다른 Domain (같은 localhost 이지만! 127.0.0.1 != localhost) 에 document cookie 를 보내기 위한 설정
  crossDomain: true,
  xhrFields: {
    withCredentials: true
  },
  success: function (data, textStatus, request) {
    // 서버에서 201 을 보내면 새로운 Access Token 을 발행했다는 의미
    // 또는 request.getResponseHeader('authorization') 가 null 인지 아닌지로 처리해도 좋다
    // ToDo: Back 에서 request.getCookies 가 null 이 뜨는 이유 찾기 (JSESSION, cookie 이상 x)
    // 해결: domain 이 달라서 (127.0.0.1 , localhost) 생겼던 문제
    // ToDo: localhost 로 통일시킨 것 같은데 remote url request url 이 달라서 그런지 header 가 전달되지 않는 문제
    console.log("request status: " + request.status);
    if (request.status === 201) {
      // console.log("Refresh Access Token");
      // console.log("");
      // console.log("=========================================");
      // console.log("[testMain] : [http success]");
      // console.log("-----------------------------------------");
      // console.log("[header all] : " + request.getAllResponseHeaders());
      // console.log("-----------------------------------------");
      // console.log("[header content-type] : " + request.getResponseHeader('content-type'));
      // console.log("-----------------------------------------");
      // console.log("[header authorization] : " + request.getResponseHeader('Authorization'));
      // console.log("-----------------------------------------");
      // console.log("[textStatus] : " + JSON.stringify(textStatus));
      // console.log("-----------------------------------------");
      // console.log("[response data] : " + JSON.stringify(data));
      // console.log("=========================================");
      // console.log("");
      accessToken = request.getResponseHeader('Authorization');
      if (accessToken !== null) {
        localStorage.setItem("accessToken", accessToken);
      }
    }
    // ToDo: 이 js에서 선언한 변수를 다른 파일에서 쓸 수 있도록 더 좋은 방법 생각하기
    // export, import 
    $('.user-name').text(data['username']);
    $('.user-name').val(data['username']);
    $('.id').val(data['id']);
    if (data['password'] == null) {
      $('.password').attr('placeholder', 'Empty');
    } else {
      $('.password').val(data['password']);
    }
    $('.create-date').val(data['createDate']);
    $('.email').val(data['email']);
    $('.name').val(data['username']);
    $('.role').val(data['role']);
  },
  error: function (jqXHR) {
    alert("code:" + jqXHR.status + "\n" + jqXHR.statusText);
    localStorage.removeItem("accessToken");
    location.href = "/app.html";
  }
})

// function enablePasswordChange(clicked_id){
//   let revise_btn = document.querySelector("#"+clicked_id);
//   console.log(revise_btn.attributes);
// }

function reviseUser() {
  let username = $('.user-name').val();
  let password = $('.password').val();
  var user = {
    "username": username,
    "password": password
  }
  $.ajax({
    url: 'http://localhost:8080/user/update',
    type: 'POST',
    headers: { 'Authorization': accessToken, 'Content-Type': 'application/json' },
    data: JSON.stringify(user),
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    success: function () {
      alert("회원 정보가 수정됐습니다!");
    },
    error: function () {

    }
  })
}

function deleteUser() {
  let username = $('.user-name').val();
  var user = { "username": username };
  let result = confirm("탈퇴하시겠습니까?");
  if (result) {
    $.ajax({
      url: 'http://localhost:8080/user/delete',
      type: 'DELETE',
      headers: { 'Authorization': accessToken, 'Content-Type': 'application/json' },
      data: JSON.stringify(user),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      success: function () {
        alert("정상적으로 탈퇴했습니다!");
        location.href = "/app.html";
      },
      error: function (errorThrown) {
        alert(errorThrown);
      }
    });
  }
}