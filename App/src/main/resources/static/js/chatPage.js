
let timerId = null;
let deadline = null;

document.addEventListener('DOMContentLoaded', function() {
    connect();
    connectWerewolves();
    connectGameInfo();
});

function startTimer() {
    deadline = new Date().getTime() + 120 * 1000;
    timerId = setInterval(countdownTimer, 1000);
}

function countdownTimer() {
    const diff = deadline - new Date();
    if (diff <= 0) {
        clearInterval(timerId);
    }
    const minutes = diff > 0 ? Math.floor(diff / 1000 / 60) % 60 : 0;
    const seconds = diff > 0 ? Math.floor(diff / 1000) % 60 : 0;
    document.querySelector('.timer_minutes').textContent = minutes < 10 ? '0' + minutes : minutes;
    document.querySelector('.timer_seconds').textContent = seconds < 10 ? '0' + seconds : seconds;
}

function changeBg(p, dayNumber) {
    if (p=='/img/backgrounds/picture6.jpg') {
        colP1 = document.getElementsByClassName("player1Panel");
        for (let i = 0; i < colP1.length; i++) {
            colP1[i].hidden = true;
        }
        colP2 = document.getElementsByClassName("player2Panel");
        for (let i = 0; i < colP2.length; i++) {
            colP2[i].hidden = false;
        }
    } else {
        if (dayNumber > 1) {
            colP1 = document.getElementsByClassName("player1Panel");
            for (let i = 0; i < colP1.length; i++) {
                colP1[i].hidden = false;
            }
        }
        colP2 = document.getElementsByClassName("player2Panel");
        for (let i = 0; i < colP2.length; i++) {
            colP2[i].hidden = true;
        }
    }
    document.body.style.backgroundImage = 'url(' + p + ')';
}

var webSocket = null;
var webSocketGameInfo = null;

function onMessage(event) {
    document.getElementById('messages').innerHTML
        += '<br />' + event.data;
}
function onOpen(event) {
    document.getElementById.innerHTML
}
function onError(event) {
    console.log(event);
    webSocket = null;
}
function onClose(event) {
    webSocket = null;
}
function connect() {
    var Group_no = document.getElementById('Group_no').value;
    var nickname = document.getElementById('login').value;
    if (url == '' || nickname == '') {
        return;
    }
    if(webSocket!=null){
        return;
    }
    var url = 'ws://localhost:8081/groupChat/' + Group_no + '/' + nickname;
    webSocket = new WebSocket(url);
    webSocket.onerror = function(event) {
        onError(event)
    };
    webSocket.onopen = function(event) {
        onOpen(event)
    };
    webSocket.onmessage = function(event) {
        onMessage(event)
    };
    webSocket.onclose = function(event) {
        onClose(event)
    };
}
function start() {
    var text = document.getElementById('content').value;
    if(text== ''){
        alert ("Чтобы отправить сообщение, нужно его написать");
        return;
    }
    if(webSocket==null){
        return;
    }
    webSocket.send(text);
    document.getElementById('content').value = '';
}

var webSocketWerewolves = null;

function connectWerewolves() {
    if (document.getElementById('Group_noWerewolves') == null || document.getElementById('loginWerewolves') == null) {
        return;
    }
    var Group_no = document.getElementById('Group_noWerewolves').value;
    var nickname = document.getElementById('loginWerewolves').value;
    if(webSocketWerewolves!=null){
        return;
    }
    var url = 'ws://localhost:8081/groupChat/' + Group_no + '/' + nickname;
    webSocketWerewolves = new WebSocket(url);
    webSocketWerewolves.onerror = function(event) {
        onError(event)
    };
    webSocketWerewolves.onopen = function(event) {
        onOpenWerewolves(event)
    };
    webSocketWerewolves.onmessage = function(event) {
        onMessageWerewolves(event)
    };
    webSocketWerewolves.onclose = function(event) {
        onCloseWerewolves(event)
    };
}
function startWerewolves() {
    var text = document.getElementById('contentWerewolves').value;
    if(text== ''){
        alert ("Чтобы отправить сообщение, нужно его написать");
        return;
    }
    if(webSocketWerewolves==null){
        return;
    }
    webSocketWerewolves.send(text);
    document.getElementById('contentWerewolves').value = '';
}
function onOpenWerewolves(event) {
    document.getElementById.innerHTML
}
function onMessageWerewolves(event) {
    document.getElementById('messagesWerewolves').innerHTML
        += '<br />' + event.data;
}
function onCloseWerewolves(event) {
    webSocketWerewolves = null;
}

function connectGameInfo() {
    var url = 'ws://localhost:8081/gameinfo';
    webSocketGameInfo = new WebSocket(url);
    webSocketGameInfo.onerror = function(event) {
        onError(event)
    };
    webSocketGameInfo.onopen = function(event) {
       console.log("connect to gameinfo");
    };
    webSocketGameInfo.onmessage = function(event) {
        console.log(event.data);
        if (event.data.startsWith("DAY")) {
            changeBg('/img/backgrounds/picture5.jpg', event.data.replace("DAY ", ""));
            window.location.href = '/chatPage';
        } else if (event.data.startsWith("NIGHT")) {
            changeBg('/img/backgrounds/picture6.jpg', event.data.replace("NIGHT ", ""));
            window.location.href = '/chatPage';
        } else if (event.data == "ENDGAME") {
            window.location.href = '/endGame';}
        else if (event.data == "FULLMOON") {
                window.location.href = '/endGameFullMoon';
        } else if (event.data == "PLAYERDEAD") {
            window.location.href = '/chatPage';
        }

    };
    webSocketGameInfo.onclose = function(event) {
        webSocketGameInfo = null;
    };
}

function execute(login) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', 'chatPage/execute', true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        console.log("executed");
        let colP1 = document.getElementsByClassName("player1Panel");
        for (let i = 0; i < colP1.length; i++) {
            colP1[i].hidden = true;
        }
    };
    xhr.send('login='+login);
}

function kill(login) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', 'chatPage/kill', true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        console.log("killed");
        let colP2 = document.getElementsByClassName("player2Panel");
        for (let i = 0; i < colP2.length; i++) {
            colP2[i].hidden = true;
        }
    };
    xhr.send('login='+login);
}

function bite(login) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', 'chatPage/bite', true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        console.log("bitten");
        let colP2= document.getElementsByClassName("player2Panel");
        for (let i = 0; i < colP2.length; i++) {
            colP2[i].hidden = true;
        }
    };
    xhr.send('login='+login);
}