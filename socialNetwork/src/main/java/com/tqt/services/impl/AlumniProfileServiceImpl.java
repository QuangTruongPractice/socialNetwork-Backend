/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.pojo.AlumniProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.AlumniProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.AlumniProfileService;
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
public class AlumniProfileServiceImpl implements AlumniProfileService {

    @Autowired
    private AlumniProfileRepository alumniRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public AlumniProfile getAlumniProfileById(int id) {
        return this.alumniRepo.getAlumniProfileById(id);
    }

    @Override
    public AlumniProfile getAlumniProfileByUserId(int id) {
        return this.alumniRepo.getAlumniProfileByUserId(id);
    }

    @Override
    public AlumniProfile addAlumniProfile(Map<String, String> params, MultipartFile coverImage, Integer userId) {
        AlumniProfile p = new AlumniProfile();
        User u = this.userRepo.getUserById(userId);
        if (u != null) {
            p.setUser(u);
        } else {
            throw new IllegalArgumentException("Invalid userId: " + userId);
        }
        p.setGraduationYear(Integer.parseInt(params.get("graduationYear")));
        p.setMajor(params.get("major"));
        p.setFaculty(params.get("faculty"));
        p.setCurrentJob(params.get("currentJob"));
        p.setCompany(params.get("company"));
        p.setDescription(params.get("description"));

        // Upload ảnh nếu có
        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(coverImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                p.setCoverImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Upload avatar failed: " + ex.getMessage());
            }
        }

        return this.alumniRepo.addAlumniProfile(p);
    }

    @Override
    public AlumniProfile updateAlumniProfile(Map<String, String> params, MultipartFile coverImage, Integer userId) {
        User u = this.userRepo.getUserById(userId);
        if (u == null) {
            throw new IllegalArgumentException("Invalid userId: " + userId);
        }
        AlumniProfile p = this.alumniRepo.getAlumniProfileByUserId(userId);
        if (params.get("graduationYear") != null) {
            p.setGraduationYear(Integer.parseInt(params.get("graduationYear")));
        }
        if (params.get("major") != null) {
            p.setMajor(params.get("major"));
        }
        if (params.get("faculty") != null) {
            p.setFaculty(params.get("faculty"));
        }
        if (params.get("currentJob") != null) {
            p.setCurrentJob(params.get("currentJob"));
        }
        if (params.get("company") != null) {
            p.setCompany(params.get("company"));
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
        return this.alumniRepo.updateAlumniProfile(p);
    }

}
