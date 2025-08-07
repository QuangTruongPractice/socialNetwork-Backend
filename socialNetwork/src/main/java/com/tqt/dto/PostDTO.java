/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.dto;

import com.tqt.pojo.Post;
import com.tqt.pojo.Reaction;
import com.tqt.pojo.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Quang Truong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Integer id;
    private String content;
    private String image;
    private String createdAt;
    private Integer authorId;
    private String authorFullname;
    private String authorAvatar;
    private Boolean isLocked = false;
    private List<SurveyOptionDTO> surveyOptions;
    private Map<String, Long> reactions; 
    private String userReaction;
    private int totalReacts;
    

    public PostDTO(Post p, int totalReacts) {
        this.id = p.getId();
        this.content = p.getContent();
        this.image = p.getImage();
        this.createdAt = p.getCreatedAt().toString();
        this.authorId = p.getUser().getId();
        this.authorFullname = p.getUser() != null ? p.getUser().getFirstName() + " " + p.getUser().getLastName() : null;
        this.authorAvatar = p.getUser().getAvatar();
        if (p.getSurveyOptions() != null) {
            this.surveyOptions = p.getSurveyOptions().stream()
                    .map(SurveyOptionDTO::new)
                    .collect(Collectors.toList());
        }
        this.totalReacts = totalReacts;
        this.isLocked = p.getIsLocked();
    }
    public void setReactionsForPost(List<Reaction> reactions, User currentUser) {
        this.reactions = reactions.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getReactionType().name(),
                        Collectors.counting()
                ));
        this.userReaction = reactions.stream()
                .filter(r -> r.getUser().getId().equals(currentUser.getId()))
                .map(r -> r.getReactionType().name())
                .findFirst()
                .orElse(null);
    }
    
}
