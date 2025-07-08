package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.NotificationQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationQueueRepo extends JpaRepository<NotificationQueue,Integer> {
}
