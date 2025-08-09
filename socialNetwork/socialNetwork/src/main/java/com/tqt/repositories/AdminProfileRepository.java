/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.AdminProfile;

/**
 *
 * @author Quang Truong
 */
public interface AdminProfileRepository {
    AdminProfile getAdminProfileById(int id);
    AdminProfile getAdminProfileByUserId(int id);
    AdminProfile addAdminProfile(AdminProfile p);
    AdminProfile updateAdminProfile(AdminProfile p);
}
