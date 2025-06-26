package com.project.E_Commerce.Mapper;

//import com.project.E_Commerce.Entity.UserEmailPreference;
import com.project.E_Commerce.Entity.UserEmailPreferences;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserEmailPreferencesMapper {

    // ✅ Insert a new preference
    @Insert("""
        INSERT INTO user_email_preferences (user_id, email_type, is_subscribed)
        VALUES (#{userId}, #{emailType}, #{isSubscribed})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createPreference(UserEmailPreferences pref);

    // ✅ Update subscription status
    @Update("""
        UPDATE user_email_preferences
        SET is_subscribed = #{isSubscribed}
        WHERE user_id = #{userId} AND email_type = #{emailType}
    """)
    int updateSubscriptionStatus(@Param("userId") int userId,
                                 @Param("emailType") String emailType,
                                 @Param("isSubscribed") boolean isSubscribed);

    // ✅ Get all preferences for a user
    @Select("SELECT * FROM user_email_preferences WHERE user_id = #{userId}")
    List<UserEmailPreferences> getPreferencesByUserId(@Param("userId") int userId);

    // ✅ Get one specific preference
    @Select("""
        SELECT * FROM user_email_preferences
        WHERE user_id = #{userId} AND email_type = #{emailType}
    """)
    UserEmailPreferences getPreference(@Param("userId") int userId,
                                      @Param("emailType") String emailType);

    // ✅ Optional: Delete a preference
    @Delete("""
        DELETE FROM user_email_preferences
        WHERE user_id = #{userId} AND email_type = #{emailType}
    """)
    int deletePreference(@Param("userId") int userId,
                         @Param("emailType") String emailType);


}
