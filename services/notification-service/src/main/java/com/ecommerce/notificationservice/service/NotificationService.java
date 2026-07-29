package com.ecommerce.notificationservice.service;

import com.ecommerce.notificationservice.dto.NotificationResponse;
import com.ecommerce.notificationservice.dto.SendNotificationRequest;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponse sendNotification(SendNotificationRequest request);
    NotificationResponse getNotificationById(UUID id);
    List<NotificationResponse> getNotificationsByUserId(UUID userId);
    List<NotificationResponse> getUnreadNotifications(UUID userId);
    NotificationResponse markAsRead(UUID id);
    long countUnread(UUID userId);
}
