package com.uon.KCBMobileApis.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "BANK_TO_MOBILE_TRANSACTIONS")
public class BankToMobileTransferRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DBP_UNIQUE_TRANSACTION_NUMBER", nullable = false, unique = true)
    private String dbpTransactionReference;

    @Column(name = "DEBIT_ACCOUNT_ID")
    private String debitAccountID;

    @Column(name = "DEBIT_ACCOUNT_MOBILE")
    private String debitAccountMobile;

    @Column(name = "CREDIT_ACCOUNT_ID")
    private String creditAccountID;

    @Column(name = "CREDIT_ACCOUNT_MOBILE")
    private String creditAccountMobile;

    @Column(name = "CREDIT_ACCOUNT_NAME")
    private String creditAccountName;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @Column(name = "RECEIVING_BANK_NAME")
    private String receivingBankName;

    @Column(name = "CHARGES_AMOUNT")
    private BigDecimal chargesAmount;

    @Column(name = "TRANS_CODE")
    private String transactionCode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TRANS_REF")
    private String transactionRef;

    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @UpdateTimestamp
    private LocalDateTime dateUpdated;
}