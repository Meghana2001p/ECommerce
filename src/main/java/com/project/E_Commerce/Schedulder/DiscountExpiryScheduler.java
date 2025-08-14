package com.project.E_Commerce.Schedulder;

import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Repository.Product.DiscountRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DiscountExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(DiscountExpiryScheduler.class);


    @Autowired
    private DiscountRepo discountRepo;

    @Scheduled(cron = "0 0 0/5 * * ?")//runs every 5 hours
    public void expireOldDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> expiredDiscounts = discountRepo.findAllByIsActiveTrueAndEndDateBefore(now);

        if (!expiredDiscounts.isEmpty()) {
            for (Discount discount : expiredDiscounts) {
                discount.setIsActive(false);
            }
            discountRepo.saveAll(expiredDiscounts);
            log.info("Expired " + expiredDiscounts.size() + " discounts at " + now);
        }
    }
}
