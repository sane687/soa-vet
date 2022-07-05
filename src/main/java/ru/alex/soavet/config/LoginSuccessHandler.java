package ru.alex.soavet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.alex.soavet.errors.UserResponseStatus;
import ru.alex.soavet.service.UserDetailsImpl;
import ru.alex.soavet.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Кастомные обрабочик удачных аутентификаций
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Autowired
    public LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод сбрасывающий счетчик неудачных попыток при успешной аутентификации
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDetailsImpl userDetails =  (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails.getFailedAttempt() > 0) {
            userService.resetFailedAttempts(userDetails.getUsername());
        }
        response.getWriter().write(new ObjectMapper().writeValueAsString(UserResponseStatus.SUCCESS));
        response.setStatus(200);
    }
}
