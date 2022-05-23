package pac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pac.service.UserService;

@Controller
@Validated
@RequiredArgsConstructor
public class AdminPageController {

    private final UserService userService;

    @RequestMapping("/adminPage")
    @GetMapping
    public String adminPage() {

        return "pages/adminPage";
    }

    @RequestMapping("/clear")
    @GetMapping
    public String clearDB() {
        userService.clearDatabase();
        return "pages/adminPage";
    }
}