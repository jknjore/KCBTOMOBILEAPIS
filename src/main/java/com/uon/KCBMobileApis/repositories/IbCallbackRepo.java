package com.uon.KCBMobileApis.repositories;

import com.uon.KCBMobileApis.entities.BankToMobileTransferRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IbCallbackRepo extends JpaRepository<BankToMobileTransferRequest,Long> {
    Optional<BankToMobileTransferRequest> findByTransactionRef(String ref);
    Optional<BankToMobileTransferRequest> findByDbpTransactionReference(String ref);
}
