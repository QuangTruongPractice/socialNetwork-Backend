/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.dto;

import com.tqt.pojo.SurveyOption;
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
public class SurveyOptionDTO {
    private Integer id;
    private String text;
    private int voteCount;

    public SurveyOptionDTO(SurveyOption option) {
        this.id = option.getId();
        this.text = option.getOptionText();
        this.voteCount = option.getVoteCount();
    }
}
