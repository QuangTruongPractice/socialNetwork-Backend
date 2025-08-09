/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.services;

import com.tqt.pojo.SurveyVote;

/**
 *
 * @author Quang Truong
 */
public interface SurveyVoteService {
    boolean existsByUserIdAndPostId(int userId, int postId);
    void addOrUpdateSurveyVote(SurveyVote sv);
}
