/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.AlumniProfile;

/**
 *
 * @author Quang Truong
 */
public interface AlumniProfileRepository {
    AlumniProfile getAlumniProfileById(int id);
    AlumniProfile getAlumniProfileByUserId(int id);
    AlumniProfile addAlumniProfile(AlumniProfile p);
    AlumniProfile updateAlumniProfile(AlumniProfile p);
}
