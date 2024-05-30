package kz.aptekaplus.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.aptekaplus.model.User;
import kz.aptekaplus.service.JWTService;
import kz.aptekaplus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTService jwtService;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String refreshToken = null;
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
                if (refreshToken != null) {
                    UUID userId = UUID.fromString(jwtService.extractID(refreshToken));
                    User user = userService.findById(userId);
                    if (user != null) {
                        modelAndView.addObject("token", refreshToken);
                        modelAndView.addObject("userId", userId);
                        modelAndView.addObject("user", user);
                        System.out.println("==================");
                        System.out.println("777");
                        System.out.println(refreshToken);
                        System.out.println("userId");
                        System.out.println(userId);
                        System.out.println("==================");

                    }

                }
            }

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
    }
}
