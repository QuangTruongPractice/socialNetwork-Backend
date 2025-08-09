/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@Entity
@Table(name = "alumni_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    private String major;

    private String faculty;

    @Column(name = "current_job")
    private String currentJob;

    private String company;
    
    private String description;

    @Column(name = "cover_image")
    private String coverImage;
    
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;
    
    @Transient
    @JsonIgnore
    private MultipartFile file;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

}