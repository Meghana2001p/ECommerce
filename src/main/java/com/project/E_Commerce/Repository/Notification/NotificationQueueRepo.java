package com.project.E_Commerce.Repository.Notification;

import com.project.E_Commerce.Entity.Email.NotificationQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationQueueRepo extends JpaRepository<NotificationQueue,Integer> {
    List<NotificationQueue> findByStatusAndType(NotificationQueue.NotificationStatus notificationStatus, NotificationQueue.NotificationType notificationType);
}
