package com.shopping.domain;

import com.shopping.constant.Role;
import com.shopping.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String name;

    //주소
    private String address;
    private String phoneNumber;
    private String role; // 권한

}
