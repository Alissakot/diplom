package pac.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Authorities {

    static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
    public static final String ROLE_PLAYER1 = ROLE_PREFIX + "PLAYER1";
    public static final String ROLE_PLAYER2 = ROLE_PREFIX + "PLAYER2";
    public static final String ROLE_PLAYEROTHER = ROLE_PREFIX + "PLAYEROTHER";
    public static final String ROLE_GHOST = ROLE_PREFIX + "GHOST";
    public static final String ROLE_WEREWOLF = ROLE_PREFIX + "WEREWOLF";

    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_ADMIN::equals);
    }

    public boolean isPlayer1() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_PLAYER1::equals);
    }

    public boolean isPlayer2() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_PLAYER2::equals);
    }

    public boolean isPlayerOther() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_PLAYEROTHER::equals);
    }

    public boolean isGhost() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_GHOST::equals);
    }

    public boolean isWerewolf() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_WEREWOLF::equals);
    }
}