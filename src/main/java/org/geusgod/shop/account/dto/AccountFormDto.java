package org.geusgod.shop.account.dto;

import lombok.Getter;
import lombok.Setter;
import org.geusgod.shop.account.constant.Role;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String Name;

    private String email;

    private String password;

    private String address;

    private Role role;

}
