/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.dto.RegisterDTO;
import com.tqt.pojo.Account;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
public interface RegisterService {
    RegisterDTO register(Map<String, String> params, MultipartFile avatar);
    String changePassword(Map<String, String> body, Account acc);
}
