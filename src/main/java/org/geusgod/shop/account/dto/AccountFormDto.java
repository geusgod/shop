package org.geusgod.shop.account.dto;

import lombok.Getter;
import lombok.Setter;
import org.geusgod.shop.account.constant.Role;

@Getter
@Setter
public class AccountFormDto {

    private String Name;

    private String email;

    private String password;

    private String address;

    private Role role;

}
