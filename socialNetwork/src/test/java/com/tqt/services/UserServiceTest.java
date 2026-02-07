package com.tqt.services;

import com.cloudinary.Cloudinary;
import com.tqt.enums.Gender;
import com.tqt.pojo.AdminProfile;
import com.tqt.pojo.AlumniProfile;
import com.tqt.pojo.LecturerProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.AdminProfileRepository;
import com.tqt.repositories.AlumniProfileRepository;
import com.tqt.repositories.LecturerProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private AdminProfileRepository adminRepo;

    @Mock
    private AlumniProfileRepository alumniRepo;

    @Mock
    private LecturerProfileRepository lecturerRepo;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private UserServiceImpl userService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetUsers_Success() {
        Map<String, String> params = new HashMap<>();
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepo.getUsers(params)).thenReturn(users);

        List<User> result = userService.getUsers(params);

        assertEquals(2, result.size());
        verify(userRepo).getUsers(params);
    }

    @Test
    public void testGetTotalPages_Success() {
        Map<String, String> params = new HashMap<>();
        when(userRepo.getTotalPages(params)).thenReturn(10);

        Integer result = userService.getTotalPages(params);

        assertEquals(10, result);
        verify(userRepo).getTotalPages(params);
    }

    @Test
    public void testGetUserById_Success() {
        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        User result = userService.getUserById(1);

        assertEquals(user, result);
        verify(userRepo).getUserById(1);
    }

    @Test
    public void testExistUserByUserCode_Success() {
        when(userRepo.existUserByUserCode("123456")).thenReturn(true);

        boolean result = userService.existUserByUserCode("123456");

        assertTrue(result);
        verify(userRepo).existUserByUserCode("123456");
    }

    @Test
    public void testFindUsersWithoutAccount_Success() {
        List<User> users = Arrays.asList(new User());
        when(userRepo.findUsersWithoutAccount()).thenReturn(users);

        List<User> result = userService.findUsersWithoutAccount();

        assertEquals(1, result.size());
        verify(userRepo).findUsersWithoutAccount();
    }

    @Test
    public void testFindAllUserById_Success() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepo.findAllUserById(ids)).thenReturn(users);

        List<User> result = userService.findAllUserById(ids);

        assertEquals(2, result.size());
        verify(userRepo).findAllUserById(ids);
    }

    // Complex method: addOrUpdateUser - Test all branches
    @Test
    public void testAddOrUpdateUser_NewUser_UserCodeExists_ShouldThrowException() {
        User user = new User();
        user.setUserCode("123456");
        user.setFile(mock(MultipartFile.class));
        when(user.getFile().isEmpty()).thenReturn(true);

        when(userRepo.existUserByUserCode("123456")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.addOrUpdateUser(user));
        verify(userRepo, never()).addOrUpdateUser(any());
    }

    @Test
    public void testAddOrUpdateUser_NewUser_NoFile_Success() {
        User user = new User();
        user.setUserCode("123456");
        user.setFile(mock(MultipartFile.class));
        when(user.getFile().isEmpty()).thenReturn(true);

        when(userRepo.existUserByUserCode("123456")).thenReturn(false);

        userService.addOrUpdateUser(user);

        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testAddOrUpdateUser_NewUser_WithFile_Success() throws Exception {
        User user = new User();
        user.setUserCode("123456");

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[] { 1, 2, 3 });
        user.setFile(file);

        when(userRepo.existUserByUserCode("123456")).thenReturn(false);

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/avatar.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        userService.addOrUpdateUser(user);

        assertEquals("https://cloudinary.com/avatar.jpg", user.getAvatar());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testAddOrUpdateUser_UpdateUser_UserCodeChanged_AlreadyExists_ShouldThrowException() {
        User existing = new User();
        existing.setId(1);
        existing.setUserCode("123456");

        User updated = new User();
        updated.setId(1);
        updated.setUserCode("654321");
        updated.setFile(mock(MultipartFile.class));
        when(updated.getFile().isEmpty()).thenReturn(true);

        when(userRepo.getUserById(1)).thenReturn(existing);
        when(userRepo.existUserByUserCode("654321")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.addOrUpdateUser(updated));
        verify(userRepo, never()).addOrUpdateUser(any());
    }

    @Test
    public void testAddOrUpdateUser_UpdateUser_SameUserCode_Success() {
        User existing = new User();
        existing.setId(1);
        existing.setUserCode("123456");

        User updated = new User();
        updated.setId(1);
        updated.setUserCode("123456");
        updated.setFile(mock(MultipartFile.class));
        when(updated.getFile().isEmpty()).thenReturn(true);

        when(userRepo.getUserById(1)).thenReturn(existing);

        userService.addOrUpdateUser(updated);

        verify(userRepo).addOrUpdateUser(updated);
    }

    // Complex method: getProfileByUserId - Test all conditional branches
    @Test
    public void testGetProfileByUserId_AdminExists_ShouldReturnAdmin() {
        AdminProfile adminProfile = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(adminProfile);

        Object result = userService.getProfileByUserId(1);

        assertEquals(adminProfile, result);
        verify(adminRepo).getAdminProfileByUserId(1);
        verify(alumniRepo, never()).getAlumniProfileByUserId(anyInt());
        verify(lecturerRepo, never()).getLecturerProfileByUserId(anyInt());
    }

    @Test
    public void testGetProfileByUserId_AlumniExists_ShouldReturnAlumni() {
        AlumniProfile alumniProfile = new AlumniProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(null);
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(alumniProfile);

        Object result = userService.getProfileByUserId(1);

        assertEquals(alumniProfile, result);
        verify(adminRepo).getAdminProfileByUserId(1);
        verify(alumniRepo).getAlumniProfileByUserId(1);
        verify(lecturerRepo, never()).getLecturerProfileByUserId(anyInt());
    }

    @Test
    public void testGetProfileByUserId_LecturerExists_ShouldReturnLecturer() {
        LecturerProfile lecturerProfile = new LecturerProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(null);
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(null);
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(lecturerProfile);

        Object result = userService.getProfileByUserId(1);

        assertEquals(lecturerProfile, result);
        verify(adminRepo).getAdminProfileByUserId(1);
        verify(alumniRepo).getAlumniProfileByUserId(1);
        verify(lecturerRepo).getLecturerProfileByUserId(1);
    }

    @Test
    public void testGetProfileByUserId_NoneExists_ShouldReturnNull() {
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(null);
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(null);
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(null);

        Object result = userService.getProfileByUserId(1);

        assertNull(result);
    }

    // Complex method: updateUser - Test all conditional field branches
    @Test
    public void testUpdateUser_UpdateFirstName() {
        Map<String, String> params = new HashMap<>();
        params.put("firstName", "John");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        userService.updateUser(params, null, 1);

        assertEquals("John", user.getFirstName());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_UpdateLastName() {
        Map<String, String> params = new HashMap<>();
        params.put("lastName", "Doe");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        userService.updateUser(params, null, 1);

        assertEquals("Doe", user.getLastName());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_UpdateDob() {
        Map<String, String> params = new HashMap<>();
        params.put("dob", "2000-01-15");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        userService.updateUser(params, null, 1);

        assertNotNull(user.getDob());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_UpdateGender() {
        Map<String, String> params = new HashMap<>();
        params.put("gender", "male");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        userService.updateUser(params, null, 1);

        assertEquals(Gender.MALE, user.getGender());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_UpdateUserCode_NewCodeAvailable_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("userCode", "654321");

        User user = new User();
        user.setUserCode("123456");
        when(userRepo.getUserById(1)).thenReturn(user);
        when(userRepo.existUserByUserCode("654321")).thenReturn(false);

        userService.updateUser(params, null, 1);

        assertEquals("654321", user.getUserCode());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_UpdateUserCode_NewCodeExists_ShouldThrowException() {
        Map<String, String> params = new HashMap<>();
        params.put("userCode", "654321");

        User user = new User();
        user.setUserCode("123456");
        when(userRepo.getUserById(1)).thenReturn(user);
        when(userRepo.existUserByUserCode("654321")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.updateUser(params, null, 1));
        verify(userRepo, never()).addOrUpdateUser(any());
    }

    @Test
    public void testUpdateUser_UpdatePhone() {
        Map<String, String> params = new HashMap<>();
        params.put("phone", "0123456789");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        userService.updateUser(params, null, 1);

        assertEquals("0123456789", user.getPhone());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_WithAvatar_Success() throws Exception {
        Map<String, String> params = new HashMap<>();

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile avatar = mock(MultipartFile.class);
        when(avatar.isEmpty()).thenReturn(false);
        when(avatar.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/new-avatar.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        userService.updateUser(params, avatar, 1);

        assertEquals("https://cloudinary.com/new-avatar.jpg", user.getAvatar());
        verify(userRepo).addOrUpdateUser(user);
    }

    @Test
    public void testUpdateUser_AvatarUploadFails_ShouldStillUpdate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("firstName", "Jane");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile avatar = mock(MultipartFile.class);
        when(avatar.isEmpty()).thenReturn(false);
        when(avatar.getBytes()).thenThrow(new IOException("Upload failed"));

        userService.updateUser(params, avatar, 1);

        assertEquals("Jane", user.getFirstName());
        verify(userRepo).addOrUpdateUser(user);
    }
}
