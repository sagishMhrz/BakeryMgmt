//package com.tahadonuk.restaurantmanagementsystem.security;
//
//import com.tahadonuk.restaurantmanagementsystem.service.UserService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class LogoutHandler implements LogoutSuccessHandler {
//    @Autowired
//    UserService userService;
//    @Override
//    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//
//        String username = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
//
//        userService.updateLogoutDate(username);
//
//        httpServletRequest.logout();
//        httpServletRequest.getSession().invalidate();
//        httpServletResponse.sendRedirect("/login?logout");
//    }
//
//
//}
