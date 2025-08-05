/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Quang Truong
 */
public interface UserService{
    List<User> getUsers(Map<String, String> params);
    User getUserById(int id);
    void addOrUpdateUser(User user);
    List<User> findUsersWithoutAccount();
}
