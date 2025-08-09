/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.LecturerProfile;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
public interface LecturerProfileService {
    LecturerProfile getLecturerProfileById(int id);
    LecturerProfile getLecturerProfileByUserId(int id);
    LecturerProfile addLecturerProfile(Map<String, String> params, MultipartFile coverImage, Integer userId);
    LecturerProfile updateLecturerProfile(Map<String, String> params, MultipartFile coverImage, Integer userId);
}
