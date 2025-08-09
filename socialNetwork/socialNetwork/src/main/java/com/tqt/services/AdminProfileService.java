/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.AdminProfile;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
public interface AdminProfileService {
    AdminProfile getAdminProfileById(int id);
    AdminProfile getAdminProfileByUserId(int id);
    AdminProfile addAdminProfile(Map<String, String> params, MultipartFile coverImage, Integer userId);
    AdminProfile updateAdminProfile(Map<String, String> params, MultipartFile coverImage, Integer userId);
}
