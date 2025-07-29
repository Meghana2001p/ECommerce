package com.project.E_Commerce.Repository.email;

import com.project.E_Commerce.Entity.Email.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepo extends JpaRepository<EmailLog,Integer> {
}
