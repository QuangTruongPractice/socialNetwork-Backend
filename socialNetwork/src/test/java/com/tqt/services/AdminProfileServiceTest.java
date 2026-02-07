package com.tqt.services;

import com.cloudinary.Cloudinary;
import com.tqt.pojo.AdminProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.AdminProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.AdminProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminProfileServiceTest extends BaseServiceTest {

    @Mock
    private AdminProfileRepository adminRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private AdminProfileServiceImpl adminProfileService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetAdminProfileById_Success() {
        AdminProfile profile = new AdminProfile();
        when(adminRepo.getAdminProfileById(1)).thenReturn(profile);

        AdminProfile result = adminProfileService.getAdminProfileById(1);

        assertEquals(profile, result);
        verify(adminRepo).getAdminProfileById(1);
    }

    @Test
    public void testGetAdminProfileByUserId_Success() {
        AdminProfile profile = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(profile);

        AdminProfile result = adminProfileService.getAdminProfileByUserId(1);

        assertEquals(profile, result);
        verify(adminRepo).getAdminProfileByUserId(1);
    }

    // Complex method: addAdminProfile - Test all branches
    @Test
    public void testAddAdminProfile_UserExists_NoCoverImage() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Manager");
        params.put("description", "Admin description");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        AdminProfile saved = new AdminProfile();
        when(adminRepo.addAdminProfile(any(AdminProfile.class))).thenReturn(saved);

        AdminProfile result = adminProfileService.addAdminProfile(params, null, 1);

        assertEquals(saved, result);
        verify(userRepo).getUserById(1);
        verify(adminRepo).addAdminProfile(any(AdminProfile.class));
    }

    @Test
    public void testAddAdminProfile_UserNotExists_ShouldStillCreate() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Manager");
        params.put("description", "Admin description");

        when(userRepo.getUserById(999)).thenReturn(null);

        AdminProfile saved = new AdminProfile();
        when(adminRepo.addAdminProfile(any(AdminProfile.class))).thenReturn(saved);

        AdminProfile result = adminProfileService.addAdminProfile(params, null, 999);

        assertEquals(saved, result);
        verify(userRepo).getUserById(999);
        verify(adminRepo).addAdminProfile(any(AdminProfile.class));
    }

    @Test
    public void testAddAdminProfile_WithCoverImage_Success() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Manager");
        params.put("description", "Admin description");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/image.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        AdminProfile saved = new AdminProfile();
        when(adminRepo.addAdminProfile(any(AdminProfile.class))).thenReturn(saved);

        AdminProfile result = adminProfileService.addAdminProfile(params, coverImage, 1);

        assertEquals(saved, result);
        verify(cloudinary.uploader()).upload(any(byte[].class), any());
    }

    @Test
    public void testAddAdminProfile_CoverImageUploadFails_ShouldStillCreate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Manager");
        params.put("description", "Admin description");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenThrow(new IOException("Upload failed"));

        AdminProfile saved = new AdminProfile();
        when(adminRepo.addAdminProfile(any(AdminProfile.class))).thenReturn(saved);

        AdminProfile result = adminProfileService.addAdminProfile(params, coverImage, 1);

        assertEquals(saved, result);
        verify(adminRepo).addAdminProfile(any(AdminProfile.class));
    }

    // Complex method: updateAdminProfile - Test all conditional branches
    @Test
    public void testUpdateAdminProfile_UpdatePosition() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Senior Manager");

        AdminProfile existing = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(existing);
        when(adminRepo.updateAdminProfile(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = adminProfileService.updateAdminProfile(params, null, 1);

        assertEquals("Senior Manager", existing.getPosition());
        verify(adminRepo).updateAdminProfile(existing);
    }

    @Test
    public void testUpdateAdminProfile_UpdateDescription() {
        Map<String, String> params = new HashMap<>();
        params.put("description", "New description");

        AdminProfile existing = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(existing);
        when(adminRepo.updateAdminProfile(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = adminProfileService.updateAdminProfile(params, null, 1);

        assertEquals("New description", existing.getDescription());
        verify(adminRepo).updateAdminProfile(existing);
    }

    @Test
    public void testUpdateAdminProfile_UpdateBothFields() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Director");
        params.put("description", "New director description");

        AdminProfile existing = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(existing);
        when(adminRepo.updateAdminProfile(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = adminProfileService.updateAdminProfile(params, null, 1);

        assertEquals("Director", existing.getPosition());
        assertEquals("New director description", existing.getDescription());
        verify(adminRepo).updateAdminProfile(existing);
    }

    @Test
    public void testUpdateAdminProfile_NoFieldsProvided_ShouldStillUpdate() {
        Map<String, String> params = new HashMap<>();

        AdminProfile existing = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(existing);
        when(adminRepo.updateAdminProfile(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = adminProfileService.updateAdminProfile(params, null, 1);

        assertEquals(existing, result);
        verify(adminRepo).updateAdminProfile(existing);
    }

    @Test
    public void testUpdateAdminProfile_WithCoverImage_Success() throws Exception {
        Map<String, String> params = new HashMap<>();

        AdminProfile existing = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(existing);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/new-image.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        when(adminRepo.updateAdminProfile(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = adminProfileService.updateAdminProfile(params, coverImage, 1);

        assertEquals(existing, result);
        verify(cloudinary.uploader()).upload(any(byte[].class), any());
    }

    @Test
    public void testUpdateAdminProfile_CoverImageUploadFails_ShouldStillUpdate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("position", "CEO");

        AdminProfile existing = new AdminProfile();
        when(adminRepo.getAdminProfileByUserId(1)).thenReturn(existing);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenThrow(new IOException("Upload failed"));

        when(adminRepo.updateAdminProfile(any(AdminProfile.class))).thenReturn(existing);

        AdminProfile result = adminProfileService.updateAdminProfile(params, coverImage, 1);

        assertEquals("CEO", existing.getPosition());
        verify(adminRepo).updateAdminProfile(existing);
    }
}
