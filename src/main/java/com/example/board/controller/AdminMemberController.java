package com.example.board.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.dto.AdminMemberUpdateRequest;
import com.example.board.entity.Member;
import com.example.board.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "admin/members";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);

        AdminMemberUpdateRequest request = new AdminMemberUpdateRequest();
        request.setName(member.getName());
        request.setEmail(member.getEmail());

        model.addAttribute("memberId", id);
        model.addAttribute("adminMemberUpdateRequest", request);
        return "admin/member-edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                        @Valid @ModelAttribute("adminMemberUpdateRequest") AdminMemberUpdateRequest request,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberId", id);
            return "admin/member-edit";
        }

        memberService.updateByAdmin(id, request);
        return "redirect:/admin/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                          @AuthenticationPrincipal Member admin,
                          RedirectAttributes redirectAttributes) {
        if (admin.getUserId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "본인 계정은 삭제할 수 없습니다.");
            return "redirect:/admin/members";
        }

        memberService.delete(id);
        return "redirect:/admin/members";
    }
}
