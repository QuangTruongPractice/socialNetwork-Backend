package com.tqt.services;

import com.cloudinary.Cloudinary;
import com.tqt.pojo.AlumniProfile;
import com.tqt.pojo.User;
import com.tqt.repositories.AlumniProfileRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.impl.AlumniProfileServiceImpl;
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

public class AlumniProfileServiceTest extends BaseServiceTest {

    @Mock
    private AlumniProfileRepository alumniRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private AlumniProfileServiceImpl alumniProfileService;

    // Simple pass-through methods - 1 test each
    @Test
    public void testGetAlumniProfileById_Success() {
        AlumniProfile profile = new AlumniProfile();
        when(alumniRepo.getAlumniProfileById(1)).thenReturn(profile);

        AlumniProfile result = alumniProfileService.getAlumniProfileById(1);

        assertEquals(profile, result);
        verify(alumniRepo).getAlumniProfileById(1);
    }

    @Test
    public void testGetAlumniProfileByUserId_Success() {
        AlumniProfile profile = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(profile);

        AlumniProfile result = alumniProfileService.getAlumniProfileByUserId(1);

        assertEquals(profile, result);
        verify(alumniRepo).getAlumniProfileByUserId(1);
    }

    // Complex method: addAlumniProfile - Test all branches
    @Test
    public void testAddAlumniProfile_AllFieldsProvided_NoCoverImage() {
        Map<String, String> params = new HashMap<>();
        params.put("graduationYear", "2020");
        params.put("major", "Computer Science");
        params.put("faculty", "Engineering");
        params.put("currentJob", "Software Engineer");
        params.put("company", "Tech Corp");
        params.put("description", "Alumni description");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        AlumniProfile saved = new AlumniProfile();
        when(alumniRepo.addAlumniProfile(any(AlumniProfile.class))).thenReturn(saved);

        AlumniProfile result = alumniProfileService.addAlumniProfile(params, null, 1);

        assertEquals(saved, result);
        verify(userRepo).getUserById(1);
        verify(alumniRepo).addAlumniProfile(any(AlumniProfile.class));
    }

    @Test
    public void testAddAlumniProfile_UserNotExists_ShouldStillCreate() {
        Map<String, String> params = new HashMap<>();
        params.put("graduationYear", "2020");
        params.put("major", "CS");
        params.put("faculty", "Engineering");
        params.put("currentJob", "Developer");
        params.put("company", "XYZ");
        params.put("description", "Test");

        when(userRepo.getUserById(999)).thenReturn(null);

        AlumniProfile saved = new AlumniProfile();
        when(alumniRepo.addAlumniProfile(any(AlumniProfile.class))).thenReturn(saved);

        AlumniProfile result = alumniProfileService.addAlumniProfile(params, null, 999);

        assertEquals(saved, result);
        verify(userRepo).getUserById(999);
    }

    @Test
    public void testAddAlumniProfile_WithCoverImage_Success() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("graduationYear", "2020");
        params.put("major", "CS");
        params.put("faculty", "Engineering");
        params.put("currentJob", "Dev");
        params.put("company", "ABC");
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

        AlumniProfile saved = new AlumniProfile();
        when(alumniRepo.addAlumniProfile(any(AlumniProfile.class))).thenReturn(saved);

        AlumniProfile result = alumniProfileService.addAlumniProfile(params, coverImage, 1);

        assertEquals(saved, result);
        verify(cloudinary.uploader()).upload(any(byte[].class), any());
    }

    @Test
    public void testAddAlumniProfile_CoverImageUploadFails_ShouldStillCreate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("graduationYear", "2020");
        params.put("major", "CS");
        params.put("faculty", "Eng");
        params.put("currentJob", "Dev");
        params.put("company", "XYZ");
        params.put("description", "Test");

        User user = new User();
        when(userRepo.getUserById(1)).thenReturn(user);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenThrow(new IOException("Upload failed"));

        AlumniProfile saved = new AlumniProfile();
        when(alumniRepo.addAlumniProfile(any(AlumniProfile.class))).thenReturn(saved);

        AlumniProfile result = alumniProfileService.addAlumniProfile(params, coverImage, 1);

        assertEquals(saved, result);
    }

    // Complex method: updateAlumniProfile - Test all 6 conditional field branches +
    // coverImage
    @Test
    public void testUpdateAlumniProfile_UpdateGraduationYear() {
        Map<String, String> params = new HashMap<>();
        params.put("graduationYear", "2021");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals(2021, existing.getGraduationYear());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_UpdateMajor() {
        Map<String, String> params = new HashMap<>();
        params.put("major", "Data Science");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals("Data Science", existing.getMajor());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_UpdateFaculty() {
        Map<String, String> params = new HashMap<>();
        params.put("faculty", "Science");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals("Science", existing.getFaculty());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_UpdateCurrentJob() {
        Map<String, String> params = new HashMap<>();
        params.put("currentJob", "Senior Developer");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals("Senior Developer", existing.getCurrentJob());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_UpdateCompany() {
        Map<String, String> params = new HashMap<>();
        params.put("company", "Google");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals("Google", existing.getCompany());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_UpdateDescription() {
        Map<String, String> params = new HashMap<>();
        params.put("description", "New description");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals("New description", existing.getDescription());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_UpdateAllFields() {
        Map<String, String> params = new HashMap<>();
        params.put("graduationYear", "2022");
        params.put("major", "AI");
        params.put("faculty", "Tech");
        params.put("currentJob", "ML Engineer");
        params.put("company", "Meta");
        params.put("description", "Expert in AI");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals(2022, existing.getGraduationYear());
        assertEquals("AI", existing.getMajor());
        assertEquals("Tech", existing.getFaculty());
        assertEquals("ML Engineer", existing.getCurrentJob());
        assertEquals("Meta", existing.getCompany());
        assertEquals("Expert in AI", existing.getDescription());
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_NoFieldsProvided_ShouldStillUpdate() {
        Map<String, String> params = new HashMap<>();

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);
        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, null, 1);

        assertEquals(existing, result);
        verify(alumniRepo).updateAlumniProfile(existing);
    }

    @Test
    public void testUpdateAlumniProfile_WithCoverImage_Success() throws Exception {
        Map<String, String> params = new HashMap<>();

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        Map<String, Object> cloudinaryResult = new HashMap<>();
        cloudinaryResult.put("secure_url", "https://cloudinary.com/new.jpg");
        when(cloudinary.uploader()).thenReturn(mock(com.cloudinary.Uploader.class));
        when(cloudinary.uploader().upload(any(byte[].class), any())).thenReturn(cloudinaryResult);

        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, coverImage, 1);

        assertEquals(existing, result);
        verify(cloudinary.uploader()).upload(any(byte[].class), any());
    }

    @Test
    public void testUpdateAlumniProfile_CoverImageUploadFails_ShouldStillUpdate() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("major", "Physics");

        AlumniProfile existing = new AlumniProfile();
        when(alumniRepo.getAlumniProfileByUserId(1)).thenReturn(existing);

        MultipartFile coverImage = mock(MultipartFile.class);
        when(coverImage.isEmpty()).thenReturn(false);
        when(coverImage.getBytes()).thenThrow(new IOException("Upload failed"));

        when(alumniRepo.updateAlumniProfile(any(AlumniProfile.class))).thenReturn(existing);

        AlumniProfile result = alumniProfileService.updateAlumniProfile(params, coverImage, 1);

        assertEquals("Physics", existing.getMajor());
        verify(alumniRepo).updateAlumniProfile(existing);
    }
}
