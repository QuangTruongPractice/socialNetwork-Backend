package com.tqt.components;

import com.tqt.pojo.Account;
import com.tqt.services.AccountService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AccountScheduler {

    @Autowired
    private AccountService accountService;

    @Scheduled(cron = "0 */5 * * * *")
    public void disableExpiredAccounts() {
        List<Account> accounts = this.accountService.getAccounts(new HashMap<>());
        for (Account acc : accounts) {
            if (acc.getMustChangePassword() && acc.getPasswordExpiresAt() != null && LocalDateTime.now().isAfter(acc.getPasswordExpiresAt())) {
                acc.setIsActive(false);
                this.accountService.addOrUpdateAccount(acc);
            }
        }
        System.out.println(" Checked for expired password accounts at " + LocalDateTime.now());
    }


}
