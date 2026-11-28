package com.example.board.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.board.dto.SignupRequest;
import com.example.board.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupRequest") SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            memberService.signup(request);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("signupError", e.getMessage());
            return "signup";
        }

        return "redirect:/login";
    }
}
