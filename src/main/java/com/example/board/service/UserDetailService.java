package com.example.board.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.board.entity.Member;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service 
public class UserDetailService implements UserDetailsService{
    private final MemberRepository memberRepository;

    // 사용자 이름(email)으로 사용자 정보를 조회하는 메서드
    @Override
    public Member loadUserByUsername(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
