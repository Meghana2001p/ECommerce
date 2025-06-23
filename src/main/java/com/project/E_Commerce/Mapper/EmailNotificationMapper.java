package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.EmailNotification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmailNotificationMapper {

    // 1. Get email template by type
    @Select("SELECT * FROM email_notification WHERE type = #{type}")
    EmailNotification getByType(@Param("type") String type);

    // 2. Get email template by ID
    @Select("SELECT * FROM email_notification WHERE id = #{id}")
    EmailNotification getById(@Param("id") int id);

    // 3. Insert new email template
    @Insert("INSERT INTO email_notification (type, subject, body_template, is_html) " +
            "VALUES (#{type}, #{subject}, #{bodyTemplate}, #{isHtml})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertEmailNotification(EmailNotification notification);

    // 4. Update existing template
    @Update("UPDATE email_notification SET subject = #{subject}, body_template = #{bodyTemplate}, is_html = #{isHtml} " +
            "WHERE id = #{id}")
    void updateEmailNotification(EmailNotification notification);

    // 5. Get all email templates
    @Select("SELECT * FROM email_notification")
    List<EmailNotification> getAllEmailNotifications();

    // 6. Delete a template
    @Delete("DELETE FROM email_notification WHERE id = #{id}")
    void deleteById(@Param("id") int id);
}
