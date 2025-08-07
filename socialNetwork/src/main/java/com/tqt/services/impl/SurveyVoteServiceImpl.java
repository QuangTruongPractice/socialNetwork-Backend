/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.SurveyVote;
import com.tqt.repositories.SurveyVoteRepository;
import com.tqt.services.SurveyVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class SurveyVoteServiceImpl implements SurveyVoteService{
    
    @Autowired
    private SurveyVoteRepository svRepo;

    @Override
    public boolean existsByUserIdAndPostId(int userId, int postId) {
        return this.svRepo.existsByUserIdAndPostId(userId, postId);
    }

    @Override
    public void addOrUpdateSurveyVote(SurveyVote sv) {
        this.svRepo.addOrUpdateSurveyVote(sv);
    }
    
}
