let inputBox = document.querySelector(".input-container .inputBox");
let searchBox = document.querySelector(".searchBox");
let list = document.querySelector(".list");
// import { accessToken } from "../userInfo/chekUser.js"; -> cannot use import statement outside a module 문제 발생
let accessToken = localStorage.getItem("accessToken");


searchBox.addEventListener("keyup", function (e) {
  let listItem = document.querySelectorAll(".list-container .list-item");
  for (let i = 0; i < listItem.length; i++) {
    if (listItem[i].innerText.includes(searchBox.value, 0)) {
      listItem[i].parentElement.style.display = "flex";
    } else {
      listItem[i].parentElement.style.display = "none";
    }
  }
});

function initInput() {
  const inputDate = document.querySelector(".date-input");
  const inputTime = document.querySelector(".time-input");

  //init input values and date input
  inputBox.value = "";
  inputDate.value = inputDate.min;

  // init time input
  const date = new Date();
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  inputTime.value = `${hours}:${minutes}`;

}
// ******** Side Menu Func ********

//open side menu
function openSideMenu() {
  const sideMenu = document.querySelector(".side-menu");
  if (sideMenu.classList.contains("side-menu-hidden")) {
    sideMenu.classList.remove("side-menu-hidden");
    sideMenu.classList.add("side-menu-visible");
  } else {
    sideMenu.classList.remove("side-menu-visible");
    sideMenu.classList.add("side-menu-hidden");
  }
}

function insertSchedule(id, title, isPublic) {
  let isLocked = isPublic ? "unlocked" : "locked";
  let imgSrc = isPublic ? "/img/unlock.png" : "/img/lock.png";
  let scheduleList = document.querySelector(".schedule-list");
  scheduleList.insertAdjacentHTML("beforeend",
    `<li>
    <div class="schedule-id">${id}</div>
    <span>${title}</span>
    <img class="lock ${isLocked}" onClick="lockEvent(this)" src="${imgSrc}">
    </li>`);
}
// insert schedule in list
function addSchedule() {
  let title = document.querySelector(".schedule-inputBox").value;
  if (title === null || title === undefined || title === '') {
    return 0;
  }
  let schedule = {
    "title": title,
    "isPublic": false
  }
  $.ajax({
    method: "POST",
    url: "http://localhost:8080/schedule/add",
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    headers: { "Content-Type": "application/json", 'Authorization': accessToken },
    data: JSON.stringify(schedule),
    success: function (data) {
      document.querySelector(".schedule-inputBox").value = "";
      insertSchedule(data['id'], data['title'], data['isPublic']);
    },
    error: function () {
      console.log("add schedule 실패");
    }
  })
};
document.querySelector(".schedule-inputBox").addEventListener("keyup", function (e) {
  if (e.keyCode === 13) {
    addSchedule();
  }
});
document.querySelector(".schedule-add-btn").addEventListener("click", addSchedule);
// get shcedule list from backend
function getScheduleList() {
  $.ajax({
    method: "GET",
    url: "http://localhost:8080/schedule/schedules",
    headers: { 'Authorization': accessToken },
    // 다른 Domain (같은 localhost 이지만! 127.0.0.1 != localhost) 에 document cookie 를 보내기 위한 설정
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    success: function (data) {
      console.log(data);
      data.forEach(element => {
        insertSchedule(element['id'], element['title'], element['isPublic']);
      });
    },
    error: function () {
      console.log("getScheduleList 실패");
    }
  })
}

getScheduleList();

