package com.tqt.services;

import com.tqt.dto.PostDTO;
import com.tqt.pojo.Account;
import com.tqt.pojo.Group;
import com.tqt.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MailServiceTest extends BaseServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private AccountRepository accRepo;

    @InjectMocks
    private MailService mailService;

    // sendSimpleMessage - Basic success test
    @Test
    public void testSendSimpleMessage_Success() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        mailService.sendSimpleMessage(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("qkhanh1632@gmail.com", sentMessage.getFrom());
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());
    }

    // sendInvitationMailToAll tests - Test conditional (line 52) and empty list
    @Test
    public void testSendInvitationMailToAll_EmptyAccountList_ShouldNotSendMail() {
        PostDTO post = new PostDTO();
        post.setContent("Event invitation");
        post.setAuthorFullname("Admin User");

        when(accRepo.getAccounts(null)).thenReturn(Collections.emptyList());

        mailService.sendInvitationMailToAll(post);

        verify(accRepo).getAccounts(null);
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendInvitationMailToAll_AccountsWithValidEmails_ShouldSendMails() {
        PostDTO post = new PostDTO();
        post.setContent("Event invitation");
        post.setAuthorFullname("Admin User");

        Account acc1 = new Account();
        acc1.setEmail("user1@example.com");

        Account acc2 = new Account();
        acc2.setEmail("user2@example.com");

        List<Account> accounts = Arrays.asList(acc1, acc2);
        when(accRepo.getAccounts(null)).thenReturn(accounts);
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        mailService.sendInvitationMailToAll(post);

        verify(accRepo).getAccounts(null);
        verify(emailSender, times(2)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendInvitationMailToAll_AccountsWithNullOrEmptyEmails_ShouldSkipThem() {
        PostDTO post = new PostDTO();
        post.setContent("Event invitation");
        post.setAuthorFullname("Admin User");

        Account acc1 = new Account();
        acc1.setEmail("user1@example.com");

        Account acc2 = new Account();
        acc2.setEmail(null); // Null email

        Account acc3 = new Account();
        acc3.setEmail(""); // Empty email

        Account acc4 = new Account();
        acc4.setEmail("user4@example.com");

        List<Account> accounts = Arrays.asList(acc1, acc2, acc3, acc4);
        when(accRepo.getAccounts(null)).thenReturn(accounts);
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        mailService.sendInvitationMailToAll(post);

        verify(accRepo).getAccounts(null);
        // Should only send to acc1 and acc4 (2 emails)
        verify(emailSender, times(2)).send(any(SimpleMailMessage.class));
    }

    // sendInvitationMailToGroup tests
    @Test
    public void testSendInvitationMailToGroup_Success() {
        PostDTO post = new PostDTO();
        post.setContent("Group invitation");
        post.setAuthorFullname("Admin User");

        Group group = new Group();
        group.setId(1);
        group.setName("Test Group");

        Account acc1 = new Account();
        acc1.setEmail("member1@example.com");

        Account acc2 = new Account();
        acc2.setEmail("member2@example.com");

        List<Account> groupMembers = Arrays.asList(acc1, acc2);
        when(accRepo.getAccountByGroupId(1)).thenReturn(groupMembers);
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        mailService.sendInvitationMailToGroup(post, group);

        verify(accRepo).getAccountByGroupId(1);
        verify(emailSender, times(2)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendInvitationMailToGroup_EmptyGroup_ShouldNotSendMail() {
        PostDTO post = new PostDTO();
        post.setContent("Group invitation");
        post.setAuthorFullname("Admin User");

        Group group = new Group();
        group.setId(1);

        when(accRepo.getAccountByGroupId(1)).thenReturn(Collections.emptyList());

        mailService.sendInvitationMailToGroup(post, group);

        verify(accRepo).getAccountByGroupId(1);
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    // sendInvitationMailToAccount tests
    @Test
    public void testSendInvitationMailToAccount_Success() {
        PostDTO post = new PostDTO();
        post.setContent("Personal invitation");
        post.setAuthorFullname("Admin User");

        Account acc = new Account();
        acc.setEmail("recipient@example.com");

        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        mailService.sendInvitationMailToAccount(post, acc);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("qkhanh1632@gmail.com", sentMessage.getFrom());
        assertEquals("recipient@example.com", sentMessage.getTo()[0]);
        assertEquals("You've received an invitation!", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Personal invitation"));
        assertTrue(sentMessage.getText().contains("Admin User"));
    }

    @Test
    public void testSendInvitationMailToAccount_MessageContentFormat() {
        PostDTO post = new PostDTO();
        post.setContent("Test event description");
        post.setAuthorFullname("John Doe");

        Account acc = new Account();
        acc.setEmail("test@example.com");

        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        mailService.sendInvitationMailToAccount(post, acc);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        String expectedContent = "Content: Test event description\nFrom: John Doe";
        assertEquals(expectedContent, sentMessage.getText());
    }
}
