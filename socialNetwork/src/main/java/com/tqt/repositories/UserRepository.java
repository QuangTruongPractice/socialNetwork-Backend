/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Quang Truong
 */
public interface UserRepository {
    List<User> getUsers(Map<String, String> params);
    Integer getTotalPages(Map<String, String> params);
    User getUserById(int id);
    boolean existUserByUserCode(String code);
    void addOrUpdateUser(User user);
    List<User> findUsersWithoutAccount();
    List<User> findAllUserById(List<Integer> ids);
}
