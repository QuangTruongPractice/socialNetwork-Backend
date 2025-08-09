/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.pojo.LecturerProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.LecturerProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.LecturerProfileService;
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
public class LecturerProfileServiceImpl implements LecturerProfileService{
    @Autowired
    private LecturerProfileRepository lecturerRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public LecturerProfile getLecturerProfileById(int id) {
        return this.lecturerRepo.getLecturerProfileById(id);
    }
    
    @Override
    public LecturerProfile getLecturerProfileByUserId(int id) {
        return this.lecturerRepo.getLecturerProfileByUserId(id);
    }

    @Override
    public LecturerProfile addLecturerProfile(Map<String, String> params, MultipartFile coverImage, Integer userId) {
        LecturerProfile p = new LecturerProfile();
        User u = this.userRepo.getUserById(userId);
        if (u != null) {
            p.setUser(u);
        }

        p.setPosition(params.get("position"));
        p.setDegree(params.get("degree"));
        p.setSpecialization(params.get("specialization"));
        p.setFaculty(params.get("faculty"));
        p.setDescription(params.get("description"));

        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(coverImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                p.setCoverImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Upload avatar failed: " + ex.getMessage());
            }
        }

        return this.lecturerRepo.addLecturerProfile(p);
    }

    @Override
    public LecturerProfile updateLecturerProfile(Map<String, String> params, MultipartFile coverImage, Integer userId) {
        LecturerProfile p = this.lecturerRepo.getLecturerProfileByUserId(userId);
        
        if (params.get("position") != null) {
            p.setPosition(params.get("position"));
        }       
        if (params.get("degree") != null) {
            p.setDegree(params.get("degree"));
        }        
        if (params.get("specialization") != null) {
            p.setSpecialization(params.get("specialization"));
        }
        if (params.get("faculty") != null) {
            p.setFaculty(params.get("faculty"));
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
        return this.lecturerRepo.updateLecturerProfile(p);
    }
}
