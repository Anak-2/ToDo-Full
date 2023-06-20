let accessToken = localStorage.getItem("accessToken");
console.log(accessToken);
if(accessToken == null){
  alert("로그인이 필요합니다!");
  location.href="/app.html";
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
      console.log("request status: " + request.status);
      if (request.status === 201) {
        console.log("Refresh Access Token");
        console.log("");
        console.log("=========================================");
        console.log("[testMain] : [http success]");
        console.log("-----------------------------------------");
        console.log("[header all] : " + request.getAllResponseHeaders());
        console.log("-----------------------------------------");
        console.log("[header content-type] : " + request.getResponseHeader('content-type'));
        console.log("-----------------------------------------");
        console.log("[header authorization] : " + request.getResponseHeader('authorization'));
        console.log("-----------------------------------------");
        console.log("[textStatus] : " + JSON.stringify(textStatus));
        console.log("-----------------------------------------");
        console.log("[response data] : " + JSON.stringify(data));
        console.log("=========================================");
        console.log("");
        accessToken = request.getResponseHeader('authorization');
        if(accessToken !== null){
          localStorage.setItem("accessToken", accessToken);
        }
      }
      document.querySelector(".user-name").textContent = data['username'];
    },
    error: function (request,status,error) {
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      localStorage.removeItem("accessToken");
      location.href = "./app.html";
    }
  })