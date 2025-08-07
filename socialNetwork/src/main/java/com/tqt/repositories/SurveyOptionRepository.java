/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.SurveyOption;

/**
 *
 * @author Quang Truong
 */
public interface SurveyOptionRepository {
    SurveyOption getSurveyOptionById(int id);
    void addOrUpdateSurveyOption(SurveyOption so);
}
