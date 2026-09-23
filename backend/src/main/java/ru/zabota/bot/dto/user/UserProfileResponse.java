package ru.zabota.bot.dto.user;

import java.util.List;

/*

 * DTO полного профиля пользователя.
 *
 * Объединяет основную информацию о пользователе
 * и список его детей.
 */
public class UserProfileResponse {

    private UserResponse user;

    private List<UserChildResponse> children;

    public UserProfileResponse() {
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<UserChildResponse> getChildren() {
        return children;
    }

    public void setChildren(List<UserChildResponse> children) {
        this.children = children;
    }
}