//  ToDo: public , private 조건 주기
function lockEvent(lockElement) {
  const lockWrapper = lockElement.parentElement;
  const scheduleId = lockWrapper.children[0].innerText;
  const title = lockWrapper.children[1].innerText;
  var locked = lockElement.classList.contains('locked') ? true : false;
  console.log(locked);
  const requestData = {
    "id": scheduleId,
    "title": title,
    "isPublic": locked
  }
  console.log(requestData);
  $.ajax({
    method: "PUT",
    url: "http://localhost:8080/schedule/update",
    headers: { "Content-Type": "application/json", 'Authorization': accessToken },
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    data: JSON.stringify(requestData),
    success: function () {
      if (!locked) {
        lockElement.classList.remove('unlocked');
        lockElement.classList.add('locked');
        lockElement.src = "/img/lock.png";
        console.log('Lock closed');
      } else {
        lockElement.classList.remove('locked');
        lockElement.classList.add('unlocked');
        lockElement.src = "/img/unlock.png";
        console.log('Lock opened');
      }
    },
    error: function () {
      alert("schedule update 실패");
    }
  })
}

// ******** List Func ********

function getTodoList(scheduleTitle) {

}
//add key event listencer
inputBox.addEventListener("keyup", function (e) {
  if (e.keyCode === 13) {
    insertList(e);
  }
});

inputBox.addEventListener("focus", function (e) {
  // give shadow to list-inpu document.querySelector(".input-info")t-container
  inputBox.parentElement.style.boxShadow = "0 2px 6px rgb(0 0 0 / 20%)";
  const inputInfo = document.querySelector(".input-info");
  if (inputInfo.classList.contains("input-info-hidden")) {
    inputInfo.classList.remove("input-info-hidden");
    inputInfo.classList.add("input-info-visible");
  }
});

//when inputBox lost focus, it activates (it works but it couldn't handle hiding input-info)
// inputBox.addEventListener("blur", function (e) {
//   inputBox.parentElement.style.boxShadow = "none";
// });

//when click outside of the list-input, hide box-shadow and input-info
document.addEventListener("mouseup", function (e) {
  let container = document.querySelector(".list-input");
  const inputInfo = document.querySelector(".input-info");
  if (!container.contains(e.target)) {
    if (inputInfo.classList.contains("input-info-visible")) {
      inputInfo.classList.remove("input-info-visible");
      inputInfo.classList.add("input-info-hidden");
    }
    inputBox.parentElement.style.boxShadow = "none";
  }
});

function makeListWrapper() {
  const listWrapper = document.createElement("div");
  listWrapper.classList.add("list-wrapper");
  const listItem = document.createElement("li");

  //combine list with inputdate and put in listWrapper
  const inputTime = document.querySelector(".time-input");
  const deadline = document.createElement("span");
  deadline.innerText = inputTime.value;
  deadline.classList.add("deadline");
  listItem.classList.add("list-item");
  listItem.innerText = inputBox.value;
  listItem.appendChild(deadline);
  listWrapper.appendChild(listItem);

  // make check Btn and put in listWrapper
  const checkBtn = document.createElement("button");
  checkBtn.addEventListener("click", checkList);
  checkBtn.classList.add("complete-check");
  checkBtn.innerText = "✅";
  listWrapper.appendChild(checkBtn);

  //make delete Btn and put in listWrapper
  const deleteBtn = document.createElement("button");
  deleteBtn.addEventListener("click", deleteList);
  deleteBtn.classList.add("delete-btn");
  deleteBtn.innerText = "❌";
  listWrapper.appendChild(deleteBtn);

  return listWrapper;
}
//find location to insert list
function insertList(e) {
  if (inputBox.value != "") {
    const listWrapper = makeListWrapper();
    // input in date order
    const inputDate = document.querySelector(".date-input");
    const dateItem = document.querySelectorAll(".date-item");
    let outerListIndex = 0;
    // if there is no date-item, then makes new outer list and date box (date-item)
    if (dateItem.length == 0) {
      const outerListAndNewDate = `
        <div class="outer-list">
          <div class="date-item">${inputDate.value}</div>
        </div>
      `;
      list.insertAdjacentHTML("beforeend", outerListAndNewDate);
    } else {
      outerListIndex = findDateLocation(inputDate, dateItem);
    }
    const outerListArr = document.querySelectorAll(".outer-list");

    // input in time order
    const inputTime = document.querySelector(".time-input");
    // finally, find out the index where to put todo
    const outerListChildIdx = findTimeLocation(
      outerListArr[outerListIndex],
      inputTime
    );
    outerListArr[outerListIndex].children[outerListChildIdx]
      .insertAdjacentElement("afterend", listWrapper);

    // input box 초기화
    initInput();
  }
}

