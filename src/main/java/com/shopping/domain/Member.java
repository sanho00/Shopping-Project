package com.shopping.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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
