package com.shopping.dto;

import com.shopping.domain.Member;
import lombok.Data;

@Data
public class SignupDto {

    private String username;
    private String password;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
