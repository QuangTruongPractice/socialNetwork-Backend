/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.dto;

import com.tqt.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Quang Truong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Integer id;
    private String content;
    private String createdDate;
    private String updatedDate;
    private Integer userId;
    private String fullname;
    private String avatar;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate().toString();
        this.updatedDate = comment.getUpdatedDate() != null ? comment.getUpdatedDate().toString() : null;
        this.userId = comment.getUser().getId();
        this.fullname =  comment.getUser().getFirstName() + " " + comment.getUser().getLastName();
        this.avatar = comment.getUser().getAvatar();
    }
}
