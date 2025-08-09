/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.SurveyOption;
import com.tqt.repositories.SurveyOptionRepository;
import com.tqt.services.SurveyOptionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Quang Truong
 */
public class SurveyOptionServiceImpl implements SurveyOptionService{
    
    @Autowired
    private SurveyOptionRepository soRepo;

    @Override
    public SurveyOption getSurveyOptionById(int id) {
        return this.soRepo.getSurveyOptionById(id);
    }

    @Override
    public void addOrUpdateSurveyOption(SurveyOption so) {
        this.soRepo.addOrUpdateSurveyOption(so);
    }
    
}
