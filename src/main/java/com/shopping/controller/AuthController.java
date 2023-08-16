package com.shopping.controller;

import com.shopping.domain.Member;
import com.shopping.dto.SignupDto;
import com.shopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(SignupDto signupDto) {

        Member member = signupDto.toEntity();

        Member memberEntity = authService.signup(member);
        System.out.println(memberEntity);

        return "login";
    }

}
