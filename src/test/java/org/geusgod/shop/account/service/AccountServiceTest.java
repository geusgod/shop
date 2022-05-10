package org.geusgod.shop.account.service;

import lombok.RequiredArgsConstructor;
import org.geusgod.shop.account.constant.Role;
import org.geusgod.shop.account.dto.AccountFormDto;
import org.geusgod.shop.account.entity.Account;
import org.geusgod.shop.account.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
//@TestPropertySource(locations = "classpath:application-test.properties")
class AccountServiceTest {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    @AfterEach
    public void clearData() {
        accountRepository.deleteAll();
    }

    public Account createMember() {
        AccountFormDto accountFormDto = new AccountFormDto();
        accountFormDto.setName("홍길동");
        accountFormDto.setEmail("test@gmail.com");
        accountFormDto.setPassword("1111");
        accountFormDto.setAddress("주소");
        accountFormDto.setRole(Role.USER);
        return Account.createAccount(accountFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void accountJoinTest() {
        //given
        Account account = createMember();

        //when
        Account saveAccount = accountService.saveAccount(account);

        //then
        assertEquals(account.getEmail(), saveAccount.getEmail());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void accountJoinDuplicateTest() {
        //given
        Account account1 = createMember();
        Account account2 = createMember();

        //when
        accountService.saveAccount(account1);

        //then
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            accountService.saveAccount(account2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }

}