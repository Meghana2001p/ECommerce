package com.project.E_Commerce.Scheduler;

import com.project.E_Commerce.Entity.Discount;
import com.project.E_Commerce.Repository.DiscountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DiscountExpiryScheduler {

    @Autowired
    private DiscountRepo discountRepo;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void expireOldDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> expiredDiscounts = discountRepo.findAllByIsActiveTrueAndEndDateBefore(now);

        if (!expiredDiscounts.isEmpty()) {
            for (Discount discount : expiredDiscounts) {
                discount.setIsActive(false);
            }
            discountRepo.saveAll(expiredDiscounts);
            System.out.println("Expired " + expiredDiscounts.size() + " discounts at " + now);
        }
    }
}
