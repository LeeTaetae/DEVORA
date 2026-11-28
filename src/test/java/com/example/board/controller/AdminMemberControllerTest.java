package com.example.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.example.board.config.security.SecurityConfig;
import com.example.board.entity.Member;
import com.example.board.entity.Role;
import com.example.board.service.MemberService;
import com.example.board.support.StubViewResolverConfig;

@WebMvcTest(controllers = AdminMemberController.class)
@Import({SecurityConfig.class, StubViewResolverConfig.class})
class AdminMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    private Member admin(Long id) {
        Member member = Member.builder()
                .username("admin1").password("encoded").name("관리자").email("admin@test.com").role(Role.ADMIN)
                .build();
        ReflectionTestUtils.setField(member, "userId", id);
        return member;
    }

    private Member normalUser() {
        return Member.builder()
                .username("user1").password("encoded").name("홍길동").email("user1@test.com").role(Role.USER)
                .build();
    }

    @Test
    void 비로그인_사용자는_회원관리_목록에_접근할수없다() throws Exception {
        mockMvc.perform(get("/admin/members"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void 일반회원이_회원관리_목록에_접근하면_403이다() throws Exception {
        mockMvc.perform(get("/admin/members").with(user(normalUser())))
                .andExpect(status().isForbidden());
    }

    @Test
    void 관리자는_회원관리_목록을_조회할수있다() throws Exception {
        given(memberService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/admin/members").with(user(admin(1L))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/members"));
    }

    @Test
    void 관리자는_회원_수정폼을_조회할수있다() throws Exception {
        Member target = normalUser();
        ReflectionTestUtils.setField(target, "userId", 2L);
        given(memberService.findById(2L)).willReturn(target);

        mockMvc.perform(get("/admin/members/{id}/edit", 2L).with(user(admin(1L))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/member-edit"));
    }

    @Test
    void 관리자는_회원정보를_수정할수있다() throws Exception {
        mockMvc.perform(post("/admin/members/{id}/edit", 2L)
                        .with(user(admin(1L)))
                        .with(csrf())
                        .param("name", "새이름")
                        .param("email", "new@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/members"));

        verify(memberService).updateByAdmin(eq(2L), any());
    }

    @Test
    void 관리자는_다른_회원을_삭제할수있다() throws Exception {
        mockMvc.perform(post("/admin/members/{id}/delete", 2L)
                        .with(user(admin(1L)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/members"));

        verify(memberService).delete(2L);
    }

    @Test
    void 관리자는_자기자신을_삭제할수없다() throws Exception {
        mockMvc.perform(post("/admin/members/{id}/delete", 1L)
                        .with(user(admin(1L)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/members"));

        verify(memberService, never()).delete(any());
    }
}
