/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.Account;
import com.tqt.repositories.AccountRepository;
import com.tqt.services.AccountService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accRepo;

    @Override
    public List<Account> getAccounts(Map<String, String> params) {
        return this.accRepo.getAccounts(params);
    }

    @Override
    public Account getAccountById(int id) {
        return this.accRepo.getAccountById(id);
    }

    @Override
    public void addOrUpdateAccount(Account account) {
        this.accRepo.addOrUpdateAccount(account);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return this.accRepo.getAccountByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accRepo.getAccountByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Email không tồn tại: " + email);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + account.getRole()));

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(), // username là email
                account.getPassword(), // password đã mã hóa
                account.getIsActive(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}
