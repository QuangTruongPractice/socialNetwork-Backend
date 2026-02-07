/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqt.dto.PostDTO;
import com.tqt.enums.PostType;
import com.tqt.pojo.Account;
import com.tqt.pojo.Group;
import com.tqt.pojo.Post;
import com.tqt.pojo.PostRecipient;
import com.tqt.pojo.SurveyOption;
import com.tqt.pojo.SurveyVote;
import com.tqt.pojo.User;
import com.tqt.repositories.AccountRepository;
import com.tqt.repositories.GroupRepository;
import com.tqt.repositories.PostRecipientRepository;
import com.tqt.repositories.PostRepository;
import com.tqt.repositories.SurveyOptionRepository;
import com.tqt.repositories.SurveyVoteRepository;
import com.tqt.repositories.UserRepository;
import com.tqt.services.MailService;
import com.tqt.services.PostService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Quang Truong
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private SurveyOptionRepository soRepo;

    @Autowired
    private SurveyVoteRepository svRepo;

    @Autowired
    private PostRecipientRepository prRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Post> getPosts(Map<String, String> params) {
        return this.postRepo.getPosts(params);
    }

    @Override
    public Integer getTotalPages(Map<String, String> params) {
        return this.postRepo.getTotalPages(params);
    }

    @Override
    public Post getPostById(int id) {
        return this.postRepo.getPostById(id);
    }

    @Override
    public List<Post> getPostByUserId(int id) {
        return this.postRepo.getPostByUserId(id);
    }

    @Override
    public void addOrUpdatePost(Post p) {
        if (!p.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.postRepo.addOrUpdatePost(p);
    }

    @Override
    public void deletePost(int id) {
        this.postRepo.deletePost(id);
    }

    @Override
    public Post addPost(Map<String, String> params, MultipartFile image, Integer userId, String role) {
        User user = this.userRepo.getUserById(userId);
        Post p = new Post();
        p.setContent(params.get("content"));
        p.setIsLocked(false);
        p.setPostType(PostType.POST);
        p.setUser(user);

        if (image != null && !image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        this.postRepo.addOrUpdatePost(p);
        return p;
    }

    @Override
    public Post updatePost(Map<String, String> params, MultipartFile image, Integer userId, Integer postId,
            String role) {
        Post p = this.postRepo.getPostById(postId);

        if (!p.getUser().getId().equals(userId) && !"ADMIN".equals(role)) {
            throw new SecurityException("Bạn không có quyền cập nhật bài viết này!");
        }

        if (params.containsKey("content")) {
            p.setContent(params.get("content"));
        }

        if (image != null && !image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Lỗi upload ảnh: " + ex.getMessage());
            }
        }
        if (params.containsKey("isLocked")) {
            p.setIsLocked(Boolean.parseBoolean(params.get("isLocked")));
        }

        this.postRepo.addOrUpdatePost(p);
        return p;
    }

    @Override
    public Post addSurvey(Map<String, String> params, MultipartFile image, Integer userId, String role,
            List<String> options) {
        if (!"ADMIN".equals(role)) {
            throw new SecurityException("Chỉ ADMIN mới được tạo khảo sát!");
        }

        User user = this.userRepo.getUserById(userId);
        Post surveyPost = new Post();
        surveyPost.setContent(params.get("content"));
        surveyPost.setIsLocked(false);
        surveyPost.setPostType(PostType.SURVEY);
        surveyPost.setUser(user);

        if (image != null && !image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                surveyPost.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Lỗi upload ảnh khảo sát: " + ex.getMessage());
            }
        }

        List<SurveyOption> surveyOptions = options.stream().map(opt -> {
            SurveyOption o = new SurveyOption();
            o.setOptionText(opt);
            o.setPost(surveyPost);
            return o;
        }).toList();

        surveyPost.setSurveyOptions(surveyOptions);

        this.postRepo.addOrUpdatePost(surveyPost);

        return surveyPost;
    }

    @Override
    public Post addInvitation(Map<String, String> params, MultipartFile image, Integer userId, String role,
            List<String> recipients) {
        if (!"ADMIN".equals(role)) {
            throw new SecurityException("Chỉ ADMIN mới được tạo thư mời!");
        }

        User user = this.userRepo.getUserById(userId);
        Post invitePost = new Post();
        invitePost.setContent(params.get("content"));
        invitePost.setIsLocked(false);
        invitePost.setPostType(PostType.INVITATION);
        invitePost.setUser(user);

        if (image != null && !image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                invitePost.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Lỗi upload ảnh khảo sát: " + ex.getMessage());
            }
        }
        this.postRepo.addOrUpdatePost(invitePost);
        for (String recipient : recipients) {
            PostRecipient pr = new PostRecipient();
            pr.setPost(invitePost);

            if ("ALL".equalsIgnoreCase(recipient)) {
                pr.setIsToAll(true);
                this.mailService.sendInvitationMailToAll(new PostDTO(invitePost, 0));
            } else if (recipient.startsWith("group:")) {
                int groupId = Integer.parseInt(recipient.substring(6));
                Group group = this.groupRepo.getGroupById(groupId);
                pr.setGroup(group);
                this.mailService.sendInvitationMailToGroup(new PostDTO(invitePost, 0), group);
            } else if (recipient.startsWith("account:")) {
                int accId = Integer.parseInt(recipient.substring(8));
                Account acc = this.accRepo.getAccountById(accId);
                pr.setAccount(acc);
                this.mailService.sendInvitationMailToAccount(new PostDTO(invitePost, 0), acc);
            }
            this.prRepo.addOrUpdatePostRecipient(pr);
        }

        return invitePost;
    }

    @Override
    public void vote(Integer userId, Integer optionId) {
        User user = this.userRepo.getUserById(userId);
        SurveyOption option = this.soRepo.getSurveyOptionById(optionId);
        Post post = option.getPost();
        boolean hasVoted = this.svRepo.existsByUserIdAndPostId(userId, post.getId());
        if (hasVoted) {
            throw new RuntimeException("You have already voted in this survey!");
        }

        option.setVoteCount(option.getVoteCount() + 1);
        this.soRepo.addOrUpdateSurveyOption(option);

        SurveyVote vote = new SurveyVote();
        vote.setUser(user);
        vote.setOption(option);
        this.svRepo.addOrUpdateSurveyVote(vote);
    }

}
