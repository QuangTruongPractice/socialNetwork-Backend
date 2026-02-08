package com.tqt.services;

import com.tqt.pojo.Notification;
import com.tqt.repositories.NotificationRepository;
import com.tqt.services.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceTest extends BaseServiceTest {

    @Mock
    private NotificationRepository notiRepo;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    public void testGetNotificationByUserId_Success() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notiRepo.getNotificationByUserId(1)).thenReturn(notifications);

        List<Notification> result = notificationService.getNotificationByUserId(1);

        assertEquals(2, result.size());
        verify(notiRepo).getNotificationByUserId(1);
    }

    @Test
    public void testGetNotificationById_Success() {
        Notification notification = new Notification();
        when(notiRepo.getNotificationById(1)).thenReturn(notification);

        Notification result = notificationService.getNotificationById(1);

        assertEquals(notification, result);
        verify(notiRepo).getNotificationById(1);
    }

    @Test
    public void testAddOrUpdateNotification_Success() {
        Notification notification = new Notification();
        doNothing().when(notiRepo).addOrUpdateNotification(notification);

        notificationService.addOrUpdateNotification(notification);

        verify(notiRepo).addOrUpdateNotification(notification);
    }

    @Test
    public void testRead_ShouldMarkAsRead() {
        Notification notification = new Notification();
        notification.setRead(false);

        when(notiRepo.getNotificationById(1)).thenReturn(notification);
        doNothing().when(notiRepo).addOrUpdateNotification(any(Notification.class));

        notificationService.read(1);

        assertTrue(notification.isRead());
        verify(notiRepo).getNotificationById(1);
        verify(notiRepo).addOrUpdateNotification(notification);
    }

    @Test
    public void testRead_AlreadyRead_ShouldStillUpdate() {
        Notification notification = new Notification();
        notification.setRead(true); // Already read

        when(notiRepo.getNotificationById(1)).thenReturn(notification);
        doNothing().when(notiRepo).addOrUpdateNotification(any(Notification.class));

        notificationService.read(1);

        assertTrue(notification.isRead()); // Still true
        verify(notiRepo).getNotificationById(1);
        verify(notiRepo).addOrUpdateNotification(notification);
    }
}
