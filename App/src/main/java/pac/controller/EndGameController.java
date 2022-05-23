package pac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
@RequiredArgsConstructor
public class EndGameController {

    @RequestMapping("/endGame")
    @GetMapping
    public String endGame() {
        return "pages/endGameCommon";
    }

    @RequestMapping("/endGameFullMoon")
    @GetMapping
    public String fullMoon() {
        return "pages/endGameWerewolves";
    }
}