//delete one list
function deleteList(e) {
  let s = e.target.parentElement;
  let outerList = s.parentElement;
  let t1 = new TimelineMax({
    //remove를 애니메이션 다 끝난 후 호출하기 위한 방법
    onComplete: function () {
      s.remove();
      if (outerList.children.length == 1) {
        outerList.remove();
      }
    },
  });
  t1.to(s, { duration: 0.5, opacity: 0, y: -50, ease: "line" });
}

// decide where to put the date and return the index which points to a place
// to write list-wrapper
function findDateLocation(inputDate, dateItem) {
  const inputDateArr = inputDate.value.split("-");
  const newDate = document.createElement("div");
  newDate.classList.add("date-item");
  newDate.innerText = inputDate.value;
  const outerList = document.createElement("div");
  outerList.classList.add("outer-list");
  outerList.appendChild(newDate);
  for (let i = 0; i < dateItem.length; i++) {
    const date = dateItem[i].innerText.split("-");
    console.log(date);
    if (inputDateArr[0] == date[0]) {
      if (inputDateArr[1] == date[1]) {
        if (inputDateArr[2] == date[2]) {
          console.log("no date insert");
          return i;
        } else {
          if (inputDateArr[2] > date[2] && i == dateItem.length - 1) {
            dateItem[i].parentElement.insertAdjacentElement(
              "afterend",
              outerList
            );
            return i + 1;
          } else if (inputDateArr[2] < date[2]) {
            dateItem[i].parentElement.insertAdjacentElement(
              "beforebegin",
              outerList
            );
            return i;
          }
        }
      } else if (inputDateArr[1] > date[1] && i == dateItem.length - 1) {
        dateItem[i].parentElement.insertAdjacentElement("afterend", outerList);
        return i + 1;
      } else if (inputDateArr[1] < date[1]) {
        dateItem[i].parentElement.insertAdjacentElement(
          "beforebegin",
          outerList
        );
        return i;
      }
    } else if (inputDateArr[0] > date[0] && i == dateItem.length - 1) {
      dateItem[i].parentElement.insertAdjacentElement("afterend", outerList);
      return i + 1;
    } else if (inputDateArr[0] < date[0]) {
      dateItem[i].parentElement.insertAdjacentElement("beforebegin", outerList);
      return i;
    }
  }
}

// decide where to put the time and return the index which points to a place
// to write to-do
function findTimeLocation(outerList, inputTime) {
  let outerListChildIdx = 0;
  const outerListChildLen = outerList.childElementCount;
  const [inputHour, inputMin] = inputTime.value.split(":");
  const onlyDateBoxLen = 1; // wrapper has only date box alone
  if (outerListChildLen == onlyDateBoxLen) return outerListChildIdx;
  let compareHour, compareMin;
  for (outerListChildIdx = 1; outerListChildIdx < outerListChildLen; outerListChildIdx++) {
    const deadline = outerList.children[outerListChildIdx].children[0].children[0].innerText;
    [compareHour, compareMin] = deadline.split(":");
    if (inputHour <= compareHour && inputMin <= compareMin) return outerListChildIdx - 1;
    if (inputHour < compareHour) return outerListChildIdx - 1;
  }
  return outerListChildIdx - 1;
}

