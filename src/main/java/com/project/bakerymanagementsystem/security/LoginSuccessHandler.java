//package com.tahadonuk.restaurantmanagementsystem.security;
//
//import com.tahadonuk.restaurantmanagementsystem.data.UserRole;
//import com.tahadonuk.restaurantmanagementsystem.service.UserService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//
//@Component
//public class LoginSuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    UserService userService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
//    }
//
//    @SneakyThrows
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        String username = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
//
//        CustomUserDetails details = ((CustomUserDetails) authentication.getPrincipal());
////
////        if(details.getUser().getRole() == UserRole.USER){
////            response.sendRedirect("/disabled");
////            System.out.println("a");
////            return;
////        }
//
//        userService.updateLoginDate(username);
//
//        response.sendRedirect(request.getContextPath());
//    }
//
//
//}
