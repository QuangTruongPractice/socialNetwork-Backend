/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.pojo.AdminProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.AdminProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.AdminProfileService;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@Service
public class AdminProfileServiceImpl implements AdminProfileService{
    @Autowired
    private AdminProfileRepository adminRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public AdminProfile getAdminProfileById(int id) {
        return this.adminRepo.getAdminProfileById(id);
    }
    
    @Override
    public AdminProfile getAdminProfileByUserId(int id) {
        return this.adminRepo.getAdminProfileByUserId(id);
    }

    @Override
    public AdminProfile addAdminProfile(Map<String, String> params, MultipartFile coverImage, Integer userId) {
        AdminProfile p = new AdminProfile();
        User u = this.userRepo.getUserById(userId);
        if (u != null) {
            p.setUser(u);
        } 
        p.setPosition(params.get("position"));
        p.setDescription(params.get("description"));

        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(coverImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                p.setCoverImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Upload avatar failed: " + ex.getMessage());
            }
        }

        return this.adminRepo.addAdminProfile(p);
    }

    @Override
    public AdminProfile updateAdminProfile(Map<String, String> params, MultipartFile coverImage, Integer userId) {
        AdminProfile p = this.adminRepo.getAdminProfileByUserId(userId);

        if (params.get("position") != null) {
            p.setPosition(params.get("position"));
        }
        if (params.get("description") != null) {
            p.setDescription(params.get("description"));
        }

        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(coverImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                p.setCoverImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Upload cover image failed: " + ex.getMessage());
            }
        }
        return this.adminRepo.updateAdminProfile(p);
    }
}
