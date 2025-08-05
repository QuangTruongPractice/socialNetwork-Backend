/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Quang Truong
 */
@Entity
@Table(name = "lecturer_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LecturerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    private String position;         // Chức vụ: Giảng viên, Trưởng bộ môn, Phó khoa...
    
    private String degree;           // Học vị: Thạc sĩ, Tiến sĩ...

    private String specialization;   // Lĩnh vực chuyên môn
    
    private String faculty;

    private String description; 

    @Column(name = "cover_image")
    private String coverImage;
}