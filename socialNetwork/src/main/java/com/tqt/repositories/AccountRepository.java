/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.Account;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Quang Truong
 */
public interface AccountRepository {
    List<Account> getAccounts(Map<String, String> params);
    Account getAccountById(int id);
    Account getAccountByEmail(String email);
    void addOrUpdateAccount(Account account);
}
