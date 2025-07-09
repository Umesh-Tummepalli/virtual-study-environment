var time=document.querySelector("#time");
var dragElement=document.querySelector(".Drag");
var dragel=document.querySelector(".Dragel");
var todo=document.querySelector(".todo");
var vid=document.querySelector(".bg-video");
var leftArr=document.querySelector(".leftarr");
var rightArr=document.querySelector(".rightarr");

var mute=document.querySelector(".mute");
var PlayPause=document.querySelector("#playPause");
var i=0;

// let arr=[]
function padTime(num) {
  return num>9?num:`0${num}`
}
document.addEventListener("DOMContentLoaded", () => {
  setInterval(() => {
    let date = new Date();
    let hours = date.getHours();
    let minutes = date.getMinutes();
    let seconds = date.getSeconds();
    time.textContent = `${padTime(hours)}:${padTime(minutes)}:${padTime(seconds)}`;
  },900)
})
rightArr.addEventListener("click",()=>{
  if(i<arr.length-1){
    vid.src=arr[++i];
  }
  else{
    i=0;
    vid.src=arr[i];
  }
})
leftArr.addEventListener("click",()=>{
  if(i>0){
    vid.src=arr[--i];
  }
  else{
    i=arr.length-1;
    vid.src=arr[i];
  }
})

audio.addEventListener('ended', function() {
  audio.play();
});
mute.addEventListener('click', function() {
  if(audio.paused){
    audio.play();
    mute.innerHTML=`<i class="ri-volume-up-line"></i>`
  }
  else{
    audio.pause();
    audio.volume=.3;
    mute.innerHTML=`<i class="ri-volume-mute-fill"></i>`;
  }
});


var transleft;
function drag(event) {
  gsap.to(dragel,{
    left: `${event.clientX}px`,
    top: `${event.clientY}px`,
    x:-transleft,
    duration:0.3 ,
    ease:"power4.out",
  })
}
dragElement.addEventListener('mousedown', (e) => {
    dimensions=dragElement.getBoundingClientRect();
    transleft=e.clientX-dimensions.left;
    document.addEventListener('mousemove', drag);
});
document.addEventListener('mouseup', () => {
    document.removeEventListener('mousemove', drag);
});

var openmenu=document.querySelector(".vidMenu");
var openmenuBtn=document.querySelector(".menuBtn");
var closemenuBtn=document.querySelector(".closeBtn");
openmenuBtn.addEventListener('click',()=>{
  gsap.to(openmenu,{
    x: "0",
    duration:.5,
    ease:"power2.out",
  })
})
closemenuBtn.addEventListener('click',()=>{
  gsap.to(openmenu,{
    x: "100%",
    duration:.5,
    ease:"power4.out",
  })
})
var videoAppend="";
arr.forEach((el)=>{
  videoAppend+=`<video src="${el}" class="video"></video>\n`;
})
var vids=document.querySelector('.videos');

vids.innerHTML=videoAppend;
var video=document.querySelectorAll('.video');

  video.forEach((element)=>{
    element.addEventListener('click',(event)=>{
      vid.src = event.target.src;
  })
  element.addEventListener("mouseenter",(event)=>{
    event.currentTarget.play();
  })
  element.addEventListener("mouseleave",(event)=>{
    event.currentTarget.pause();
  }) 
})
var addtaskBtn = document.querySelector(".addtaskBtn");

const stopwatch = document.getElementById('stopwatch');
const playPauseButton = document.getElementById('playPause');
const resetButton = document.getElementById('reset');
const alarm = new Audio('alarm.wav'); // Add an audio element for alarm
let elapsedTime = 0;
let pomodoroInterval = 25 * 60; // 25 minutes
let breakInterval = 5 * 60; // 5 minutes
let isPomodoroRunning = true;
let running = false;
let intervalId;

