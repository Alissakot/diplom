package pac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ServerEndpoint("/groupChat/{Group_no}/{username}")
public class GroupChatController {

    private static final ConcurrentHashMap<String, List<Session>> groupMemberInfoMap = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(@PathParam("Group_no") String Group_no,
                          @PathParam("username") String username, String message) {

        List<Session> sessionList = groupMemberInfoMap.get(Group_no);
        sessionList.forEach(item -> {
            try {
                String text = username + ": " + message;
                item.getBasicRemote().sendText(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("Group_no") String Group_no) {
        List<Session> sessionList = groupMemberInfoMap.get(Group_no);
        if (sessionList == null) {
            sessionList = new ArrayList<>();
            groupMemberInfoMap.put(Group_no, sessionList);
        }
        sessionList.add(session);
        log.info("Соединение установлено");
        log.info("номер группы: {}, номер группы: {}", Group_no, sessionList.size());
    }

    @OnClose
    public void onClose(Session session, @PathParam("Group_no") String Group_no) {
        List<Session> sessionList = groupMemberInfoMap.get(Group_no);
        sessionList.remove(session);
        log.info("соединение закрыто");
        log.info("номер группы: {}, номер группы: {}", Group_no, sessionList.size());
    }

    @OnError
    public void OnError(Throwable error) {
        log.info("Ошибка соединения: {}", error.getMessage());
    }
}
