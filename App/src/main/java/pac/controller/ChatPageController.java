package pac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pac.model.User;
import pac.service.UserService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@Validated
@RequiredArgsConstructor
public class ChatPageController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @RequestMapping("/chatPage")
    @PostMapping
    public String chatPage(Model model, String img) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        if (img != null) {
            userService.setImage(login, img);
        }

        UserDetails details = userDetailsService.loadUserByUsername(login);
        Authentication auth = new UsernamePasswordAuthenticationToken(details.getUsername(), details.getPassword(), details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        List<User> users = userService.getUsers();
        List<User> users1 = users.stream().filter(user -> !user.getRole().getName().equals("ADMIN")).collect(Collectors.toList());

        model.addAttribute("users", users1);
        model.addAttribute("login", login);
        model.addAttribute("isDay", GameInfoController.isDay);
        model.addAttribute("dayNumber", GameInfoController.dayNumber);
        return "pages/chatPage";
    }

    @RequestMapping("/chatPage/execute")
    @PostMapping
    public ResponseEntity execute(String login) {
        User user = userService.getUser(login);

        if (user.getRole().getName().equals("PLAYER2")) {
            endGame("common");
        }

        userService.madeGhost(login);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/chatPage/kill")
    @PostMapping
    public ResponseEntity kill(String login) {
        User user = userService.getUser(login);
        if (user.getRole().getName().equals("PLAYER1")) {
            List<User> users = userService.getUsers();
            List<User> users1 = users.stream().filter(player ->
                    player.getRole().getName().equals("PLAYEROTHER")).toList();
            int numberPlayers = users1.size();

            if (numberPlayers == 0) {
                endGame("werewolf");
                return ResponseEntity.ok().build();
            }

            Random random = new Random();
            userService.madePlayer1(users1.get(random.nextInt(numberPlayers)).getLogin());
        }

        //GameInfoController.PlayerDead();

        userService.madeGhost(login);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/chatPage/bite")
    @PostMapping
    public ResponseEntity bite(String login) {
        User user = userService.getUser(login);
        Random random = new Random();
        int i = random.nextInt(100);
        if (i <= 30) {
            endGame("common");
        } else userService.madeWerewolf(login);
        return ResponseEntity.ok().build();
    }

    private void endGame(String endingType) {
        GameInfoController.EndGame(endingType);
    }
}
