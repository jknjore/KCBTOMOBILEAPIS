package com.uon.KCBMobileApis.repositories;

import com.uon.KCBMobileApis.entities.SMSToCustomers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SMSToCustomersRepo extends JpaRepository<SMSToCustomers,Long> {
    @Query("SELECT s FROM SMSToCustomers s WHERE " +
            "s.sent = false AND " +
            "(s.sendRetries < :maxRetries) AND " +
            "s.dateCreated >= :minDateCreated AND " +
            "(s.dateRetried IS NULL OR s.dateRetried <= :maxDateRetried)")
    List<SMSToCustomers> findFailedSmsForRetry(
            @Param("maxRetries") int maxRetries,
            @Param("minDateCreated") LocalDateTime minDateCreated,
            @Param("maxDateRetried") LocalDateTime maxDateRetried
    );
}
