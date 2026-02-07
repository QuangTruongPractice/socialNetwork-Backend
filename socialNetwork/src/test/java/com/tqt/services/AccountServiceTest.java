package com.tqt.services;

import com.tqt.enums.Role;
import com.tqt.pojo.Account;
import com.tqt.repositories.AccountRepository;
import com.tqt.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest extends BaseServiceTest {

    @Mock
    private AccountRepository accRepo;

    @Mock
    private MailService mailService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetAccounts_Success() {
        Map<String, String> params = new HashMap<>();
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(accRepo.getAccounts(params)).thenReturn(accounts);

        List<Account> result = accountService.getAccounts(params);

        assertEquals(2, result.size());
        verify(accRepo).getAccounts(params);
    }

    @Test
    public void testGetTotalPages_Success() {
        Map<String, String> params = new HashMap<>();
        when(accRepo.getTotalPages(params)).thenReturn(5);

        Integer result = accountService.getTotalPages(params);

        assertEquals(5, result);
        verify(accRepo).getTotalPages(params);
    }

    @Test
    public void testGetAccountById_Success() {
        Account account = new Account();
        when(accRepo.getAccountById(1)).thenReturn(account);

        Account result = accountService.getAccountById(1);

        assertEquals(account, result);
        verify(accRepo).getAccountById(1);
    }

    @Test
    public void testGetAccountByUserId_Success() {
        Account account = new Account();
        when(accRepo.getAccountByUserId(1)).thenReturn(account);

        Account result = accountService.getAccountByUserId(1);

        assertEquals(account, result);
        verify(accRepo).getAccountByUserId(1);
    }

    @Test
    public void testGetAccountByGroupId_Success() {
        List<Account> accounts = Arrays.asList(new Account());
        when(accRepo.getAccountByGroupId(1)).thenReturn(accounts);

        List<Account> result = accountService.getAccountByGroupId(1);

        assertEquals(1, result.size());
        verify(accRepo).getAccountByGroupId(1);
    }

    @Test
    public void testGetAccountByEmail_Success() {
        Account account = new Account();
        when(accRepo.getAccountByEmail("test@test.com")).thenReturn(account);

        Account result = accountService.getAccountByEmail("test@test.com");

        assertEquals(account, result);
        verify(accRepo).getAccountByEmail("test@test.com");
    }

    @Test
    public void testGetPendingAccounts_Success() {
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(accRepo.getPendingAccounts()).thenReturn(accounts);

        List<Account> result = accountService.getPendingAccounts();

        assertEquals(2, result.size());
        verify(accRepo).getPendingAccounts();
    }

    @Test
    public void testExistAccountByEmail_Success() {
        when(accRepo.existAccountByEmail("test@test.com")).thenReturn(true);

        boolean result = accountService.existAccountByEmail("test@test.com");

        assertTrue(result);
        verify(accRepo).existAccountByEmail("test@test.com");
    }

    @Test
    public void testAuthenticate_Success() {
        when(accRepo.authenticate("user", "pass")).thenReturn(true);

        boolean result = accountService.authenticate("user", "pass");

        assertTrue(result);
        verify(accRepo).authenticate("user", "pass");
    }

    // Complex method: addOrUpdateAccount - Test all branches
    @Test
    public void testAddOrUpdateAccount_NewAccount_EmailExists_ShouldThrowException() {
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setRole(Role.ALUMNI);

        when(accRepo.existAccountByEmail("test@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> accountService.addOrUpdateAccount(account));
        verify(accRepo, never()).addOrUpdateAccount(any());
    }

    @Test
    public void testAddOrUpdateAccount_NewAccount_LecturerRole_ShouldSetPasswordExpiry() {
        Account account = new Account();
        account.setEmail("lecturer@test.com");
        account.setPassword("rawPassword");
        account.setRole(Role.LECTURER);

        when(accRepo.existAccountByEmail("lecturer@test.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");

        accountService.addOrUpdateAccount(account);

        assertTrue(account.getMustChangePassword());
        assertNotNull(account.getPasswordExpiresAt());
        assertEquals("hashedPassword", account.getPassword());
        verify(accRepo).addOrUpdateAccount(account);
    }

    @Test
    public void testAddOrUpdateAccount_NewAccount_AlumniRole_NoPasswordExpiry() {
        Account account = new Account();
        account.setEmail("alumni@test.com");
        account.setPassword("rawPassword");
        account.setRole(Role.ALUMNI);
        account.setMustChangePassword(false);

        when(accRepo.existAccountByEmail("alumni@test.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");

        accountService.addOrUpdateAccount(account);

        assertNull(account.getPasswordExpiresAt());
        assertEquals("hashedPassword", account.getPassword());
        verify(accRepo).addOrUpdateAccount(account);
    }

    @Test
    public void testAddOrUpdateAccount_NewAccount_AlumniWithMustChangePassword_ShouldSetExpiry() {
        Account account = new Account();
        account.setEmail("alumni@test.com");
        account.setPassword("rawPassword");
        account.setRole(Role.ALUMNI);
        account.setMustChangePassword(true);

        when(accRepo.existAccountByEmail("alumni@test.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");

        accountService.addOrUpdateAccount(account);

        assertNotNull(account.getPasswordExpiresAt());
        assertEquals("hashedPassword", account.getPassword());
        verify(accRepo).addOrUpdateAccount(account);
    }

    @Test
    public void testAddOrUpdateAccount_UpdateAccount_EmailChanged_AlreadyExists_ShouldThrowException() {
        Account existing = new Account();
        existing.setId(1);
        existing.setEmail("old@test.com");
        existing.setPassword("hashedOld");

        Account updated = new Account();
        updated.setId(1);
        updated.setEmail("new@test.com");
        updated.setRole(Role.ALUMNI);

        when(accRepo.getAccountById(1)).thenReturn(existing);
        when(accRepo.existAccountByEmail("new@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> accountService.addOrUpdateAccount(updated));
        verify(accRepo, never()).addOrUpdateAccount(any());
    }

    @Test
    public void testAddOrUpdateAccount_UpdateAccount_SameEmail_PasswordNotChanged() {
        Account existing = new Account();
        existing.setId(1);
        existing.setEmail("test@test.com");
        existing.setPassword("hashedPassword");

        Account updated = new Account();
        updated.setId(1);
        updated.setEmail("test@test.com");
        updated.setPassword("hashedPassword"); // Same hashed password
        updated.setRole(Role.ALUMNI);
        updated.setMustChangePassword(false);

        when(accRepo.getAccountById(1)).thenReturn(existing);

        accountService.addOrUpdateAccount(updated);

        // Password should NOT be re-encoded
        assertEquals("hashedPassword", updated.getPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(accRepo).addOrUpdateAccount(updated);
    }

    @Test
    public void testAddOrUpdateAccount_UpdateAccount_PasswordChanged_ShouldEncode() {
        Account existing = new Account();
        existing.setId(1);
        existing.setEmail("test@test.com");
        existing.setPassword("oldHashedPassword");

        Account updated = new Account();
        updated.setId(1);
        updated.setEmail("test@test.com");
        updated.setPassword("newRawPassword");
        updated.setRole(Role.ALUMNI);
        updated.setMustChangePassword(false);

        when(accRepo.getAccountById(1)).thenReturn(existing);
        when(passwordEncoder.matches("newRawPassword", "oldHashedPassword")).thenReturn(false);
        when(passwordEncoder.encode("newRawPassword")).thenReturn("newHashedPassword");

        accountService.addOrUpdateAccount(updated);

        assertEquals("newHashedPassword", updated.getPassword());
        verify(passwordEncoder).encode("newRawPassword");
        verify(accRepo).addOrUpdateAccount(updated);
    }

    @Test
    public void testAddOrUpdateAccount_UpdateAccount_SameRawPassword_ShouldNotEncode() {
        Account existing = new Account();
        existing.setId(1);
        existing.setEmail("test@test.com");
        existing.setPassword("hashedPassword");

        Account updated = new Account();
        updated.setId(1);
        updated.setEmail("test@test.com");
        updated.setPassword("rawPassword");
        updated.setRole(Role.ALUMNI);
        updated.setMustChangePassword(false);

        when(accRepo.getAccountById(1)).thenReturn(existing);
        when(passwordEncoder.matches("rawPassword", "hashedPassword")).thenReturn(true);

        accountService.addOrUpdateAccount(updated);

        // Should keep the raw password (which matches), not re-encode
        assertEquals("rawPassword", updated.getPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(accRepo).addOrUpdateAccount(updated);
    }

    // Complex method: loadUserByUsername - Test both branches
    @Test
    public void testLoadUserByUsername_Success() {
        Account account = new Account();
        account.setEmail("user@test.com");
        account.setPassword("hashedPassword");
        account.setRole(Role.ADMIN);

        when(accRepo.getAccountByEmail("user@test.com")).thenReturn(account);

        UserDetails result = accountService.loadUserByUsername("user@test.com");

        assertEquals("user@test.com", result.getUsername());
        assertEquals("hashedPassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(accRepo).getAccountByEmail("user@test.com");
    }

    @Test
    public void testLoadUserByUsername_NotFound_ShouldThrowException() {
        when(accRepo.getAccountByEmail("notfound@test.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> accountService.loadUserByUsername("notfound@test.com"));
        verify(accRepo).getAccountByEmail("notfound@test.com");
    }

    // Complex method: approveAccount - Test mail success and failure
    @Test
    public void testApproveAccount_MailSuccess() throws Exception {
        Account account = new Account();
        account.setEmail("pending@test.com");

        when(accRepo.getAccountById(1)).thenReturn(account);
        doNothing().when(mailService).sendSimpleMessage(anyString(), anyString(), anyString());

        accountService.approveAccount(1);

        assertTrue(account.getIsVerified());
        assertTrue(account.getIsActive());
        verify(accRepo).addOrUpdateAccount(account);
        verify(mailService).sendSimpleMessage(eq("pending@test.com"), anyString(), anyString());
    }

    @Test
    public void testApproveAccount_MailFailure_ShouldNotAffectApproval() throws Exception {
        Account account = new Account();
        account.setEmail("pending@test.com");

        when(accRepo.getAccountById(1)).thenReturn(account);
        doThrow(new RuntimeException("Mail server down"))
                .when(mailService).sendSimpleMessage(anyString(), anyString(), anyString());

        // Should not throw exception
        accountService.approveAccount(1);

        // Account should still be approved despite mail failure
        assertTrue(account.getIsVerified());
        assertTrue(account.getIsActive());
        verify(accRepo).addOrUpdateAccount(account);
        verify(mailService).sendSimpleMessage(eq("pending@test.com"), anyString(), anyString());
    }
}
