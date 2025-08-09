/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.dto;

import com.tqt.pojo.Reaction;
import java.time.LocalDate;
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
public class ReactDTO {
    private Integer id;
    private String reactionType;
    private Integer userId;
    private Integer postId;
    private LocalDate createdDate;

    public ReactDTO(Reaction r) {
        this.id = r.getId();
        this.reactionType = r.getReactionType().toString();
        this.userId = r.getUser().getId();
        this.postId = r.getPost().getId();
        this.createdDate = r.getCreatedDate();
    }
}