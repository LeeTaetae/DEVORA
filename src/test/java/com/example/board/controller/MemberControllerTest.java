package com.example.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.board.config.security.SecurityConfig;
import com.example.board.entity.Member;
import com.example.board.entity.Role;
import com.example.board.service.MemberService;
import com.example.board.support.StubViewResolverConfig;

@WebMvcTest(controllers = MemberController.class)
@Import({SecurityConfig.class, StubViewResolverConfig.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    private Member loggedInMember() {
        return Member.builder()
                .username("user1").password("encoded").name("홍길동").email("user1@test.com").role(Role.USER)
                .build();
    }

    @Test
    void 비로그인_상태로_접근하면_로그인으로_리다이렉트된다() throws Exception {
        mockMvc.perform(get("/members/password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void 로그인한_사용자는_비밀번호변경_폼에_접근할수있다() throws Exception {
        mockMvc.perform(get("/members/password").with(user(loggedInMember())))
                .andExpect(status().isOk())
                .andExpect(view().name("members/password"));
    }

    @Test
    void 비밀번호_변경_성공시_로그인페이지로_리다이렉트한다() throws Exception {
        mockMvc.perform(post("/members/password")
                        .with(user(loggedInMember()))
                        .with(csrf())
                        .param("currentPassword", "old-pw")
                        .param("newPassword", "new-password")
                        .param("confirmPassword", "new-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?passwordChanged"));
    }

    @Test
    void 새비밀번호와_확인이_다르면_다시_폼을_보여준다() throws Exception {
        mockMvc.perform(post("/members/password")
                        .with(user(loggedInMember()))
                        .with(csrf())
                        .param("currentPassword", "old-pw")
                        .param("newPassword", "new-password")
                        .param("confirmPassword", "different"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/password"));
    }

    @Test
    void 현재비밀번호가_틀리면_에러와함께_폼을_다시_보여준다() throws Exception {
        doThrow(new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다."))
                .when(memberService).changePassword(eq("user1@test.com"), any());

        mockMvc.perform(post("/members/password")
                        .with(user(loggedInMember()))
                        .with(csrf())
                        .param("currentPassword", "wrong")
                        .param("newPassword", "new-password")
                        .param("confirmPassword", "new-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/password"));
    }
}
