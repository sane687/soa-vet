package ru.alex.soavet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.alex.soavet.errors.UserResponseStatus;
import ru.alex.soavet.model.User;
import ru.alex.soavet.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Кастомные обрабочик неудачный аутентификаций
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    UserService userService;

    @Autowired
    public LoginFailureHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод который засчитывает неудачные попытки входа.
     * Блокирует пользователя на 10 минут если он совершил 10 неудачных попыток
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        User user = userService.findUserByUsername(username);

        if(user != null){
            if(user.isNonLocked()){
                if(user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS){
                    userService.increaseFailedAttempts(user);
                }else {
                    userService.lock(user);
                    response.getWriter().write(new ObjectMapper().writeValueAsString(UserResponseStatus.TOO_MANY_FAILED_ATTEMPTS));
                    response.setStatus(403);
                    return;
                }
            } else {
                if(!userService.unlockWhenTimeExpired(user)){
                    response.getWriter().write(new ObjectMapper().writeValueAsString(UserResponseStatus.TOO_MANY_FAILED_ATTEMPTS));
                    response.setStatus(403);
                    return;
                }
                }
            }

        response.getWriter().write(new ObjectMapper().writeValueAsString(UserResponseStatus.INCORRECT_USERNAME_OR_PASSWORD));
        response.setStatus(403);
        }
    }
