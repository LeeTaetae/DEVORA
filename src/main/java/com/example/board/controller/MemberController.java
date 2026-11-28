package com.example.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.board.dto.PasswordChangeRequest;
import com.example.board.entity.Member;
import com.example.board.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/password")
    public String passwordForm(Model model) {
        model.addAttribute("passwordChangeRequest", new PasswordChangeRequest());
        return "members/password";
    }

    @PostMapping("/members/password")
    public String changePassword(@AuthenticationPrincipal Member member,
                                  @Valid @ModelAttribute("passwordChangeRequest") PasswordChangeRequest request,
                                  BindingResult bindingResult,
                                  HttpServletRequest httpRequest) {
        if (bindingResult.hasErrors()) {
            return "members/password";
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            bindingResult.reject("passwordMismatch", "새 비밀번호가 일치하지 않습니다.");
            return "members/password";
        }

        try {
            memberService.changePassword(member.getEmail(), request);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("passwordError", e.getMessage());
            return "members/password";
        }

        // 비밀번호가 바뀌었으므로 현재 세션은 무효화하고 재로그인하도록 한다.
        httpRequest.getSession().invalidate();
        return "redirect:/login?passwordChanged";
    }
}
