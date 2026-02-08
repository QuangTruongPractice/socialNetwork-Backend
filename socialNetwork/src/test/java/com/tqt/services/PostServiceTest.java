package com.tqt.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.tqt.dto.PostDTO;
import com.tqt.enums.PostType;
import com.tqt.pojo.*;
import com.tqt.repositories.*;
import com.tqt.services.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PostServiceTest extends BaseServiceTest {

    @Mock
    private PostRepository postRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private AccountRepository accRepo;

    @Mock
    private GroupRepository groupRepo;

    @Mock
    private SurveyOptionRepository soRepo;

    @Mock
    private SurveyVoteRepository svRepo;

    @Mock
    private PostRecipientRepository prRepo;

    @Mock
    private MailService mailService;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private PostServiceImpl postService;

    // ========== Simple pass-through methods - 5 tests ==========

    @Test
    public void testGetPosts_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");

        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(postRepo.getPosts(params)).thenReturn(posts);

        List<Post> result = postService.getPosts(params);

        assertEquals(2, result.size());
        verify(postRepo).getPosts(params);
    }

    @Test
    public void testGetTotalPages_Success() {
        Map<String, String> params = new HashMap<>();
        when(postRepo.getTotalPages(params)).thenReturn(5);

        Integer result = postService.getTotalPages(params);

        assertEquals(5, result);
        verify(postRepo).getTotalPages(params);
    }

    @Test
    public void testGetPostById_Success() {
        Post post = new Post();
        post.setId(1);
        when(postRepo.getPostById(1)).thenReturn(post);

        Post result = postService.getPostById(1);

        assertEquals(1, result.getId());
        verify(postRepo).getPostById(1);
    }

    @Test
    public void testGetPostByUserId_Success() {
        List<Post> posts = Arrays.asList(new Post(), new Post(), new Post());
        when(postRepo.getPostByUserId(10)).thenReturn(posts);

        List<Post> result = postService.getPostByUserId(10);

        assertEquals(3, result.size());
        verify(postRepo).getPostByUserId(10);
    }

    @Test
    public void testDeletePost_Success() {
        doNothing().when(postRepo).deletePost(100);

        postService.deletePost(100);

        verify(postRepo).deletePost(100);
    }

    // ========== addOrUpdatePost tests - 5 tests ==========

    @Test
    public void testAddOrUpdatePost_NullFile_ShouldNotUpload() {
        Post post = new Post();
        post.setFile(null);
        post.setContent("Test content");

        doNothing().when(postRepo).addOrUpdatePost(post);

        postService.addOrUpdatePost(post);

        verify(cloudinary, never()).uploader();
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testAddOrUpdatePost_EmptyFile_ShouldNotUpload() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        Post post = new Post();
        post.setFile(emptyFile);

        doNothing().when(postRepo).addOrUpdatePost(post);

        postService.addOrUpdatePost(post);

        verify(cloudinary, never()).uploader();
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testAddOrUpdatePost_VideoUpload_Success() throws IOException {
        MultipartFile videoFile = mock(MultipartFile.class);
        when(videoFile.isEmpty()).thenReturn(false);
        when(videoFile.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/video.mp4");
        uploadResult.put("resource_type", "video");
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        Post post = new Post();
        post.setFile(videoFile);

        doNothing().when(postRepo).addOrUpdatePost(post);

        postService.addOrUpdatePost(post);

        assertEquals("https://cloudinary.com/video.mp4", post.getVideo());
        assertNull(post.getImage());
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testAddOrUpdatePost_ImageUpload_Success() throws IOException {
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/image.jpg");
        uploadResult.put("resource_type", "image");
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        Post post = new Post();
        post.setFile(imageFile);

        doNothing().when(postRepo).addOrUpdatePost(post);

        postService.addOrUpdatePost(post);

        assertEquals("https://cloudinary.com/image.jpg", post.getImage());
        assertNull(post.getVideo());
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testAddOrUpdatePost_IOException_ShouldStillSave() throws IOException {
        MultipartFile badFile = mock(MultipartFile.class);
        when(badFile.isEmpty()).thenReturn(false);
        when(badFile.getBytes()).thenThrow(new IOException("Upload failed"));

        Post post = new Post();
        post.setFile(badFile);

        doNothing().when(postRepo).addOrUpdatePost(post);

        postService.addOrUpdatePost(post);

        // Post should still be saved even if upload fails
        assertNull(post.getImage());
        assertNull(post.getVideo());
        verify(postRepo).addOrUpdatePost(post);
    }

    // ========== addPost test - 1 test ==========

    @Test
    public void testAddPost_Success() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("content", "New post content");

        User user = new User();
        user.setId(5);
        when(userRepo.getUserById(5)).thenReturn(user);

        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/newimage.jpg");
        uploadResult.put("resource_type", "image");
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        doNothing().when(postRepo).addOrUpdatePost(any(Post.class));

        Post result = postService.addPost(params, Arrays.asList(imageFile), 5, "USER");

        assertNotNull(result);
        assertEquals("New post content", result.getContent());
        assertEquals(PostType.POST, result.getPostType());
        assertFalse(result.getIsLocked());
        assertEquals(user, result.getUser());
        verify(postRepo).addOrUpdatePost(any(Post.class));
    }

    // ========== updatePost tests - 6 tests ==========

    @Test
    public void testUpdatePost_DifferentUserAndNotAdmin_ShouldThrowException() {
        Integer userId = 1;
        Integer otherUserId = 2;
        Integer postId = 100;

        User owner = new User();
        owner.setId(otherUserId);

        Post post = new Post();
        post.setUser(owner);

        when(postRepo.getPostById(postId)).thenReturn(post);

        Map<String, String> params = new HashMap<>();
        String role = "USER"; // Non-admin role

        // User is not post owner AND not admin -> should throw SecurityException
        assertThrows(SecurityException.class, () -> postService.updatePost(params, null, userId, postId, role));
    }

    @Test
    public void testUpdatePost_OwnerUpdatesPost_Success() {
        Integer userId = 5;
        Integer postId = 100;

        User owner = new User();
        owner.setId(userId);

        Post post = new Post();
        post.setUser(owner);
        post.setContent("Old content");

        when(postRepo.getPostById(postId)).thenReturn(post);
        doNothing().when(postRepo).addOrUpdatePost(post);

        Map<String, String> params = new HashMap<>();
        params.put("content", "Updated content");

        Post result = postService.updatePost(params, null, userId, postId, "USER");

        assertEquals("Updated content", result.getContent());
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testUpdatePost_AdminUpdatesOtherPost_Success() {
        Integer adminId = 1;
        Integer ownerId = 5;
        Integer postId = 100;

        User owner = new User();
        owner.setId(ownerId);

        Post post = new Post();
        post.setUser(owner);
        post.setContent("Original content");

        when(postRepo.getPostById(postId)).thenReturn(post);
        doNothing().when(postRepo).addOrUpdatePost(post);

        Map<String, String> params = new HashMap<>();
        params.put("content", "Admin updated");

        Post result = postService.updatePost(params, null, adminId, postId, "ADMIN");

        assertEquals("Admin updated", result.getContent());
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testUpdatePost_NoContentParam_ShouldNotUpdateContent() {
        Integer userId = 5;
        Integer postId = 100;

        User owner = new User();
        owner.setId(userId);

        Post post = new Post();
        post.setUser(owner);
        post.setContent("Original");

        when(postRepo.getPostById(postId)).thenReturn(post);
        doNothing().when(postRepo).addOrUpdatePost(post);

        Map<String, String> params = new HashMap<>();
        // No content param

        Post result = postService.updatePost(params, null, userId, postId, "USER");

        assertEquals("Original", result.getContent()); // Content unchanged
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testUpdatePost_WithImages_ShouldUploadMedia() throws IOException {
        Integer userId = 5;
        Integer postId = 100;

        User owner = new User();
        owner.setId(userId);

        Post post = new Post();
        post.setUser(owner);
        post.setMedias(new ArrayList<>());

        when(postRepo.getPostById(postId)).thenReturn(post);

        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/updated.jpg");
        uploadResult.put("resource_type", "image");
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        doNothing().when(postRepo).addOrUpdatePost(post);

        Map<String, String> params = new HashMap<>();

        Post result = postService.updatePost(params, Arrays.asList(imageFile), userId, postId, "USER");

        assertNotNull(result.getMedias());
        assertFalse(result.getMedias().isEmpty());
        verify(postRepo).addOrUpdatePost(post);
    }

    @Test
    public void testUpdatePost_WithIsLockedParam_ShouldUpdateLockStatus() {
        Integer userId = 5;
        Integer postId = 100;

        User owner = new User();
        owner.setId(userId);

        Post post = new Post();
        post.setUser(owner);
        post.setIsLocked(false);

        when(postRepo.getPostById(postId)).thenReturn(post);
        doNothing().when(postRepo).addOrUpdatePost(post);

        Map<String, String> params = new HashMap<>();
        params.put("isLocked", "true");

        Post result = postService.updatePost(params, null, userId, postId, "ADMIN");

        assertTrue(result.getIsLocked());
        verify(postRepo).addOrUpdatePost(post);
    }

    // ========== addSurvey tests - 3 tests ==========

    @Test
    public void testAddSurvey_NonAdmin_ShouldThrowException() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Survey question?");

        List<String> options = Arrays.asList("Option A", "Option B");

        assertThrows(SecurityException.class,
                () -> postService.addSurvey(params, null, 5, "USER", options));

        verify(postRepo, never()).addOrUpdatePost(any());
    }

    @Test
    public void testAddSurvey_Admin_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "What is your favorite color?");

        User admin = new User();
        admin.setId(1);
        when(userRepo.getUserById(1)).thenReturn(admin);

        List<String> options = Arrays.asList("Red", "Blue", "Green");

        doNothing().when(postRepo).addOrUpdatePost(any(Post.class));

        Post result = postService.addSurvey(params, null, 1, "ADMIN", options);

        assertNotNull(result);
        assertEquals(PostType.SURVEY, result.getPostType());
        assertEquals("What is your favorite color?", result.getContent());
        assertFalse(result.getIsLocked());
        assertEquals(3, result.getSurveyOptions().size());
        verify(postRepo).addOrUpdatePost(any(Post.class));
    }

    @Test
    public void testAddSurvey_WithImages_Success() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Survey with image");

        User admin = new User();
        admin.setId(1);
        when(userRepo.getUserById(1)).thenReturn(admin);

        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getBytes()).thenReturn(new byte[] { 1, 2, 3 });

        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/survey.jpg");
        uploadResult.put("resource_type", "image");
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        List<String> options = Arrays.asList("Yes", "No");

        doNothing().when(postRepo).addOrUpdatePost(any(Post.class));

        Post result = postService.addSurvey(params, Arrays.asList(imageFile), 1, "ADMIN", options);

        assertNotNull(result);
        assertEquals(PostType.SURVEY, result.getPostType());
        verify(postRepo).addOrUpdatePost(any(Post.class));
    }

    // ========== addInvitation tests - 5 tests ==========

    @Test
    public void testAddInvitation_NonAdmin_ShouldThrowException() {
        Map<String, String> params = new HashMap<>();
        List<String> recipients = Arrays.asList("ALL");

        assertThrows(SecurityException.class,
                () -> postService.addInvitation(params, null, 5, "USER", recipients));

        verify(postRepo, never()).addOrUpdatePost(any());
    }

    @Test
    public void testAddInvitation_Admin_WithAllRecipient_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Event invitation");

        User admin = new User();
        admin.setId(1);
        when(userRepo.getUserById(1)).thenReturn(admin);

        // Mock postRepo to set createdAt when addOrUpdatePost is called
        doAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setCreatedAt(java.time.LocalDate.now());
            return null;
        }).when(postRepo).addOrUpdatePost(any(Post.class));

        doNothing().when(prRepo).addOrUpdatePostRecipient(any(PostRecipient.class));
        doNothing().when(mailService).sendInvitationMailToAll(any(PostDTO.class));

        List<String> recipients = Arrays.asList("ALL");

        Post result = postService.addInvitation(params, null, 1, "ADMIN", recipients);

        assertNotNull(result);
        assertEquals(PostType.INVITATION, result.getPostType());
        verify(mailService).sendInvitationMailToAll(any(PostDTO.class));
        verify(prRepo).addOrUpdatePostRecipient(argThat(pr -> pr.getIsToAll() != null && pr.getIsToAll()));
    }

    @Test
    public void testAddInvitation_Admin_WithGroupRecipient_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Group event");

        User admin = new User();
        admin.setId(1);
        when(userRepo.getUserById(1)).thenReturn(admin);

        Group group = new Group();
        group.setId(10);
        when(groupRepo.getGroupById(10)).thenReturn(group);

        // Mock postRepo to set createdAt when addOrUpdatePost is called
        doAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setCreatedAt(java.time.LocalDate.now());
            return null;
        }).when(postRepo).addOrUpdatePost(any(Post.class));

        doNothing().when(prRepo).addOrUpdatePostRecipient(any(PostRecipient.class));
        doNothing().when(mailService).sendInvitationMailToGroup(any(PostDTO.class), any(Group.class));

        List<String> recipients = Arrays.asList("group:10");

        Post result = postService.addInvitation(params, null, 1, "ADMIN", recipients);

        assertNotNull(result);
        assertEquals(PostType.INVITATION, result.getPostType());
        verify(groupRepo).getGroupById(10);
        verify(mailService).sendInvitationMailToGroup(any(PostDTO.class), eq(group));
        verify(prRepo).addOrUpdatePostRecipient(argThat(pr -> pr.getGroup() != null && pr.getGroup().getId() == 10));
    }

    @Test
    public void testAddInvitation_Admin_WithAccountRecipient_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Personal invitation");

        User admin = new User();
        admin.setId(1);
        when(userRepo.getUserById(1)).thenReturn(admin);

        Account account = new Account();
        account.setId(50);
        when(accRepo.getAccountById(50)).thenReturn(account);

        // Mock postRepo to set createdAt when addOrUpdatePost is called
        doAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setCreatedAt(java.time.LocalDate.now());
            return null;
        }).when(postRepo).addOrUpdatePost(any(Post.class));

        doNothing().when(prRepo).addOrUpdatePostRecipient(any(PostRecipient.class));
        doNothing().when(mailService).sendInvitationMailToAccount(any(PostDTO.class), any(Account.class));

        List<String> recipients = Arrays.asList("account:50");

        Post result = postService.addInvitation(params, null, 1, "ADMIN", recipients);

        assertNotNull(result);
        assertEquals(PostType.INVITATION, result.getPostType());
        verify(accRepo).getAccountById(50);
        verify(mailService).sendInvitationMailToAccount(any(PostDTO.class), eq(account));
        verify(prRepo)
                .addOrUpdatePostRecipient(argThat(pr -> pr.getAccount() != null && pr.getAccount().getId() == 50));
    }

    @Test
    public void testAddInvitation_Admin_WithMultipleRecipients_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("content", "Multi-recipient invitation");

        User admin = new User();
        admin.setId(1);
        when(userRepo.getUserById(1)).thenReturn(admin);

        Group group = new Group();
        group.setId(5);
        when(groupRepo.getGroupById(5)).thenReturn(group);

        Account account = new Account();
        account.setId(20);
        when(accRepo.getAccountById(20)).thenReturn(account);

        // Mock postRepo to set createdAt when addOrUpdatePost is called
        doAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setCreatedAt(java.time.LocalDate.now());
            return null;
        }).when(postRepo).addOrUpdatePost(any(Post.class));

        doNothing().when(prRepo).addOrUpdatePostRecipient(any(PostRecipient.class));
        doNothing().when(mailService).sendInvitationMailToAll(any(PostDTO.class));
        doNothing().when(mailService).sendInvitationMailToGroup(any(PostDTO.class), any(Group.class));
        doNothing().when(mailService).sendInvitationMailToAccount(any(PostDTO.class), any(Account.class));

        List<String> recipients = Arrays.asList("ALL", "group:5", "account:20");

        Post result = postService.addInvitation(params, null, 1, "ADMIN", recipients);

        assertNotNull(result);
        verify(mailService).sendInvitationMailToAll(any(PostDTO.class));
        verify(mailService).sendInvitationMailToGroup(any(PostDTO.class), eq(group));
        verify(mailService).sendInvitationMailToAccount(any(PostDTO.class), eq(account));
        verify(prRepo, times(3)).addOrUpdatePostRecipient(any(PostRecipient.class));
    }

    // ========== vote tests - 2 tests ==========

    @Test
    public void testVote_Success() {
        User user = new User();
        user.setId(10);
        when(userRepo.getUserById(10)).thenReturn(user);

        Post post = new Post();
        post.setId(100);

        SurveyOption option = new SurveyOption();
        option.setId(5);
        option.setPost(post);
        option.setVoteCount(10);
        when(soRepo.getSurveyOptionById(5)).thenReturn(option);

        when(svRepo.existsByUserIdAndPostId(10, 100)).thenReturn(false);
        doNothing().when(soRepo).addOrUpdateSurveyOption(option);
        doNothing().when(svRepo).addOrUpdateSurveyVote(any(SurveyVote.class));

        postService.vote(10, 5);

        assertEquals(11, option.getVoteCount());
        verify(soRepo).addOrUpdateSurveyOption(option);
        verify(svRepo).addOrUpdateSurveyVote(any(SurveyVote.class));
    }

    @Test
    public void testVote_AlreadyVoted_ShouldThrowException() {
        Post post = new Post();
        post.setId(100);

        SurveyOption option = new SurveyOption();
        option.setId(5);
        option.setPost(post);
        when(soRepo.getSurveyOptionById(5)).thenReturn(option);

        when(svRepo.existsByUserIdAndPostId(10, 100)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> postService.vote(10, 5));

        verify(soRepo, never()).addOrUpdateSurveyOption(any());
        verify(svRepo, never()).addOrUpdateSurveyVote(any());
    }
}
