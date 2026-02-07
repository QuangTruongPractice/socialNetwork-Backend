package com.tqt.services;

import com.tqt.pojo.PostRecipient;
import com.tqt.repositories.PostRecipientRepository;
import com.tqt.services.impl.PostRecipientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class PostRecipientServiceTest extends BaseServiceTest {

    @Mock
    private PostRecipientRepository prRepo;

    @InjectMocks
    private PostRecipientServiceImpl postRecipientService;

    @Test
    public void testAddOrUpdatePostRecipient_Success() {
        PostRecipient pr = new PostRecipient();
        doNothing().when(prRepo).addOrUpdatePostRecipient(pr);

        postRecipientService.addOrUpdatePostRecipient(pr);

        verify(prRepo).addOrUpdatePostRecipient(pr);
    }
}
