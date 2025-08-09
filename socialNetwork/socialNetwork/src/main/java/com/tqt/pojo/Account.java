/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.tqt.enums.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Quang Truong
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank(message = "Email không được để trống")
    private String email;
    
    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Mật khẩu không được quá ngắn")
    private String password;
    
    @Column(name = "is_active")
    private Boolean isActive = false;
    
    @Column(name = "must_change_password")
    private Boolean mustChangePassword;
    
    @Column(name = "password_expires_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passwordExpiresAt;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ALUMNI;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    private LocalDate createdAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }
}