package com.example.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.board.entity.Member;
import com.example.board.entity.Role;

/**
 * 실제 Hibernate 매핑(제약조건, 유니크 제약)이 정상 동작하는지 확인하는 레포지토리 레벨 테스트.
 * 서비스 단위 테스트는 Repository를 Mock으로 대체하므로 이 부분은 검증하지 못한다.
 */
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member(String username, String email) {
        return Member.builder()
                .username(username)
                .password("encoded-pw")
                .name("홍길동")
                .email(email)
                .role(Role.USER)
                .build();
    }

    @Test
    void 회원을_저장하고_이메일로_조회할수있다() {
        memberRepository.save(member("user1", "user1@test.com"));

        assertThat(memberRepository.findByEmail("user1@test.com")).isPresent();
        assertThat(memberRepository.findByEmail("없는사람@test.com")).isEmpty();
    }

    @Test
    void existsByUsername과_existsByEmail이_정상동작한다() {
        memberRepository.save(member("user1", "user1@test.com"));

        assertThat(memberRepository.existsByUsername("user1")).isTrue();
        assertThat(memberRepository.existsByUsername("없음")).isFalse();
        assertThat(memberRepository.existsByEmail("user1@test.com")).isTrue();
        assertThat(memberRepository.existsByEmail("없음@test.com")).isFalse();
    }

    @Test
    void 아이디가_중복되면_저장시_제약조건_위반이_발생한다() {
        memberRepository.save(member("dup", "a@test.com"));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            memberRepository.save(member("dup", "b@test.com"));
            memberRepository.flush();
        });
    }

    @Test
    void 이메일이_중복되면_저장시_제약조건_위반이_발생한다() {
        memberRepository.save(member("user1", "dup@test.com"));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            memberRepository.save(member("user2", "dup@test.com"));
            memberRepository.flush();
        });
    }

    @Test
    void createdAt은_저장시_자동으로_채워진다() {
        Member saved = memberRepository.save(member("user1", "user1@test.com"));

        assertThat(saved.getCreatedAt()).isNotNull();
    }
}
