/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.AlumniProfile;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
public interface AlumniProfileService {
    AlumniProfile getAlumniProfileById(int id);
    AlumniProfile getAlumniProfileByUserId(int id);
    AlumniProfile addAlumniProfile(Map<String, String> params, MultipartFile coverImage, Integer userId);
    AlumniProfile updateAlumniProfile(Map<String, String> params, MultipartFile coverImage, Integer userId);
}
