/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.services.impl;

import com.tqt.dto.GroupDTO;
import com.tqt.pojo.Group;
import com.tqt.repositories.GroupRepository;
import com.tqt.services.GroupService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Quang Truong
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepo;

    @Override
    @Transactional
    public List<GroupDTO> getGroups(Map<String, String> params) {
        List<Group> groups = this.groupRepo.getGroups(params); 
        return groups.stream().map(GroupDTO::new).collect(Collectors.toList());
    }
    
    public Integer getTotalPages(Map<String, String> params){
        return this.groupRepo.getTotalPages(params);
    }

    @Override
    public Group getGroupById(int id) {
        return this.groupRepo.getGroupById(id);
    }

    @Override
    public void addOrUpdateGroup(Group g) {
        this.groupRepo.addOrUpdateGroup(g);
    }

    @Override
    public void deleteGroup(int id) {
        this.groupRepo.deleteGroup(id);
    }

}
