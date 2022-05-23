package pac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pac.model.Role;
import pac.model.User;
import pac.security.UserPrincipal;
import pac.repository.RoleRepository;
import pac.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).map(UserPrincipal::new)
                .orElseThrow(() -> new IllegalArgumentException("Has no user with login " + login));
    }

    @Override
    public void setImage(String login, String image) {
        userRepository.setImageByLogin(image, login);
    }

    @Override
    public void madeGhost(String login) {
        Role role = roleRepository.findByName("GHOST");
        String img = getUser(login).getImage().replace("p1_", "").replace("d_", "");
        userRepository.setImageByLogin("d_" + img, login);
        userRepository.setRole(role, login);
    }

    @Override
    public void madePlayer1(String login) {
        String img = getUser(login).getImage().replace("p1_", "").replace("d_", "");
        Role role = roleRepository.findByName("PLAYER1");
        userRepository.setImageByLogin("p1_" + img, login);
        userRepository.setRole(role, login);
    }

    @Override
    public void madeWerewolf(String login) {
        Role role = roleRepository.findByName("WEREWOLF");
        userRepository.setRole(role, login);
    }

    @Override
    public void clearDatabase() {
        userRepository.deleteAllByLoginIsNot("admin");
    }
}