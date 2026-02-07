/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.dto.AccountDTO;
import com.tqt.dto.RegisterDTO;
import com.tqt.dto.UserDTO;
import com.tqt.enums.Gender;
import com.tqt.enums.Role;
import com.tqt.pojo.Account;
import com.tqt.pojo.User;
import com.tqt.repositories.AccountRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.RegisterService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterDTO register(Map<String, String> params, MultipartFile avatar) {
        User u = new User();
        u.setFirstName(params.get("firstName"));
        u.setLastName(params.get("lastName"));
        u.setDob(params.get("dob") != null ? LocalDate.parse(params.get("dob")) : null);
        u.setGender(params.get("gender") != null ? Gender.valueOf(params.get("gender").toUpperCase()) : Gender.OTHER);
        if (this.userRepo.existUserByUserCode(params.get("userCode")))
            throw new RuntimeException("MSSV đã tồn tại");
        else
            u.setUserCode(params.get("userCode"));
        u.setPhone(params.get("phone"));

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Upload avatar failed: " + ex.getMessage());
            }
        }
        userRepo.addOrUpdateUser(u);

        Account acc = new Account();
        if (this.accRepo.existAccountByEmail(params.get("email")))
            throw new RuntimeException("Email đã tồn tại");
        else
            acc.setEmail(params.get("email"));
        acc.setPassword(passwordEncoder.encode(params.get("password")));
        acc.setIsActive(false);
        acc.setIsVerified(false);
        acc.setMustChangePassword(false);
        acc.setUser(u);
        acc.setRole(Role.ALUMNI);

        accRepo.addOrUpdateAccount(acc);

        UserDTO userDTO = new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), u.getUserCode(), u.getPhone(),
                u.getAvatar());
        AccountDTO accDTO = new AccountDTO(acc.getId(), acc.getEmail(), acc.getIsActive(), acc.getIsVerified(),
                acc.getMustChangePassword(), acc.getRole());

        return new RegisterDTO(userDTO, accDTO);
    }

    @Override
    public String changePassword(Map<String, String> body, Account acc) {
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("password");
        if (!this.accRepo.authenticate(acc.getEmail(), currentPassword)) {
            return ("Mật khẩu hiện tại không đúng");
        }
        acc.setPassword(passwordEncoder.encode(newPassword));
        acc.setMustChangePassword(false);
        acc.setPasswordExpiresAt(null);
        this.accRepo.addOrUpdateAccount(acc);
        return ("Đổi mật khẩu thành công");
    }

}
