/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.Account;
import com.tqt.repositories.AccountRepository;
import com.tqt.services.AccountService;
import com.tqt.services.MailService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAccounts(Map<String, String> params) {
        return this.accRepo.getAccounts(params);
    }

    public Integer getTotalPages(Map<String, String> params) {
        return this.accRepo.getTotalPages(params);
    }

    @Override
    public Account getAccountById(int id) {
        return this.accRepo.getAccountById(id);
    }

    @Override
    public Account getAccountByUserId(int id) {
        return this.accRepo.getAccountByUserId(id);
    }

    @Override
    public List<Account> getAccountByGroupId(int id) {
        return this.accRepo.getAccountByGroupId(id);
    }

    @Override
    public void addOrUpdateAccount(Account account) {
        boolean isNew = (account.getId() == null);

        if (isNew) {
            if (accRepo.existAccountByEmail(account.getEmail())) {
                throw new RuntimeException("Email đã tồn tại, vui lòng chọn email khác");
            }
        } else {
            Account existing = accRepo.getAccountById(account.getId());
            if (!existing.getEmail().equals(account.getEmail())
                    && accRepo.existAccountByEmail(account.getEmail())) {
                throw new RuntimeException("Email đã tồn tại, vui lòng chọn email khác");
            }
        }
        if ("LECTURER".equals(account.getRole().name())) {
            account.setMustChangePassword(true);
            account.setPasswordExpiresAt(LocalDateTime.now().plusHours(24));
        } else {
            if (Boolean.TRUE.equals(account.getMustChangePassword())) {
                account.setPasswordExpiresAt(LocalDateTime.now().plusHours(24));
            } else {
                account.setPasswordExpiresAt(null);
            }
        }

        if (account.getId() == null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        } else {
            Account existing = accRepo.getAccountById(account.getId());
            if (!existing.getPassword().equals(account.getPassword())) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
            }
        }
        this.accRepo.addOrUpdateAccount(account);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return this.accRepo.getAccountByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = this.accRepo.getAccountByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Email không tồn tại: " + email);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + account.getRole()));

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                authorities
        );
    }

    @Override
    public List<Account> getPendingAccounts() {
        return this.accRepo.getPendingAccounts();
    }

    public void approveAccount(int id) {
        Account acc = this.accRepo.getAccountById(id);
        acc.setIsVerified(true);
        acc.setIsActive(true);
        this.accRepo.addOrUpdateAccount(acc);
        String subject = "Tài khoản của bạn đã được duyệt";
        String content = "Xin chào " + acc.getEmail() + ","
                + "Tài khoản của bạn đã được admin phê duyệt thành công."
                + "Bạn có thể đăng nhập và sử dụng hệ thống."
                + "Trân trọng.";
        try {
            this.mailService.sendSimpleMessage(acc.getEmail(), subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existAccountByEmail(String email) {
        return this.accRepo.existAccountByEmail(email);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.accRepo.authenticate(username, password);
    }

}
