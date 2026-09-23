package ru.zabota.bot.dto.user;

import java.util.List;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;

/*

 * DTO полного профиля пользователя.
 *
 * Объединяет основную информацию о пользователе
 * и список его детей.
 */
public class UserProfileResponse {

    private UserResponse user;

    private List<UserChildResponse> children;

    private List<UserSupportResponse> supports;

    public UserProfileResponse() {
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<UserSupportResponse> getSupports() {
        return supports;
    }

    public void setSupports(List<UserSupportResponse> supports) {
        this.supports = supports;
    }

    public List<UserChildResponse> getChildren() {
        return children;
    }

    public void setChildren(List<UserChildResponse> children) {
        this.children = children;
    }
}
