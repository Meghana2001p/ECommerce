package com.project.E_Commerce.Schedulder;

import com.project.E_Commerce.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class NotificationScheduler {

    @Autowired
    private NotificationService emailNotificationService;

    @Scheduled(fixedRate = 10000)
    public void sendPendingNotifications() {
        emailNotificationService.processPendingEmails();
    }
}
