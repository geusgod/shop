package org.geusgod.shop.account.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.geusgod.shop.account.constant.Role;
import org.geusgod.shop.account.dto.AccountFormDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@ToString
@Table(name="account")
@Entity
public class Account {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Account(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public static Account createAccount(AccountFormDto accountFormDto, PasswordEncoder passwordEncoder) {
        return Account.builder()
                .name(accountFormDto.getName())
                .email(accountFormDto.getEmail())
                .password(passwordEncoder.encode(accountFormDto.getPassword()))
                .address(accountFormDto.getAddress())
                .role(accountFormDto.getRole())
                .build();
    }
}
