package org.geusgod.shop.account.repository;

import org.geusgod.shop.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByEmail(String email);
}
