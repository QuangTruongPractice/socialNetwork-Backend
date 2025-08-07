/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.repositories.StatsRepository;
import com.tqt.services.StatsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class StatsServiceImpl implements StatsService{
    
    @Autowired
    private StatsRepository statsRepo;

    @Override
    public List<Object[]> getUserStatsByYear() {
        return this.statsRepo.getUserStatsByYear();
    }

    @Override
    public List<Object[]> getUserStatsByYear(int year) {
        return this.statsRepo.getUserStatsByYear(year);
    }

    @Override
    public List<Object[]> getUserStatsByQuarter(int year, int quarter) {
        return this.statsRepo.getUserStatsByQuarter(year, quarter);
    }

    @Override
    public List<Object[]> getUserStatsByMonth(int year, int month) {
        return this.statsRepo.getUserStatsByMonth(year, month);
    }

    @Override
    public List<Object[]> getPostStatsByYear() {
        return this.statsRepo.getPostStatsByYear();
    }

    @Override
    public List<Object[]> getPostStatsByYear(int year) {
        return this.statsRepo.getPostStatsByYear(year);
    }

    @Override
    public List<Object[]> getPostStatsByQuarter(int year, int quarter) {
        return this.statsRepo.getPostStatsByQuarter(year, quarter);
    }

    @Override
    public List<Object[]> getPostStatsByMonth(int year, int month) {
        return this.statsRepo.getPostStatsByMonth(year, month);
    }
    
}
