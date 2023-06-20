let inputBox = document.querySelector(".input-container .inputBox");
let searchBox = document.querySelector(".searchBox");
let list = document.querySelector(".list");
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

//add key event listencer
inputBox.addEventListener("keyup", function (e) {
  if (e.keyCode === 13) {
    addList(e);
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

//when inputBox lost focus (it works but it couldn't handle hiding input-info)
// inputBox.addEventListener("blur", function (e) {
//   inputBox.parentElement.style.boxShadow = "none";
// });

//when click outside the list-input, hide box-shadow and input-info
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

//add list
function addList(e) {
  if (inputBox.value != "") {
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

    // input in date order
    const inputDate = document.querySelector(".date-input");
    const dateItem = document.querySelectorAll(".date-item");
    let outerListIndex = 0;
    if (dateItem.length == 0) {
      const outerList = document.createElement("div");
      outerList.classList.add("outer-list");
      const newDate = document.createElement("div");
      newDate.classList.add("date-item");
      newDate.innerText = inputDate.value;
      outerList.appendChild(newDate);
      outerList.appendChild(listWrapper);
      list.appendChild(outerList);
    } else {
      outerListIndex = findDateLocation(inputDate, dateItem);
    }
    const outerListArr = document.querySelectorAll(".outer-list");

    // input in time order
    // finally, find index where to put todo
    const listWrapperIndex = findTimeLocation(
      outerListArr[outerListIndex],
      inputTime
    );
    outerListArr[outerListIndex].children[
      listWrapperIndex
    ].insertAdjacentElement("afterend", listWrapper);
    initInput();
  }
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
    if (inputDateArr[0] == date[0]) {
      if (inputDateArr[1] == date[1]) {
        if (inputDateArr[2] == date[2]) {
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
function findTimeLocation(wrapperArr, inputTime) {
  const wrapperArrLen = wrapperArr.children.length;
  const inputTimeArr = inputTime.value.split(":");
  if (wrapperArrLen == 1) {
    return 0;
  }
  for (let i = 1; i < wrapperArrLen; i++) {
    const time =
      wrapperArr.children[i].children[0].firstElementChild.innerText.split(":");
    if (time[0] == inputTimeArr[0]) {
      if (time[1] == inputTimeArr[1] && i == wrapperArrLen - 1) {
        return i;
      } else if (time[1] < inputTimeArr[1] && i == wrapperArrLen - 1) {
        return i;
      } else if (time[1] > inputTimeArr[1]) {
        return i - 1;
      }
    } else if (time[0] < inputTimeArr[0] && i == wrapperArrLen - 1) {
      return i + 1;
    } else if (time[0] > inputTimeArr[0]) {
      return i - 1;
    }
  }
}

function checkList(e) {
  let t = e.target.parentElement;
  let t2 = e.target.previousElementSibling;
  if (t2.classList.contains("complete-list")) {
    t2.classList.remove("complete-list");
    gsap.to(t, { duration: 1, opacity: 1, ease: "line" });
  } else {
    t2.classList.add("complete-list");
    gsap.to(t, { duration: 1, opacity: 0.5, ease: "line" });
  }
}

//init deleteBtn and checkBtn
const deleteBtn = document.querySelectorAll(".delete-btn");
for (let i = 0; i < deleteBtn.length; i++) {
  deleteBtn[i].addEventListener("click", deleteList);
}
const addBtn = document.querySelector(".addButton");
addBtn.addEventListener("click", addList);
const checkBtn = document.querySelectorAll(".complete-check");
for (let i = 0; i < checkBtn.length; i++) {
  checkBtn[i].addEventListener("click", checkList);
}

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
document.querySelector(".menu").addEventListener("click", openSideMenu);

//make trash-btn function
function trashCompleted() {
  let listItem = document.querySelectorAll(".list-container .list-item");
  for (let i = 0; i < listItem.length; i++) {
    if (listItem[i].classList.contains("complete-list")) {
      listItem[i].parentElement.remove();
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

initInput();
getClock();
setInterval(getClock, 60000);
