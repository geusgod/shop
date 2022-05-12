package org.geusgod.shop.account.controller;

import lombok.RequiredArgsConstructor;
import org.geusgod.shop.account.constant.Role;
import org.geusgod.shop.account.dto.AccountFormDto;
import org.geusgod.shop.account.entity.Account;
import org.geusgod.shop.account.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;


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
    public String accountAddPost(@Valid AccountFormDto accountFormDto, Errors errors, Model model) {
        if(errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("fields", accountFormDto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = accountService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "account/register";
        }

        try {
            accountFormDto.setRole(Role.USER);
            Account account = Account.createAccount(accountFormDto, passwordEncoder);
            accountService.saveAccount(account);
        }catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("fields", accountFormDto);
            return "account/register";
        }

        return "redirect:/";
    }
}
