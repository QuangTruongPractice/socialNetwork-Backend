package com.tqt.services;

import com.cloudinary.Cloudinary;
import com.tqt.pojo.LecturerProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.LecturerProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.LecturerProfileServiceImpl;
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

public class LecturerProfileServiceTest extends BaseServiceTest {

    @Mock
    private LecturerProfileRepository lecturerRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private LecturerProfileServiceImpl lecturerProfileService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetLecturerProfileById_Success() {
        LecturerProfile profile = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileById(1)).thenReturn(profile);

        LecturerProfile result = lecturerProfileService.getLecturerProfileById(1);

        assertEquals(profile, result);
        verify(lecturerRepo).getLecturerProfileById(1);
    }

    @Test
    public void testGetLecturerProfileByUserId_Success() {
        LecturerProfile profile = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(profile);

        LecturerProfile result = lecturerProfileService.getLecturerProfileByUserId(1);

        assertEquals(profile, result);
        verify(lecturerRepo).getLecturerProfileByUserId(1);
    }

    // Complex method: addLecturerProfile - Test all branches
    @Test
    public void testAddLecturerProfile_AllFieldsProvided_NoCoverImage() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Professor");
        params.put("degree", "PhD");
        params.put("specialization", "Machine Learning");
        params.put("faculty", "Computer Science");
        params.put("description", "Lecturer description");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        LecturerProfile saved = new LecturerProfile();
        when(lecturerRepo.addLecturerProfile(any(LecturerProfile.class))).thenReturn(saved);

        LecturerProfile result = lecturerProfileService.addLecturerProfile(params, null, 1);

        assertEquals(saved, result);
        verify(userRepo).getUserById(1);
        verify(lecturerRepo).addLecturerProfile(any(LecturerProfile.class));
    }

    @Test
    public void testAddLecturerProfile_UserNotExists_ShouldStillCreate() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Prof");
        params.put("degree", "PhD");
        params.put("specialization", "AI");
        params.put("faculty", "CS");
        params.put("description", "Test");

        when(userRepo.getUserById(999)).thenReturn(null);

        LecturerProfile saved = new LecturerProfile();
        when(lecturerRepo.addLecturerProfile(any(LecturerProfile.class))).thenReturn(saved);

        LecturerProfile result = lecturerProfileService.addLecturerProfile(params, null, 999);

        assertEquals(saved, result);
        verify(userRepo).getUserById(999);
    }

    @Test
    public void testAddLecturerProfile_WithCoverImage_Success() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Prof");
        params.put("degree", "PhD");
        params.put("specialization", "AI");
        params.put("faculty", "CS");
        params.put("description", "Desc");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/image.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        LecturerProfile saved = new LecturerProfile();
        when(lecturerRepo.addLecturerProfile(any(LecturerProfile.class))).thenReturn(saved);

        LecturerProfile result = lecturerProfileService.addLecturerProfile(params, coverImage, 1);

        assertEquals(saved, result);
        verify(cloudinary.uploader()).upload(any(byte[].class), any());
    }

    @Test
    public void testAddLecturerProfile_CoverImageUploadFails_ShouldStillCreate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Prof");
        params.put("degree", "PhD");
        params.put("specialization", "DB");
        params.put("faculty", "IT");
        params.put("description", "Test");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenThrow(new IOException("Upload failed"));

        LecturerProfile saved = new LecturerProfile();
        when(lecturerRepo.addLecturerProfile(any(LecturerProfile.class))).thenReturn(saved);

        LecturerProfile result = lecturerProfileService.addLecturerProfile(params, coverImage, 1);

        assertEquals(saved, result);
    }

    // Complex method: updateLecturerProfile - Test all 5 conditional field branches
    // + coverImage
    @Test
    public void testUpdateLecturerProfile_UpdatePosition() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Associate Professor");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals("Associate Professor", existing.getPosition());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_UpdateDegree() {
        Map<String, String> params = new HashMap<>();
        params.put("degree", "Master");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals("Master", existing.getDegree());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_UpdateSpecialization() {
        Map<String, String> params = new HashMap<>();
        params.put("specialization", "Deep Learning");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals("Deep Learning", existing.getSpecialization());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_UpdateFaculty() {
        Map<String, String> params = new HashMap<>();
        params.put("faculty", "Information Technology");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals("Information Technology", existing.getFaculty());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_UpdateDescription() {
        Map<String, String> params = new HashMap<>();
        params.put("description", "Updated description");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals("Updated description", existing.getDescription());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_UpdateAllFields() {
        Map<String, String> params = new HashMap<>();
        params.put("position", "Full Professor");
        params.put("degree", "PhD");
        params.put("specialization", "Quantum Computing");
        params.put("faculty", "Physics");
        params.put("description", "Expert in quantum");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals("Full Professor", existing.getPosition());
        assertEquals("PhD", existing.getDegree());
        assertEquals("Quantum Computing", existing.getSpecialization());
        assertEquals("Physics", existing.getFaculty());
        assertEquals("Expert in quantum", existing.getDescription());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_NoFieldsProvided_ShouldStillUpdate() {
        Map<String, String> params = new HashMap<>();

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);
        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, null, 1);

        assertEquals(existing, result);
        verify(lecturerRepo).updateLecturerProfile(existing);
    }

    @Test
    public void testUpdateLecturerProfile_WithCoverImage_Success() throws Exception {
        Map<String, String> params = new HashMap<>();

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/new.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, coverImage, 1);

        assertEquals(existing, result);
        verify(cloudinary.uploader()).upload(any(byte[].class), any());
    }

    @Test
    public void testUpdateLecturerProfile_CoverImageUploadFails_ShouldStillUpdate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("degree", "Doctorate");

        LecturerProfile existing = new LecturerProfile();
        when(lecturerRepo.getLecturerProfileByUserId(1)).thenReturn(existing);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenThrow(new IOException("Upload failed"));

        when(lecturerRepo.updateLecturerProfile(any(LecturerProfile.class))).thenReturn(existing);

        LecturerProfile result = lecturerProfileService.updateLecturerProfile(params, coverImage, 1);

        assertEquals("Doctorate", existing.getDegree());
        verify(lecturerRepo).updateLecturerProfile(existing);
    }
}
