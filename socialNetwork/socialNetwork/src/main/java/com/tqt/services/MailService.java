/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services;

import com.tqt.dto.PostDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Group;
import com.tqt.repositories.AccountRepository;
import com.tqt.repositories.GroupRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 *
 * @author Quang Truong
 */
@Service
@EnableAsync
public class MailService {

    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private AccountRepository accRepo;
    

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("qkhanh1632@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    @Async
    public void sendInvitationMailToAll(PostDTO post) {
        String subject = "You've received an invitation!";
        String content = "Content: " + post.getContent() + "\n"
                + "From: " + post.getAuthorFullname();

        List<Account> allAccounts = accRepo.getAccounts(null);

        for (Account account : allAccounts) {
            if (account.getEmail() != null && !account.getEmail().isEmpty()) {
                sendSimpleMessage(account.getEmail(), subject, content);
            }
        }
    }
    @Async
    public void sendInvitationMailToGroup(PostDTO post, Group group) {
        String subject = "You've received an invitation!";
        String content = "Content: " + post.getContent() + "\n"
                + "From: " + post.getAuthorFullname();

        List<Account> allGroups = accRepo.getAccountByGroupId(group.getId());

        for (Account account : allGroups) {
            sendSimpleMessage(account.getEmail(), subject, content);
        }
    }
    @Async
    public void sendInvitationMailToAccount(PostDTO post, Account acc) {
        String subject = "You've received an invitation!";
        String content = "Content: " + post.getContent() + "\n"
                + "From: " + post.getAuthorFullname();

        sendSimpleMessage(acc.getEmail(), subject, content);

    }
}
