package com.tqt.components;

import com.tqt.pojo.Account;
import com.tqt.repositories.AccountRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AccountScheduler {

    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(cron = "0 */5 * * * *") // mỗi giờ một lần
    public void disableExpiredAccounts() {
        System.out.println("⏰ [Scheduler] Bắt đầu kiểm tra tài khoản lúc " + LocalDateTime.now());
        List<Account> accounts = accountRepository.getAccounts(new HashMap<>());
        for (Account acc : accounts) {
            if (acc.getMustChangePassword()
                    && acc.getPasswordExpiresAt() != null
                    && LocalDateTime.now().isAfter(acc.getPasswordExpiresAt())) {

                acc.setIsActive(false); // Khóa tài khoản
                accountRepository.addOrUpdateAccount(acc);
            }
        }
        System.out.println("✅ Checked for expired password accounts at " + LocalDateTime.now());
    }


}
