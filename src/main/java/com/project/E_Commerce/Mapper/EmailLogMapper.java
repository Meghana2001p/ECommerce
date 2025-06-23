package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.EmailLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmailLogMapper {

    // 1. Insert a new email log entry
    @Insert("INSERT INTO email_log (user_id, email_type, order_id, recipient_email, sent_at, status, error_message) " +
            "VALUES (#{userId}, #{emailType}, #{orderId}, #{recipientEmail}, #{sentAt}, #{status}, #{errorMessage})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertEmailLog(EmailLog emailLog);

    // 2. Get all logs for a user
    @Select("SELECT * FROM email_log WHERE user_id = #{userId} ORDER BY sent_at DESC")
    List<EmailLog> getLogsByUserId(@Param("userId") int userId);

    // 3. Get logs for a specific order (optional)
    @Select("SELECT * FROM email_log WHERE order_id = #{orderId} ORDER BY sent_at DESC")
    List<EmailLog> getLogsByOrderId(@Param("orderId") int orderId);

    // 4. Get recent failed emails (for retries or monitoring)
    @Select("SELECT * FROM email_log WHERE status = 'FAILED' ORDER BY sent_at DESC")
    List<EmailLog> getFailedEmailLogs();

    // 5. Update status (e.g., from QUEUED to SENT or RETRYING)
    @Update("UPDATE email_log SET status = #{status}, error_message = #{errorMessage} WHERE id = #{id}")
    void updateEmailStatus(@Param("id") int id, @Param("status") String status, @Param("errorMessage") String errorMessage);
}
