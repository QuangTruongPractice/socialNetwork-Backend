package com.tqt.services;

import com.tqt.pojo.SurveyOption;
import com.tqt.repositories.SurveyOptionRepository;
import com.tqt.services.impl.SurveyOptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurveyOptionServiceTest extends BaseServiceTest {

    @Mock
    private SurveyOptionRepository soRepo;

    @InjectMocks
    private SurveyOptionServiceImpl surveyOptionService;

    @Test
    public void testGetSurveyOptionById_Success() {
        SurveyOption option = new SurveyOption();
        when(soRepo.getSurveyOptionById(1)).thenReturn(option);

        SurveyOption result = surveyOptionService.getSurveyOptionById(1);

        assertEquals(option, result);
        verify(soRepo).getSurveyOptionById(1);
    }

    @Test
    public void testAddOrUpdateSurveyOption_Success() {
        SurveyOption option = new SurveyOption();
        doNothing().when(soRepo).addOrUpdateSurveyOption(option);

        surveyOptionService.addOrUpdateSurveyOption(option);

        verify(soRepo).addOrUpdateSurveyOption(option);
    }
}
