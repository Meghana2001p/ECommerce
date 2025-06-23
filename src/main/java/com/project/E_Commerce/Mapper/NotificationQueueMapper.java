package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.NotificationQueue;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationQueueMapper {

    // 1. Insert a new notification into the queue
    @Insert("INSERT INTO notification_queue (user_id, type, message, status, scheduled_at) " +
            "VALUES (#{userId}, #{type}, #{message}, #{status}, #{scheduledAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNotification(NotificationQueue notification);

    // 2. Get all pending notifications (for scheduled processing)
    @Select("SELECT * FROM notification_queue WHERE status = 'PENDING' AND scheduled_at <= NOW()")
    List<NotificationQueue> getPendingNotifications();

    // 3. Update notification status (e.g., SENT, FAILED)
    @Update("UPDATE notification_queue SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") int id, @Param("status") String status);

    // 4. Get all notifications for a user
    @Select("SELECT * FROM notification_queue WHERE user_id = #{userId} ORDER BY scheduled_at DESC")
    List<NotificationQueue> getByUserId(@Param("userId") int userId);

    // 5. Delete notification by ID
    @Delete("DELETE FROM notification_queue WHERE id = #{id}")
    int deleteById(@Param("id") int id);
}
