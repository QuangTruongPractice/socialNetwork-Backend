package com.tqt.components;

import com.tqt.pojo.Account;
import com.tqt.services.AccountService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AccountScheduler {

    @Autowired
    private AccountService accountService;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void disableExpiredAccounts() {
        List<Account> accounts = this.accountService.getAccounts(null);
        for (Account acc : accounts) {
            if (acc.getMustChangePassword() && LocalDateTime.now().isAfter(acc.getPasswordExpiresAt())) {
                acc.setIsActive(false);
                this.accountService.addOrUpdateAccount(acc);
            }
        }
        System.out.println(" Checked for expired password accounts at " + LocalDateTime.now());
    }

}