function checkList(e) {
  let t = e.target.parentElement;
  let t2 = e.target.previousElementSibling;
  if (t2.classList.contains("complete-list")) {
    t2.classList.remove("complete-list");
    gsap.to(t, { duration: 0.5, opacity: 1, ease: "line" });
  } else {
    t2.classList.add("complete-list");
    gsap.to(t, { duration: 0.5, opacity: 0.5, ease: "line" });
  }
}

document.querySelector(".menu").addEventListener("click", openSideMenu);

//make trash-btn function
function trashCompleted() {
  let listItem = document.querySelectorAll(".list-container .list-item");
  for (let i = 0; i < listItem.length; i++) {
    if (listItem[i].classList.contains("complete-list")) {
      listItem[i].parentElement.remove();
    }
  }
  let outerList = document.querySelectorAll(".outer-list");
  for (let i = 0; i < outerList.length; i++) {
    if (outerList[i].childElementCount == 1) {
      outerList[i].remove();
    }
  }
}
document.querySelector(".trash-svg").addEventListener("click", announceTrash);
//make announcement when user clicks trash-btn
function announceTrash() {
  trashCompleted();
}

// change trash svg color when mouseover, mouseout
document
  .querySelector(".trash-svg")
  .addEventListener("mouseover", changeTrashColorIn);
function changeTrashColorIn() {
  let trashHover = document.querySelectorAll(".trash-hover");
  for (let i = 0; i < trashHover.length; i++) {
    trashHover[i].style.fill = "#7c9499";
  }
}

document
  .querySelector(".trash-svg")
  .addEventListener("mouseout", changeTrashColorOut);
function changeTrashColorOut() {
  let trashHover = document.querySelectorAll(".trash-hover");
  for (let i = 0; i < trashHover.length; i++) {
    trashHover[i].style.fill = "#CCD0D2";
  }
}

// make clock fucntion
const clock = document.querySelector(".current-time");

function getClock() {
  const date = new Date();
  let year = date.getFullYear(); // 년도
  let month = date.getMonth() + 1; // 월
  let day = date.getDate(); // 날짜
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  const showTime = `${month}월 ${day}일 ${hours}:${minutes}`;
  clock.innerText = showTime;
  let monthZero = String(month).padStart(2, 0);
  let dayZero = String(day).padStart(2, 0);
  const inputDate = document.querySelector(".date-input");
  inputDate.value = `${year}-${monthZero}-${dayZero}`;
  inputDate.min = `${year}-${monthZero}-${dayZero}`;
}

var dropdownTimeout;

function showDropdown() {
  clearTimeout(dropdownTimeout);
  var dropdownContent = document.querySelector('.dropdown-content');
  dropdownContent.style.display = 'block';
}

function hideDropdown() {
  dropdownTimeout = setTimeout(function () {
    var dropdownContent = document.querySelector('.dropdown-content');
    dropdownContent.style.display = 'none';
  }, 200);
}

function logout() {
  $.ajax({
    type: "POST",
    url: "http://localhost:8080/user/logout",
    headers: { 'Authorization': accessToken },
    xhrFields: { // CORS 문제 우회해서 헤더 넘겨주기 --> Set-Cookie 헤더 넘겨 줄려면 필요!!!!
      withCredentials: true
    },
    success: function (data, textStatus, request) {
      localStorage.removeItem('Authorization');
      alert("로그인 페이지로 이동합니다");
      location.href = "/app.html";
    },
    error: function () {
      alert("Error!");
    }
  })
}

function addTodo(todo) {
  var todo = {

  }
  $.ajax({
    type: "POST",
    url: "http://localhost:8080/todo/add",
    data: JSON.stringify(todo),
    success: function (data, textStatus, request) {

    },
    error: function (request, status, error) {

    }
  })
}

function memberInfo() {
  location.href = "/userInfo/user-info.html";
}

//init addBtn
const addBtn = document.querySelector(".addButton");
addBtn.addEventListener("click", insertList);

initInput();
getClock();
setInterval(getClock, 60000);