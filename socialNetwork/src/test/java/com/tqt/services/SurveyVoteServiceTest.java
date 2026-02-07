package com.tqt.services;

import com.tqt.pojo.SurveyVote;
import com.tqt.repositories.SurveyVoteRepository;
import com.tqt.services.impl.SurveyVoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurveyVoteServiceTest extends BaseServiceTest {

    @Mock
    private SurveyVoteRepository svRepo;

    @InjectMocks
    private SurveyVoteServiceImpl surveyVoteService;

    @Test
    public void testExistsByUserIdAndPostId_Success() {
        when(svRepo.existsByUserIdAndPostId(1, 2)).thenReturn(true);

        boolean result = surveyVoteService.existsByUserIdAndPostId(1, 2);

        assertTrue(result);
        verify(svRepo).existsByUserIdAndPostId(1, 2);
    }

    @Test
    public void testAddOrUpdateSurveyVote_Success() {
        SurveyVote vote = new SurveyVote();
        doNothing().when(svRepo).addOrUpdateSurveyVote(vote);

        surveyVoteService.addOrUpdateSurveyVote(vote);

        verify(svRepo).addOrUpdateSurveyVote(vote);
    }
}
