package com.uon.KCBMobileApis.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SMS_TO_CUSTOMERS")
public class SMSToCustomers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DBP_TRANSACTION_REFERENCE")
    private String dbpTransactionReference;

    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "SENT")
    private Boolean sent;

    @Column(name = "FAIL_REASON")
    private String failReason;

    @Column(name = "LAST_RETRY_DATE")
    private LocalDateTime dateRetried;

    @Column(name = "SEND_RETRIES")
    private int sendRetries;

    public SMSToCustomers(String dbpTransactionReference, LocalDateTime dateCreated, String phoneNumber, String message, String operator, Boolean sent) {
        this.dbpTransactionReference = dbpTransactionReference;
        this.dateCreated = dateCreated;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.operator = operator;
        this.sent = sent;
    }
}