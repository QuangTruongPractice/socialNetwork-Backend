/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.enums.Gender;
import com.tqt.pojo.AdminProfile;
import com.tqt.pojo.AlumniProfile;
import com.tqt.pojo.LecturerProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.AdminProfileRepository;
import com.tqt.repositories.AlumniProfileRepository;
import com.tqt.repositories.LecturerProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.UserService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AdminProfileRepository adminRepo;

    @Autowired
    private AlumniProfileRepository alumniRepo;

    @Autowired
    private LecturerProfileRepository lecturerRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public Integer getTotalPages(Map<String, String> params) {
        return this.userRepo.getTotalPages(params);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepo.getUserById(id);
    }
    
    @Override
    public User getUserByUserCode(String code) {
        return this.userRepo.getUserByUserCode(code);
    }

    @Override
    public boolean existUserByUserCode(String code) {
        return this.userRepo.existUserByUserCode(code);
    }

    @Override
    public void addOrUpdateUser(User user) {
        if(this.userRepo.existUserByUserCode(user.getUserCode()))
            throw new RuntimeException("MSSV đã tồn tại");
        if (!user.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(user.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                user.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.userRepo.addOrUpdateUser(user);
    }

    @Override
    public List<User> findUsersWithoutAccount() {
        return this.userRepo.findUsersWithoutAccount();
    }

    @Override
    public List<User> findAllUserById(List<Integer> ids) {
        return this.userRepo.findAllUserById(ids);
    }

    public Object getProfileByUserId(int userId) {
        AdminProfile admin = adminRepo.getAdminProfileByUserId(userId);
        if (admin != null) {
            return admin;
        }
        AlumniProfile alumni = alumniRepo.getAlumniProfileByUserId(userId);
        if (alumni != null) {
            return alumni;
        }
        LecturerProfile lecturer = lecturerRepo.getLecturerProfileByUserId(userId);
        if (lecturer != null) {
            return lecturer;
        }
        return null;
    }

    @Override
    public User updateUser(Map<String, String> params, MultipartFile avatar, Integer userId) {
        User u = this.userRepo.getUserById(userId);
        if (params.containsKey("firstName")) {
            u.setFirstName(params.get("firstName"));
        }
        if (params.containsKey("lastName")) {
            u.setLastName(params.get("lastName"));
        }
        if (params.containsKey("dob")) {
            u.setDob(params.get("dob") != null ? LocalDate.parse(params.get("dob")) : null);
        }
        if (params.containsKey("gender")) {
            u.setGender(params.get("gender") != null ? Gender.valueOf(params.get("gender").toUpperCase()) : Gender.OTHER);
        }
        if (params.containsKey("userCode")) {
            String newUserCode = params.get("userCode");
            if (newUserCode != null && !newUserCode.equals(u.getUserCode())) {
                if (this.userRepo.existUserByUserCode(newUserCode)) {
                    throw new RuntimeException("MSSV đã tồn tại");
                } else {
                    u.setUserCode(newUserCode);
                }
            }
        }

        if (params.containsKey("phone")) {
            u.setPhone(params.get("phone"));
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Upload avatar failed: " + ex.getMessage());
            }
        }
        this.userRepo.addOrUpdateUser(u);
        return u;
    }
}
