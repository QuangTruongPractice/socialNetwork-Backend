/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.controllers;

import com.tqt.dto.GroupDTO;
import com.tqt.services.GroupService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Quang Truong
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiGroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<?> getGroups(@RequestParam Map<String, String> params) {
        List<GroupDTO> groups = this.groupService.getGroups(params);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
