/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.pojo.PostRecipient;
import com.tqt.repositories.PostRecipientRepository;
import com.tqt.services.PostRecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
public class PostRecipientServiceImpl implements PostRecipientService{
    
    @Autowired
    private PostRecipientRepository prRepo;

    @Override
    public void addOrUpdatePostRecipient(PostRecipient pr) {
        this.prRepo.addOrUpdatePostRecipient(pr);
    }
    
    
}
