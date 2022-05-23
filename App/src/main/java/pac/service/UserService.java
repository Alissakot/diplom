package pac.service;

import pac.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> getUsers();

    User getUser(String login);

    void setImage(String login, String image);

    void madeGhost(String login);

    void madePlayer1(String login);

    void madeWerewolf(String login);

    void clearDatabase();
}