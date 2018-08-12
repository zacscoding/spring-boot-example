var stompClient = null;

function connectAndSubscribe(moduleName, action) {
  if (!isConnected()) {
    console.log('try to connect..');
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      var subscribe = stompClient.subscribe('/result/action/' + moduleName + '/'
          + action, function (result) {
        console.log('result : ', result);
        if (result.body === 'COMPLETE') {
          stompClient.unsubscribe(subscribe.id);
        } else {
          updateView(moduleName, action, result);
        }
      });
      stompClient.send('/app/action/' + moduleName + '/' + action, {},
          JSON.stringify({}));
    });
  } else {
    console.log('already connected');
    var subscribe = stompClient.subscribe('/result/action/' + moduleName + '/'
        + action, function (result) {
      console.log('result : ', result);
      if (result.body === 'COMPLETE') {
        stompClient.unsubscribe(subscribe.id);
      } else {
        updateView(moduleName, action, result);
      }
    });
    stompClient.send('/app/action/' + moduleName + '/' + action, {},
        JSON.stringify({}));
  }
}

function isConnected() {
  console.log('check stompClient..', stompClient);
  if (typeof stompClient === 'undefined' || !stompClient) {
    return false;
  }

  return stompClient.connected;
}

function updateView(moduleName, actionType, result) {
  var bodyObj = $('#action-results');
  var titleId = '#' + moduleName + '-title';
  var contentId = '#' + moduleName + '-content';

  if ($(titleId).length == 0) {
    var html = '<div><h4 id="' + titleId.substr(1)
        + '"></h4><textarea readonly="readonly" rows="15" cols="80" id="'
        + contentId.substr(1) + '"></textarea></div>';
    bodyObj.append(html);
  }

  $(titleId).text(moduleName + ' :: ' + actionType);
  var content = $(contentId).val() + '\n' + result.body;
  $(contentId).val(content);
  $(contentId).scrollTop($(contentId)[0].scrollHeight);
}

$(function () {
  $("#btnAction").click(function () {
    var moduleName = $('#moduleName').val();
    var action = $('#actionType').val();
    connectAndSubscribe(moduleName, action);
  });
});