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
import jakarta.persistence.ManyToOne;
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
@Table(name = "post_recipient")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class PostRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Nếu chỉ định một nhóm
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    // Nếu gửi cho tất cả, set cờ isToAll = true
    @Column(name = "is_to_all")
    private Boolean isToAll = false;
}
