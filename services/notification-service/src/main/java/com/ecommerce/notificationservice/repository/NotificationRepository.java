package com.ecommerce.notificationservice.repository;

import com.ecommerce.notificationservice.entity.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    Optional<Notification> findById(UUID id);
    List<Notification> findByUserId(UUID userId);
    List<Notification> findByUserIdAndStatus(UUID userId, String status);
    Notification save(Notification notification);
}
