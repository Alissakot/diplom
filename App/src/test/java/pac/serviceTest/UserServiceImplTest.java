package pac.serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pac.model.Role;
import pac.model.User;
import pac.model.type.Status;
import pac.repository.RoleRepository;
import pac.repository.UserRepository;
import pac.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("UserService метод ")
public class UserServiceImplTest {

    @MockBean
    UserRepository mock;

    @MockBean
    RoleRepository mockRole;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getUsers должен возвращать правильное число пользователей")
    public void testGetUsers() {
        User u = User.builder()
                .id(10L)
                .login("Иван")
                .password("123")
                .status(Status.OK)
                .build();

        Mockito.when(mock.findAll()).thenReturn(
                List.of(u));

        List<User> users = userService.getUsers();

        Assertions.assertEquals(1, users.size());
    }

    @Test
    @DisplayName("getUsers должен возвращать корректного пользователя")
    public void testGetUserFromDb() {
        User u = User.builder()
                .id(10L)
                .login("Иван")
                .password("123")
                .status(Status.OK)
                .build();

        Mockito.when(mock.findAll()).thenReturn(
                List.of(u));

        List<User> users = userService.getUsers();

        Assertions.assertEquals(u, users.get(0));
    }

    @Test
    @DisplayName("getUser должен возвращать пользователя по логину")
    public void testGetUser() {
        String login = "Иван";
        User u = User.builder()
                .id(10L)
                .login(login)
                .password("123")
                .status(Status.OK)
                .build();

        Mockito.when(mock.findByLogin(login)).thenReturn(Optional.of(u));

        User user = userService.getUser(login);

        Assertions.assertEquals(u, user);
    }

    @Test
    @DisplayName("SetImage должен записывать название изображения в БД")
    public void testSetImage() {
        String login = "Иван";
        String image = "img.jpg";

        userService.setImage(login, image);

        Mockito.verify(mock, times(1)).setImageByLogin(image, login);
    }

    @Test
    @DisplayName("MadeGhost должен менять роль пользователя в БД и устанавливать новое изображение")
    public void madeGhost() {

        String login = "Иван";
        String image = "img.jpg";
        String roleName1 = "PLAYER1";
        String roleName2 = "PLAYER2";

        Role role1 = Role.builder()
                .id(1L)
                .name(roleName1)
                .build();

        Role role2 = Role.builder()
                .id(2L)
                .name(roleName2)
                .build();

        User u = User.builder()
                .id(10L)
                .login(login)
                .password("123")
                .status(Status.OK)
                .image(image)
                .role(role1)
                .build();

        Mockito.when(mock.findByLogin(login)).thenReturn(Optional.of(u));
        Mockito.when(mockRole.findByName(roleName2)).thenReturn(role2);

        userService.madeGhost(login);

        Mockito.verify(mock, times(1)).setImageByLogin("d_" + image, login);

        mock.setRole(role2, login);

        Mockito.verify(mock, times(1)).setRole(role2, login);
    }

    @Test
    @DisplayName("ClearDatabase должен удалять из базы всех пользователей, кроме админа")
    public void testClearDatabase() {

        String loginAdmin = "admin";

        userService.clearDatabase();

        Mockito.verify(mock, times(1)).deleteAllByLoginIsNot(loginAdmin);
    }
}