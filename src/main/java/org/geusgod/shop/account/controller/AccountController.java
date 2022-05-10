package org.geusgod.shop.account.controller;

import lombok.RequiredArgsConstructor;
import org.geusgod.shop.account.constant.Role;
import org.geusgod.shop.account.dto.AccountFormDto;
import org.geusgod.shop.account.entity.Account;
import org.geusgod.shop.account.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/account")
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String accountLoginView() {
        return "account/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "account/login";
    }

    @GetMapping("/register")
    public String accountRegister(Model model) {
        model.addAttribute("accountFormDto", new AccountFormDto());
        return "account/register";
    }

    @PostMapping("/register")
    public String  accountAddPost(AccountFormDto accountFormDto) {
        accountFormDto.setRole(Role.USER);
        Account account = Account.createAccount(accountFormDto, passwordEncoder);
        accountService.saveAccount(account);

        return "redirect:/";
    }
}
