package goodspace.backend.admin.service.user;

import goodspace.backend.admin.dto.user.UserInfoDto;

import java.util.List;

public interface UserManageService {
    List<UserInfoDto> getUsers();

    void removeUser(long userId);
}
