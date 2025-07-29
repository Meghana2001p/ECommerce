package com.project.E_Commerce.Repository.email;

import com.project.E_Commerce.Entity.Email.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationRepo extends JpaRepository<EmailNotification,Integer> {
}
