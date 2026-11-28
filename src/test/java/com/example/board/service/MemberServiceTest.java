package com.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.dto.AdminMemberUpdateRequest;
import com.example.board.dto.PasswordChangeRequest;
import com.example.board.dto.SignupRequest;
import com.example.board.entity.Member;
import com.example.board.entity.Role;
import com.example.board.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    private Member existingMember(String email, String encodedPassword) {
        return Member.builder()
                .username("user1")
                .password(encodedPassword)
                .name("홍길동")
                .email(email)
                .role(Role.USER)
                .build();
    }

    @Test
    void 회원가입_성공시_비밀번호를_암호화하여_USER_권한으로_저장한다() {
        SignupRequest request = new SignupRequest("user1", "password123", "홍길동", "user1@test.com");
        given(memberRepository.existsByUsername("user1")).willReturn(false);
        given(memberRepository.existsByEmail("user1@test.com")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encoded-pw");
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

        memberService.signup(request);

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(captor.capture());
        Member saved = captor.getValue();

        assertThat(saved.getLoginId()).isEqualTo("user1");
        assertThat(saved.getPassword()).isEqualTo("encoded-pw");
        assertThat(saved.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void 아이디가_중복이면_가입에_실패한다() {
        SignupRequest request = new SignupRequest("dup", "password123", "홍길동", "new@test.com");
        given(memberRepository.existsByUsername("dup")).willReturn(true);

        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디");

        verify(memberRepository, never()).save(any());
    }

    @Test
    void 이메일이_중복이면_가입에_실패한다() {
        SignupRequest request = new SignupRequest("user1", "password123", "홍길동", "dup@test.com");
        given(memberRepository.existsByUsername("user1")).willReturn(false);
        given(memberRepository.existsByEmail("dup@test.com")).willReturn(true);

        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일");

        verify(memberRepository, never()).save(any());
    }

    @Test
    void 현재비밀번호가_일치하면_비밀번호를_변경한다() {
        Member member = existingMember("user1@test.com", "old-encoded");
        PasswordChangeRequest request = new PasswordChangeRequest("old-pw", "new-pw", "new-pw");

        given(memberRepository.findByEmail("user1@test.com")).willReturn(Optional.of(member));
        given(passwordEncoder.matches("old-pw", "old-encoded")).willReturn(true);
        given(passwordEncoder.encode("new-pw")).willReturn("new-encoded");

        memberService.changePassword("user1@test.com", request);

        assertThat(member.getPassword()).isEqualTo("new-encoded");
    }

    @Test
    void 현재비밀번호가_틀리면_비밀번호변경에_실패한다() {
        Member member = existingMember("user1@test.com", "old-encoded");
        PasswordChangeRequest request = new PasswordChangeRequest("wrong-pw", "new-pw", "new-pw");

        given(memberRepository.findByEmail("user1@test.com")).willReturn(Optional.of(member));
        given(passwordEncoder.matches("wrong-pw", "old-encoded")).willReturn(false);

        assertThatThrownBy(() -> memberService.changePassword("user1@test.com", request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("일치하지");

        assertThat(member.getPassword()).isEqualTo("old-encoded");
    }

    @Test
    void 관리자가_회원정보를_수정하면_이름과_이메일이_바뀐다() {
        Member member = existingMember("user1@test.com", "encoded");
        AdminMemberUpdateRequest request = new AdminMemberUpdateRequest();
        request.setName("새이름");
        request.setEmail("new-email@test.com");

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        memberService.updateByAdmin(1L, request);

        assertThat(member.getName()).isEqualTo("새이름");
        assertThat(member.getEmail()).isEqualTo("new-email@test.com");
    }

    @Test
    void 회원삭제는_repository의_deleteById를_호출한다() {
        memberService.delete(5L);

        verify(memberRepository).deleteById(5L);
    }
}
