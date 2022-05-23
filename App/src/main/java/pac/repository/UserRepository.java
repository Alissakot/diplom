package pac.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pac.model.Role;
import pac.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(value = "User.role")
    Optional<User> findByLogin(String login);

    @Modifying
    @Query("update User user set user.image = ?1 where user.login = ?2")
    void setImageByLogin(String image, String login);

    @Modifying
    @Query("update User user set user.role = ?1 where user.login = ?2")
    void setRole(Role role, String login);

    @Transactional
    void deleteAllByLoginIsNot(String login);
}