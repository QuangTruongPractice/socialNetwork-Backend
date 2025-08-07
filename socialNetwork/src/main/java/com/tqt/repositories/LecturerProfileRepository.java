/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.LecturerProfile;

/**
 *
 * @author Quang Truong
 */
public interface LecturerProfileRepository {
    LecturerProfile getLecturerProfileById(int id);
    LecturerProfile getLecturerProfileByUserId(int id);
    LecturerProfile addLecturerProfile(LecturerProfile p);
    LecturerProfile updateLecturerProfile(LecturerProfile p);
}
