package com.project.E_Commerce.Repository.User;

import com.project.E_Commerce.Entity.User.UserEmailPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEmailPreferencesRepo extends JpaRepository<UserEmailPreferences,Integer>
{

    //1.create by the save
    //2update
    //3.delete
    //4.getAll gets all the data
    //5.getByjust the Id
    //6.remove preferences or delete the preference

    //all the email preferences
    @Query("select u from UserEmailPreferences u  where u.user.id=:userId")
    List<UserEmailPreferences> findByUserId(@Param("userId") Integer userId);

    //  Get specific preference
    @Query("select u from UserEmailPreferences u  where u.user.id=:userId and u.emailType=:emailType")
    Optional<UserEmailPreferences> findByUserIdAndEmailType(@Param("userId") Integer userId,@Param("emailType") UserEmailPreferences.EmailType emailType);

    // ✅ Update subscription status (custom query)
    @Modifying
    @Query("UPDATE UserEmailPreferences p SET p.isSubscribed = :isSubscribed WHERE p.user.id = :userId AND p.emailType = :emailType")
    int updateSubscriptionStatus(@Param("userId") int userId,
                                 @Param("emailType") UserEmailPreferences.EmailType emailType,
                                 @Param("isSubscribed") boolean isSubscribed);

    //  Delete preference
    @Query("delete from UserEmailPreferences u where u.user.id=:userId  and u.emailType=:emailType ")
    int  deleteByUserIdAndEmailType(Integer userId, UserEmailPreferences.EmailType emailType);

    @Modifying
    @Query("DELETE FROM UserEmailPreferences uep WHERE uep.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);



    @Query("SELECT uep FROM UserEmailPreferences uep WHERE uep.user.id = :userId")
    List<UserEmailPreferences> findAllByUserId(@Param("userId")Integer userId);

}
