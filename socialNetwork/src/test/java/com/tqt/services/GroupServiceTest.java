package com.tqt.services;

import com.tqt.dto.GroupDTO;
import com.tqt.pojo.Group;
import com.tqt.repositories.GroupRepository;
import com.tqt.services.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupServiceTest extends BaseServiceTest {

    @Mock
    private GroupRepository groupRepo;

    @InjectMocks
    private GroupServiceImpl groupService;

    // All methods in GroupService are simple pass-through - 1 test each
    @Test
    public void testGetGroups_Success() {
        Map<String, String> params = new HashMap<>();
        List<Group> groups = Arrays.asList(new Group(), new Group());
        when(groupRepo.getGroups(params)).thenReturn(groups);

        List<GroupDTO> result = groupService.getGroups(params);

        assertEquals(2, result.size());
        verify(groupRepo).getGroups(params);
    }

    @Test
    public void testGetTotalPages_Success() {
        Map<String, String> params = new HashMap<>();
        when(groupRepo.getTotalPages(params)).thenReturn(3);

        Integer result = groupService.getTotalPages(params);

        assertEquals(3, result);
        verify(groupRepo).getTotalPages(params);
    }

    @Test
    public void testGetGroupById_Success() {
        Group group = new Group();
        when(groupRepo.getGroupById(1)).thenReturn(group);

        Group result = groupService.getGroupById(1);

        assertEquals(group, result);
        verify(groupRepo).getGroupById(1);
    }

    @Test
    public void testAddOrUpdateGroup_Success() {
        Group group = new Group();
        doNothing().when(groupRepo).addOrUpdateGroup(group);

        groupService.addOrUpdateGroup(group);

        verify(groupRepo).addOrUpdateGroup(group);
    }

    @Test
    public void testDeleteGroup_Success() {
        doNothing().when(groupRepo).deleteGroup(1);

        groupService.deleteGroup(1);

        verify(groupRepo).deleteGroup(1);
    }
}
