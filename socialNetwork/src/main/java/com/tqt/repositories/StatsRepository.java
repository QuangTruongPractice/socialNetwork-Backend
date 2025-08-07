/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import java.util.List;

/**
 *
 * @author Quang Truong
 */
public interface StatsRepository {
    List<Object[]> getUserStatsByYear();                 
    List<Object[]> getUserStatsByYear(int year);      
    List<Object[]> getUserStatsByQuarter(int year, int quarter);
    List<Object[]> getUserStatsByMonth(int year, int month);  

    // Thống kê Post
    List<Object[]> getPostStatsByYear();       
    List<Object[]> getPostStatsByYear(int year);   
    List<Object[]> getPostStatsByQuarter(int year, int quarter); 
    List<Object[]> getPostStatsByMonth(int year, int month);
}
