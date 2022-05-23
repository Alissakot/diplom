package pac.service;

import pac.model.Role;

public interface RoleService {

    Role checkAndChooseRole();

    String homePage(Role role);
}
