package com.example.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import com.example.board.service.MemberService;
import com.example.board.support.StubViewResolverConfig;

@WebMvcTest(controllers = SignupController.class)
@Import({SecurityConfig.class, StubViewResolverConfig.class})
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Test
    void 회원가입_폼은_누구나_접근가능하다() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    void 유효한_요청으로_가입하면_로그인페이지로_리다이렉트한다() throws Exception {
        given(memberService.signup(any())).willReturn(1L);

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", "user1")
                        .param("password", "password123")
                        .param("name", "홍길동")
                        .param("email", "user1@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(memberService).signup(any());
    }

    @Test
    void 입력값이_비어있으면_다시_가입폼을_보여준다() throws Exception {
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", "")
                        .param("password", "")
                        .param("name", "")
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));

        verify(memberService, org.mockito.Mockito.never()).signup(any());
    }

    @Test
    void 중복아이디면_에러와함께_가입폼을_다시_보여준다() throws Exception {
        given(memberService.signup(any())).willThrow(new IllegalArgumentException("이미 사용 중인 아이디입니다."));

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", "dup")
                        .param("password", "password123")
                        .param("name", "홍길동")
                        .param("email", "user1@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }
}
