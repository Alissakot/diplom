package pac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pac.model.Role;
import pac.model.User;
import pac.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Override
    public Role checkAndChooseRole() {

        int maxPlayerNumber = 9;
        int maxPlayerOther = 6;
        int countPlayer1 = 0;
        int countPlayer2 = 0;
        int countPlayerOther = 0;
        Random random = new Random();
        Role role;
        List<String> roles = new ArrayList<>();

        List<User> users = userService.getUsers();
        if (users.size() < maxPlayerNumber) {
            Iterator<User> userIterator = users.iterator();
            while (userIterator.hasNext()) {
                User user = userIterator.next();
                String userRoleName = user.getRole().getName();
                if (userRoleName.equals("PLAYER1")) {
                    countPlayer1++;
                } else if (userRoleName.equals("PLAYER2")) {
                    countPlayer2++;
                } else if (userRoleName.equals("PLAYEROTHER")) {
                    countPlayerOther++;
                }
            }
            if (countPlayer1 == 0) {
                roles.add("PLAYER1");
            }
            if (countPlayer2 == 0) {
                roles.add("PLAYER2");
            }
            if (countPlayerOther < maxPlayerOther) {
                roles.add("PLAYEROTHER");
            }

            role = roleRepository.findByName(roles.get(random.nextInt(roles.size())));

        } else {
            role = roleRepository.findByName("GHOST");
        }

        return role;
    }

    @Override
    public String homePage(Role role) {
        if (role.getName().equals("PLAYER1")) {
            return "/page1";
        } else if (role.getName().equals("PLAYER2")) {
            return "/page2";
        } else if (role.getName().equals("PLAYEROTHER")) {
            return "/page3";
        } else if (role.getName().equals("GHOST")) {
            return "/pageGhost";
        }
        return "";
    }
}