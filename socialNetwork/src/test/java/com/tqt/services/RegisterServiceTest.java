package com.tqt.services;

import com.tqt.pojo.Account;
import com.tqt.repositories.AccountRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.RegisterServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterServiceTest extends BaseServiceTest {

    @Mock
    private AccountRepository accRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterServiceImpl registerService;

    @Test
    public void testRegister_EmailAlreadyExists_ShouldThrowException() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "existing@example.com");
        params.put("userCode", "12345");

        when(userRepo.existUserByUserCode("12345")).thenReturn(false);
        when(accRepo.existAccountByEmail("existing@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> registerService.register(params, null));
        verify(accRepo, never()).addOrUpdateAccount(any());
    }

    @Test
    public void testChangePassword_IncorrectCurrentPassword_ShouldReturnError() {
        Account account = new Account();
        account.setEmail("test@test.com");

        Map<String, String> body = new HashMap<>();
        body.put("currentPassword", "wrong");
        body.put("password", "new");

        when(accRepo.authenticate("test@test.com", "wrong")).thenReturn(false);

        String result = registerService.changePassword(body, account);
        assertEquals("Mật khẩu hiện tại không đúng", result);
        verify(accRepo, never()).addOrUpdateAccount(any());
    }
}
