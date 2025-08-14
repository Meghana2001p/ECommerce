package com.project.E_Commerce.ServiceImplementation.Notification;

import com.project.E_Commerce.Entity.Email.NotificationQueue;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Repository.Notification.NotificationQueueRepo;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Service.Notification.NotificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationQueueRepo notificationQueueRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void processPendingEmails() {
        List<NotificationQueue> pendingEmails = notificationQueueRepo
                .findByStatusAndType(NotificationQueue.NotificationStatus.PENDING, NotificationQueue.NotificationType.EMAIL);

        if (pendingEmails.isEmpty()) {
            logger.info("No pending emails found.");
            return;
        }

        for (NotificationQueue notification : pendingEmails) {
            try {
                User user = userRepo.findById(notification.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Order Notification");
                message.setText(notification.getMessage());

                logger.info(" Sending email to: " + user.getEmail());
                mailSender.send(message);
                logger.info("Email sent successfully to " + user.getEmail());

                notification.setStatus(NotificationQueue.NotificationStatus.SENT);
                notificationQueueRepo.save(notification);

            } catch (Exception e) {
                logger.info("Failed to send email: " + e.getMessage());

                e.printStackTrace();
                notification.setStatus(NotificationQueue.NotificationStatus.FAILED);
                notificationQueueRepo.save(notification);
            }
        }
    }
}