// Function to update the stopwatch display
function updateDisplay() {
    const hours = Math.floor(elapsedTime / 3600);
    const minutes = Math.floor((elapsedTime % 3600) / 60);
    const seconds = elapsedTime % 60;

    const formattedTime = `
        ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    stopwatch.textContent = formattedTime;
}

// Function to start the Pomodoro timer
function startPomodoro() {
    intervalId = setInterval(() => {
        elapsedTime++;
        updateDisplay();
        checkInterval();
    }, 1000);
}

// Function to stop the Pomodoro timer
function stopPomodoro() {
    clearInterval(intervalId);
}

// Function to check the interval and trigger alarm or switch between Pomodoro and break
function checkInterval() {
    if (isPomodoroRunning && elapsedTime >= pomodoroInterval) {
        // Trigger alarm and switch to break
        alarm.play();
        elapsedTime = 0;
        isPomodoroRunning = false;
        stopwatch.textContent = 'Break Time!';
        setTimeout(() => {
            stopwatch.textContent = '00:00:00';
        }, 2000);
    } else if (!isPomodoroRunning && elapsedTime >= breakInterval) {
        // Trigger alarm and switch to Pomodoro
        alarm.play();
        elapsedTime = 0;
        isPomodoroRunning = true;
        stopwatch.textContent = 'Work Time!';
        setTimeout(() => {
            stopwatch.textContent = '00:00:00';
        }, 2000);
    }
}

// Play/Pause button click handler
playPauseButton.addEventListener('click', () => {
    if (running) {
        stopPomodoro();
        playPauseButton.innerHTML = '<i class="ri-play-fill"></i>';
    } else {
        startPomodoro();
        playPauseButton.innerHTML = '<i class="ri-pause-fill"></i>';
    }
    running = !running;
});

// Reset button click handler
resetButton.addEventListener('click', () => {
    stopPomodoro();
    elapsedTime = 0;
    isPomodoroRunning = true;
    updateDisplay();
    playPauseButton.innerHTML = '<i class="ri-play-fill"></i>';
    running = false;
});

// connecting to the api

const appUr = 'http://localhost:8080/api/todos';

// Fetch and display todos
async function fetchTodos() {
    try {
        const response = await fetch(appUr);
        // console.log(response);
        const todos = await response.json();
        console.log(todos);
        displayTodos(todos);
    } catch (error) {
        console.error('Error fetching todos:', error);
    }
}

// Display todos in the list
function displayTodos(todos) {
    const todoList = document.querySelector('.tasks');
    todoList.innerHTML = ''; // Clear existing todos
    todos.forEach(todo => {
        const todoItem = document.createElement('div');
        todoItem.classList.add('todotext');
        todoItem.innerHTML = `
            <p>${todo.title}</p>
            <button class="delBtn" onclick="deleteTodo(${todo.id})"><i class="ri-checkbox-circle-fill"></i></button>
        `;
        todoList.appendChild(todoItem);
    });
}

function generateRandomId(length) {
  const chars = '0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}


let taskInput = document.querySelector('.taskInput');
document.addEventListener('keydown', (e) => {
  if (e.key === '/' && document.activeElement !== taskInput) {
    taskInput.focus();
    e.preventDefault();
  }
})
// Add a new todo
taskInput.addEventListener('keydown', async (e) => {
    if(e.key == 'Enter') {
      let currid=generateRandomId(4);
    const title = document.querySelector('.taskInput').value;
    const newTodo ={ title,id:currid, completed: false , description:""};
    try {
        await fetch(appUr, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newTodo)
        });
        fetchTodos();  // Refresh the list
        document.querySelector('.taskInput').value = '';
    } catch (error) {
        console.error('Error adding todo:', error);
    }
  }
});

// Delete a todo
async function deleteTodo(id) {
    try {
        await fetch(`${appUr}/${id}`, { method: 'DELETE' });
        fetchTodos();  // Refresh the list
    } catch (error) {
        console.error('Error deleting todo:', error);
    }
}

// Toggle completion status

// Initial fetch of todos
fetchTodos();
