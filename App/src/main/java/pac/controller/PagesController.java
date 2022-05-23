package pac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pac.service.UserService;

@Controller
@Validated
@RequiredArgsConstructor
public class PagesController {

    private final UserService userService;

    @RequestMapping("/page1")
    @GetMapping
    public String page1() {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        String img = userService.getUser(login).getImage();
        if (img != null & !userService.getUser(login).getRole().getName().equals("ADMIN")) {
            return "redirect:chatPage";
        }
        return "pages/page1";
    }

    @RequestMapping("/page2")
    @GetMapping
    public String page2() {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        String img = userService.getUser(login).getImage();
        if (img != null & !userService.getUser(login).getRole().getName().equals("ADMIN")) {
            return "redirect:chatPage";
        }
        return "pages/page2";
    }

    @RequestMapping("/page3")
    @GetMapping
    public String page3() {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        String img = userService.getUser(login).getImage();
        if (img != null & !userService.getUser(login).getRole().getName().equals("ADMIN")) {
            return "redirect:chatPage";
        }
        return "pages/page3";
    }

    @RequestMapping("/pageGhost")
    @GetMapping
    public String pageGhost() {

        return "pages/pageGhost";
    }
}