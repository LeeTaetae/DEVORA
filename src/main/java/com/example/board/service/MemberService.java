package com.example.board.service;

import org.springframework.stereotype.Service;

import com.example.board.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor 
public class MemberService {
    private final MemberRepository memberRepository;

    
}
