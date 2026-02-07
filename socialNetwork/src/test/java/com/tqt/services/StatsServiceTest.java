package com.tqt.services;

import com.tqt.repositories.StatsRepository;
import com.tqt.services.impl.StatsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatsServiceTest extends BaseServiceTest {

    @Mock
    private StatsRepository statsRepo;

    @InjectMocks
    private StatsServiceImpl statsService;

    // All methods are simple pass-through - 1 test each
    @Test
    public void testGetUserStatsByYear_NoParam_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 2024, 100 });
        when(statsRepo.getUserStatsByYear()).thenReturn(stats);

        List<Object[]> result = statsService.getUserStatsByYear();

        assertEquals(1, result.size());
        verify(statsRepo).getUserStatsByYear();
    }

    @Test
    public void testGetUserStatsByYear_WithYear_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 2024, 100 });
        when(statsRepo.getUserStatsByYear(2024)).thenReturn(stats);

        List<Object[]> result = statsService.getUserStatsByYear(2024);

        assertEquals(1, result.size());
        verify(statsRepo).getUserStatsByYear(2024);
    }

    @Test
    public void testGetUserStatsByQuarter_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 1, 25 });
        when(statsRepo.getUserStatsByQuarter(2024, 1)).thenReturn(stats);

        List<Object[]> result = statsService.getUserStatsByQuarter(2024, 1);

        assertEquals(1, result.size());
        verify(statsRepo).getUserStatsByQuarter(2024, 1);
    }

    @Test
    public void testGetUserStatsByMonth_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 12, 10 });
        when(statsRepo.getUserStatsByMonth(2024, 12)).thenReturn(stats);

        List<Object[]> result = statsService.getUserStatsByMonth(2024, 12);

        assertEquals(1, result.size());
        verify(statsRepo).getUserStatsByMonth(2024, 12);
    }

    @Test
    public void testGetPostStatsByYear_NoParam_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 2024, 500 });
        when(statsRepo.getPostStatsByYear()).thenReturn(stats);

        List<Object[]> result = statsService.getPostStatsByYear();

        assertEquals(1, result.size());
        verify(statsRepo).getPostStatsByYear();
    }

    @Test
    public void testGetPostStatsByYear_WithYear_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 2024, 500 });
        when(statsRepo.getPostStatsByYear(2024)).thenReturn(stats);

        List<Object[]> result = statsService.getPostStatsByYear(2024);

        assertEquals(1, result.size());
        verify(statsRepo).getPostStatsByYear(2024);
    }

    @Test
    public void testGetPostStatsByQuarter_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 2, 120 });
        when(statsRepo.getPostStatsByQuarter(2024, 2)).thenReturn(stats);

        List<Object[]> result = statsService.getPostStatsByQuarter(2024, 2);

        assertEquals(1, result.size());
        verify(statsRepo).getPostStatsByQuarter(2024, 2);
    }

    @Test
    public void testGetPostStatsByMonth_Success() {
        List<Object[]> stats = Collections.singletonList(new Object[] { 6, 45 });
        when(statsRepo.getPostStatsByMonth(2024, 6)).thenReturn(stats);

        List<Object[]> result = statsService.getPostStatsByMonth(2024, 6);

        assertEquals(1, result.size());
        verify(statsRepo).getPostStatsByMonth(2024, 6);
    }
}
