package pac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pac.model.Role;
import pac.model.User;
import pac.model.type.Status;
import pac.service.RoleService;
import pac.service.UserService;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RoleService roleService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @RequestMapping("/")
    public String staticResource() {
        return "index";
    }

    @RequestMapping("/registration")
    @GetMapping
    public String registration(User user) {
        User existingUser = userService.getUser(user.getLogin());
        if (existingUser == null) {
            user.setStatus(Status.OK);
            Role role = roleService.checkAndChooseRole();
            user.setRole(role);
            userService.saveUser(user);
            existingUser = user;
        }
        UserDetails details = userDetailsService.loadUserByUsername(user.getLogin());
        Authentication auth = new UsernamePasswordAuthenticationToken(details.getUsername(), details.getPassword(), details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:" + roleService.homePage(existingUser.getRole());
    }
}