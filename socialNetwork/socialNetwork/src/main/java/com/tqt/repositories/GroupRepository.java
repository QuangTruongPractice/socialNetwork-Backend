/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tqt.repositories;

import com.tqt.pojo.Group;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Quang Truong
 */
public interface GroupRepository {
    List<Group> getGroups(Map<String, String> params);
    Integer getTotalPages(Map<String, String> params);
    Group getGroupById(int id);
    void addOrUpdateGroup(Group g);
    void deleteGroup(int id);
}
