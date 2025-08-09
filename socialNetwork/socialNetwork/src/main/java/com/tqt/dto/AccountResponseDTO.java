/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.dto;

import com.tqt.pojo.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Quang Truong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Integer id;
    private String email;
    private String fullname;
    private String avatar;
    
    public AccountResponseDTO(Account a) {
        this.id = a.getId();
        this.email = a.getEmail();
        this.fullname = a.getUser() != null ? a.getUser().getFirstName() + " " + a.getUser().getLastName() : null;
        this.avatar = a.getUser().getAvatar();
    }
}
