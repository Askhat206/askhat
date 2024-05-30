package kz.aptekaplus.config;


import kz.aptekaplus.filter.AuthInterceptor;
import kz.aptekaplus.service.JWTService;
import kz.aptekaplus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final JWTService jwtService;
    private final UserService userService;
    @Bean
    public MappedInterceptor loginInter() {
        return new MappedInterceptor(null, new AuthInterceptor(jwtService, userService));
    }
}
