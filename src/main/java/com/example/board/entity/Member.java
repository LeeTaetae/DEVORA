package com.example.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Entity
@Getter 
@Table (name = "member")
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Member implements UserDetails{

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column (name = "user_name", unique = true, nullable = false, length = 50)
    private String username;

    @Column (name = "password", nullable = false, length = 255)
    private String password;

    @Column (name = "name", nullable = false, length = 50)
    private String name;

    @Column (name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Enumerated (EnumType.STRING)
    @Column (name = "role", nullable = false)
    private Role role;

    @Column (name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder 
    public Member(String username, String password, String name, String email, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
    
    // 사용자가 URL/리소스에 접근할 수 있는 권한을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override 
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // getUsername()은 UserDetails 규약상 email을 반환하므로, 실제 로그인 아이디(username 필드)는 이 메서드로 조회한다.
    public String getLoginId() {
        return username;
    }

    @Override 
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았는지 여부를 반환. true -> 만료되지 않음.
    }

    @Override 
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠겨있지 않은지 여부를 반환. true -> 잠겨있지 않음.
    }

    @Override 
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않았는지 여부를 반환. true -> 만료되지 않음.
    }

    @Override 
    public boolean isEnabled() {
        return true; // 계정이 활성화되어 있는지 여부를 반환. true -> 사용 가능.
    }
}