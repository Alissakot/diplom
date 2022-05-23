package pac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@ServerEndpoint("/gameinfo")
public class GameInfoController {
    private static final List<Session> sessions = new ArrayList<>();
    private static final int playersNumber = 8;

    public static boolean isDay = false;
    public static int dayNumber = 0;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void OnError(Throwable error) {
        log.info("Ошибка соединения: {}", error.getMessage());
    }

    public static void EndGame(String endingType) {
        sessions.forEach(item -> {
            try {
                if (endingType.equals("common")) {
                    item.getBasicRemote().sendText("ENDGAME");
                } else {
                    item.getBasicRemote().sendText("FULLMOON");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void PlayerDead() {
        sessions.forEach(item -> {
            try {
                item.getBasicRemote().sendText("PLAYERDEAD");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Scheduled(fixedDelay = 60000)
    public void changeTimeOfDay() {
        if (sessions.size() < playersNumber) {
            return;
        }

        isDay = !isDay;
        dayNumber++;

        String message;
        if (isDay) {
            message = "DAY " + dayNumber;
        } else {
            message = "NIGHT " + dayNumber;
        }

        sessions.forEach(item -> {
            try {
                item.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
