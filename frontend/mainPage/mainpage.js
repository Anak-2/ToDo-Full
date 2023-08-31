function dateFormatting(source, delimiter = '-') {
  let year = source.getFullYear(); // 년도
  let month = source.getMonth() + 1; // 월
  let day = source.getDate(); // 날짜
  return [year, month, day].join(delimiter);
}

function timeFormatting(source, delimiter = ':') {
  const hours = String(source.getHours()).padStart(2, "0");
  const minutes = String(source.getMinutes()).padStart(2, "0");
  return [hours, minutes].join(delimiter);
}

let inputBox = document.querySelector(".input-container .inputBox");
let searchBox = document.querySelector(".searchBox");
let list = document.querySelector(".list");
// import { accessToken } from "../userInfo/chekUser.js"; -> cannot use import statement outside a module 문제 발생 --> 받는 쪽도 모듈화 필요
let accessToken = localStorage.getItem("accessToken");


document.querySelector(".searchButton").addEventListener("click", function (e) {
  if (searchBox.value == "") return;
  searchTodo(searchBox.value);
})


// search todo within ceratin schedule by search input
function searchTodo(searchInput) {

  const scheduleId = sessionStorage.getItem('scheduleId');
  if (scheduleId == undefined || scheduleId == null) {
    alert("please select or make schedule first!");
    return;
  }


  $.ajax({
    method: 'GET',
    url: `http://localhost:8080/todo/search?scheduleId=${scheduleId}&searchInput=${searchInput}`,
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    headers: { 'Authorization': accessToken },
    success: function (data) {
      list.innerHTML = "";
      data.forEach(todo => {
        let date = todo['finishDate'];
        insertTodo(todo['todoId'], date, todo['title'], todo['content'], todo['finished']);
      })
    },
    error: function (jqXHR) {
      alert(jqXHR.responseText);
    }
  })
}

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
    <span onClick="getTodoListByScheduleId(this)">${title}</span>
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
    url: `http://localhost:8080/schedule/add`,
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
      data.forEach(element => {
        insertSchedule(element['id'], element['title'], element['isPublic']);
      });
    },
    error: function () {
      console.log("getScheduleList 실패");
    }
  })
}


//  ToDo: public , private 조건 주기
function lockEvent(lockElement) {
  const lockWrapper = lockElement.parentElement;
  const scheduleId = lockWrapper.children[0].innerText;
  const title = lockWrapper.children[1].innerText;
  var locked = lockElement.classList.contains('locked') ? true : false;
  const requestData = {
    "id": scheduleId,
    "title": title,
    "isPublic": locked
  }
  $.ajax({
    method: "PATCH",
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
      } else {
        lockElement.classList.remove('locked');
        lockElement.classList.add('unlocked');
        lockElement.src = "/img/unlock.png";
      }
    },
    error: function () {
      alert("schedule update 실패");
    }
  })
}

document.querySelector(".menu").addEventListener("click", openSideMenu);

// ******** List Func ********

