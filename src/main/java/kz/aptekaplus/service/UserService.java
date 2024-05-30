package kz.aptekaplus.service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kz.aptekaplus.model.Product;
import kz.aptekaplus.model.User;
import kz.aptekaplus.repository.UserRepository;
import kz.aptekaplus.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final ProductServiceImpl productServiceImpl;
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(
                        String.format("user with phoneNumber %s not found", username)));
    }


    @Transactional
    public void updateUser(User userNew, HttpServletRequest request) {
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
                User user = findById(userId);
                if (user != null) {
                    user.setEntrance(userNew.getEntrance());
                    user.setFlat(userNew.getFlat());
                    user.setFloor(userNew.getFloor());
                    user.setStreetAndHouse(userNew.getStreetAndHouse());
                    userRepository.saveAndFlush(user);
                }

            }
        }




    }

    @Transactional
    public void updateUser2(User user) {
        userRepository.saveAndFlush(user);
    }

    public List<Product> getFavourites(HttpServletRequest request) {
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
                User user = findById(userId);
                if (user != null) {
                    List<String> favouriteProducts = user.getFavouriteProducts();
                    List<Product> favourites = new ArrayList<>();
                    if (!favouriteProducts.isEmpty())
                    for (String id : favouriteProducts) {
                        try {
                            favourites.add(productServiceImpl.findProduct(UUID.fromString(id)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return favourites;
                }

            }
        }
        return null;

    }

    @Transactional
    public void addFavourites(HttpServletRequest request, UUID productId) {
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
                User user = findById(userId);
                if (user != null) {
                    user.setFavouriteProducts(new ArrayList<>());
                    user.getFavouriteProducts().add(String.valueOf(productId));
                    userRepository.saveAndFlush(user);
                }

            }
        }
    }

}
