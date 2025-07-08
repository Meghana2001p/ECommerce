package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.DeliveryStatus;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DeliveryStatusRepo extends JpaRepository<DeliveryStatus,Integer> {
    @Modifying
    @Query("UPDATE DeliveryStatus ds SET ds.status = :status, ds.updatedAt = :updatedAt WHERE ds.order.id = :orderId")
    int updateDeliveryStatusByAgent(@Param("orderId") Integer orderId,
                                    @Param("status") DeliveryStatus.DeliveryState status,
                                    @Param("updatedAt") LocalDateTime updatedAt);



    @Modifying
    @Transactional
    @Query("UPDATE DeliveryStatus ds SET ds.status = :status, ds.carrier = :carrier, ds.updatedAt = CURRENT_TIMESTAMP WHERE ds.order.id = :orderId")
    int updateDeliveryStatusByAdmin(
            @Param("orderId") Integer orderId,
            @Param("status") DeliveryStatus.DeliveryState status,
            @Param("carrier") String carrier
    );

}
