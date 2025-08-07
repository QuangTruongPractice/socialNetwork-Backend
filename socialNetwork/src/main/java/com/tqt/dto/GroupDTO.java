/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.dto;

import com.tqt.pojo.Group;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class GroupDTO {

    private Integer id;
    private String name;
    private LocalDate createdAt;
    private int memberCount;
    private List<String> memberNames;
    
    public GroupDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.createdAt = group.getCreatedDate();

        if (group.getMembers() != null) {
            this.memberCount = group.getMembers().size();
            this.memberNames = group.getMembers().stream()
                .map(u -> u.getLastName() + " " + u.getFirstName())
                .collect(Collectors.toList());
        } else {
            this.memberCount = 0;
            this.memberNames = new ArrayList<>();
        }
    }
}
