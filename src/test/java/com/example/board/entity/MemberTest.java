package com.example.board.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class MemberTest {

    @Test
    void 일반회원은_ROLE_USER_권한을_가진다() {
        Member member = Member.builder()
                .username("user1")
                .password("encoded")
                .name("홍길동")
                .email("user1@test.com")
                .role(Role.USER)
                .build();

        assertThat(member.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void 관리자는_ROLE_ADMIN_권한을_가진다() {
        Member admin = Member.builder()
                .username("admin1")
                .password("encoded")
                .name("관리자")
                .email("admin1@test.com")
                .role(Role.ADMIN)
                .build();

        assertThat(admin.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void changePassword는_비밀번호를_교체한다() {
        Member member = Member.builder()
                .username("user1").password("old").name("홍길동").email("a@test.com").role(Role.USER)
                .build();

        member.changePassword("new-encoded");

        assertThat(member.getPassword()).isEqualTo("new-encoded");
    }

    @Test
    void updateProfile는_이름과_이메일을_변경한다() {
        Member member = Member.builder()
                .username("user1").password("pw").name("old-name").email("old@test.com").role(Role.USER)
                .build();

        member.updateProfile("new-name", "new@test.com");

        assertThat(member.getName()).isEqualTo("new-name");
        assertThat(member.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void getUsername은_이메일을_반환한다() {
        Member member = Member.builder()
                .username("user1").password("pw").name("홍길동").email("login-id@test.com").role(Role.USER)
                .build();

        assertThat(member.getUsername()).isEqualTo("login-id@test.com");
    }

    @Test
    void getLoginId는_실제_아이디_필드를_반환한다() {
        Member member = Member.builder()
                .username("actual-username").password("pw").name("홍길동").email("login-id@test.com").role(Role.USER)
                .build();

        assertThat(member.getLoginId()).isEqualTo("actual-username");
    }
}