//add key event listencer
inputBox.addEventListener("keyup", function (e) {
  if (e.keyCode === 13) {
    insertInputList(e);
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

function getTodoListByScheduleId(e) {
  const scheduleId = e.parentElement.children[0].innerText;
  const scheduleTitle = e.innerText;
  sessionStorage.setItem("scheduleId", scheduleId);
  sessionStorage.setItem("scheduleTitle", scheduleTitle);
  getTodoList(scheduleId, scheduleTitle);
}

// getTodoList when click schedule
function getTodoList(scheduleId, scheduleTitle) {
  if (!valueCheck(scheduleId)) return;
  $.ajax({
    method: "GET",
    url: `http://localhost:8080/todo/todos?scheduleId=${scheduleId}`,
    headers: { "Content-Type": "application/json", 'Authorization': accessToken },
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    success: function (data) {
      document.querySelector(".schedule-title").innerText = scheduleTitle;
      // first empty list container and then insert todos
      list.innerHTML = "";
      data.forEach(todo => {
        let date = todo['finishDate'];
        insertTodo(todo['todoId'], date, todo['title'], todo['content'], todo['finished']);
      })
    },
    error: function () {
      alert("failed to get todo list");
    }
  })
}

function makeListWrapper(todoId, timeValue, title, finished) {

  let listWrapper = document.createElement("div");
  listWrapper.classList.add("list-wrapper");

  let todoIdDiv = `<div class="todo-id">${todoId}</div>`;
  listWrapper.insertAdjacentHTML("afterbegin", todoIdDiv);

  const listItem = document.createElement("li");
  if (finished) {
    listItem.classList.add("complete-list");
    listWrapper.style.opacity = 0.5;
  }
  listItem.classList.add("list-item");
  listItem.addEventListener("click", (event) => handleTodoContent(event));
  listItem.innerText = title;

  const deadline = document.createElement("span");
  deadline.innerText = timeValue;
  deadline.classList.add("deadline");

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

function makeTodoContentBox(content) {
  let todoContentBox = document.createElement("div");
  todoContentBox.classList.add("todo-content-box");
  todoContentBox.classList.add("todo-content-hidden");
  let todoContent = `<input type="textarea" class="todo-content" value="${content}">`
  todoContentBox.insertAdjacentHTML("afterbegin", todoContent);
  return todoContentBox;
}

//find location to insert list
function insertTodo(todoId, date, title, content, finished) {
  const dateItem = document.querySelectorAll(".date-item");
  date = new Date(date);
  dateValue = dateFormatting(date, '-');
  timeValue = timeFormatting(date, ':');
  const listWrapper = makeListWrapper(todoId, timeValue, title, finished);
  const todoContentBox = makeTodoContentBox("ContentBox에 넣을 값");

  let outerListIndex = 0;

  outerListIndex = findDateLocation(dateValue, dateItem);

  const outerListArr = document.querySelectorAll(".outer-list");

  // finally, find out the index where to put todo
  const outerListChildIdx = findTimeLocation(
    outerListArr[outerListIndex],
    timeValue
  );

  // get outerlist's children and calculate the list wrapper where
  // todo needs should be inserted
  const outerListChild = outerListArr[outerListIndex].children;
  const listWrapperIdx = outerListChildIdx * 2;
  outerListChild[listWrapperIdx]
    .insertAdjacentElement("afterend", listWrapper)
    .insertAdjacentElement("afterend", todoContentBox);
}

``
// insert todo item into list using input value
function insertInputList(e) {
  if (inputBox.value != "") {
    const inputDate = document.querySelector(".date-input");
    const inputTime = document.querySelector(".time-input");
    const finishDate = `${inputDate.value}T${inputTime.value}`;
    const scheduleId = sessionStorage.getItem("scheduleId");
    const isFinished = false;
    const content = null;
    const title = inputBox.value;
    const todoRequestDto = {
      "title": title,
      "content": content,
      "isFinished": isFinished,
      "finishDate": finishDate,
      "scheduleId": scheduleId
    }
    if (scheduleId === undefined || scheduleId === null || scheduleId === "") {
      alert("Select or Make Your Schedule First");
      return;
    }
    $.ajax({
      method: "POST",
      url: `http://localhost:8080/todo/add`,
      headers: { "Content-Type": "application/json", 'Authorization': accessToken },
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      data: JSON.stringify(todoRequestDto),
      success: function (data) {
        insertTodo(data, finishDate, title, content, false);
      },
      error: function () {
        alert("failed to insert todo");
      }
    })
    // input box 초기화
    initInput();
  }
}

//delete one list
function deleteList(e) {
  let listWrapper = e.target.parentElement;
  let todoContentBox = listWrapper.nextElementSibling;
  let todoId = listWrapper.children[0].innerText;
  let outerList = listWrapper.parentElement;
  $.ajax({
    method: "DELETE",
    url: `http://localhost:8080/todo/${todoId}`,
    headers: { "Content-Type": "application/json", 'Authorization': accessToken },
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    success: function () {
      let t1 = new TimelineMax({
        //remove를 애니메이션 다 끝난 후 호출하기 위한 방법
        onComplete: function () {
          listWrapper.remove();
          todoContentBox.remove();
          if (outerList.children.length == 1) {
            outerList.remove();
          }
        },
      });
      t1.to(listWrapper, { duration: 0.3, opacity: 0, y: -50, ease: "line" });

    },
    error: function (jqXHR) {
      console.log("code: " + jqXHR.status + " response: " + jqXHR.responseText);
      console.log(jqXHR.responseText);
    }
  })
}

// *** Todo: change to binary search algorithm
// decide where to put the date and return the index which points to a place
// to write list-wrapper
function findDateLocation(dateValue, dateItem) {

  // if date item is empty, init
  if (dateItem.length == 0) {
    const outerListAndNewDate = `
        <div class="outer-list">
          <div class="date-item">${dateValue}</div>
        </div>
      `;
    list.insertAdjacentHTML("beforeend", outerListAndNewDate);
    return 0;
  }

  // Variable that checks whether inputDate should be placed before an existing date 
  let isBefore = false;

  const inputDateArr = dateValue.split("-").map((e) => parseInt(e));
  let dateIndex = 0;

  for (dateIndex = 0; dateIndex < dateItem.length; dateIndex++) {

    const date = dateItem[dateIndex].innerText.split("-")
      .map((e) => parseInt(e));

    if (inputDateArr[0] == date[0]) { // same year
      if (inputDateArr[1] == date[1]) { // same month
        if (inputDateArr[2] == date[2]) { // same day
          return dateIndex;
        }
        // compare day
        else if (inputDateArr[2] < date[2]) {
          isBefore = true;
          break;
        }
      }
      //compare month
      else if (inputDateArr[1] < date[1]) {
        isBefore = true;
        break;
      }
    }
    // compare year
    else if (inputDateArr[0] < date[0]) {
      isBefore = true;
      break;
    }
  }

  // make date item
  const newDate = document.createElement("div");
  newDate.classList.add("date-item");
  newDate.innerText = dateValue;
  const outerList = document.createElement("div");
  outerList.classList.add("outer-list");
  outerList.appendChild(newDate);

  if (isBefore) {
    dateItem[dateIndex].parentElement.insertAdjacentElement("beforebegin", outerList);
  } else {
    dateItem[dateIndex - 1].parentElement.insertAdjacentElement("afterend", outerList);
  }

  return dateIndex;
}

// *** Todo: change to binary search algorithm
// decide where to put the time and return the index which points to a place
// to write to-do
function findTimeLocation(outerList, timeValue) {

  const NO_OUTER_LIST = 0; // wrapper has only date box alone
  let outerListChildIdx = 0; // Index where to put the todo from outer list

  let deadlineElements = outerList.querySelectorAll(".deadline");
  if (deadlineElements.length == 0) return outerListChildIdx;

  deadlineElements = Array.from(deadlineElements);

  const [inputHour, inputMin] = timeValue.split(":").map((e) => parseInt(e));

  // compare time with input 'timeValue'
  let compareHour, compareMin;

  // console.log("inputHour: " + inputHour + " inputMin: ") + inputMin;
  deadlineElements.forEach((deadlineElement) => {

    // Get Deadline
    deadline = deadlineElement.innerText;

    // split deadline to HH, MM
    [compareHour, compareMin] = deadline.split(":").map((e) => parseInt(e));
    // console.log("compareHour: " + compareHour + " compareMin: " + compareMin);

    if (inputHour <= compareHour) {
      if (inputHour < compareHour || (inputHour == compareHour && inputMin < compareMin)) return outerListChildIdx;
    }

    outerListChildIdx++;
  });

  return outerListChildIdx;
}

function checkList(e) {
  let t = e.target.parentElement;
  let t2 = e.target.previousElementSibling;
  let isFinished = null;
  const todoId = e.target.parentElement.children[0].innerText;
  if (t2.classList.contains("complete-list")) {
    t2.classList.remove("complete-list");
    isFinished = false;
    gsap.to(t, { duration: 0.5, opacity: 1, ease: "line" });
  } else {
    t2.classList.add("complete-list");
    isFinished = true;
    gsap.to(t, { duration: 0.5, opacity: 0.5, ease: "line" });
  }
  $.ajax({
    method: "PATCH",
    url: `http://localhost:8080/todo/${todoId}/${isFinished}`,
    headers: { 'Content-Type': 'application/json', 'Authorization': accessToken, 'Access-Control-Allow-Origin': true },
    crossDomain: true,
    xhrFields: {
      withCredentials: true
    },
    success: function (data, textStatus, request) {

    },
    error: function (jqXHR) {
      alert(jqXHR.textStatus);
    }
  })
}

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

// show or hide todo-content-box
function handleTodoContent(e) {
  // let todoContent = e.parentElement.nextElementSibling;
  let todoContent = e.target.parentElement.nextElementSibling;

  if (todoContent.classList.contains("todo-content-hidden")) {
    let t1 = gsap.timeline({
      onStart: function () {
        todoContent.classList.remove("todo-content-hidden");
      },
    });
    t1.to(todoContent, { duration: 0, opacity: 0, height: 0 });
    t1.to(todoContent, { duration: 0.2, opacity: 1, height: "auto", ease: "line" });
  }
  else {
    let t1 = gsap.timeline({
      onComplete: function () {
        todoContent.classList.add("todo-content-hidden");
      },
    });
    t1.to(todoContent, { duration: 0.2, opacity: 0, height: 0, ease: "line" });
  }
}

document.querySelector(".trash-svg").addEventListener("click", trashCompleted);
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
      sessionStorage.removeItem('scheduleTitle');
      sessionStorage.removeItem('scheduleId');
      alert("로그인 페이지로 이동합니다");
      location.href = "/app.html";
    },
    error: function () {
      alert("Error!");
    }
  })
}


function memberInfo() {
  location.href = "/userInfo/user-info.html";
}

//init addBtn
const addBtn = document.querySelector(".addButton");
addBtn.addEventListener("click", insertInputList);

initInput();
getClock();
setInterval(getClock, 60000);

// getScheduleList();
// getTodoList(sessionStorage.getItem("scheduleId"), sessionStorage.getItem("scheduleTitle"));

function valueCheck(value) {
  if (value == undefined || value == null || value == "") return false;
  return true;
}