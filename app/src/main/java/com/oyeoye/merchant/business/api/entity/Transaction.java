package com.oyeoye.merchant.business.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author jfcartier
 * @since 15-09-26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {
    @JsonProperty("_id")
    public String id;
    @JsonProperty("client")
    public String clientId;
    @JsonProperty("deal")
    public Deal deal;
    @JsonProperty("merchant")
    public String merchantId;
    @JsonProperty("paymentAuthorizationId")
    public String paymentAuthorizationId;
    @JsonProperty("paymentStatus")
    public String paymentStatus;
    @JsonProperty("amount")
    public Float amount;
    @JsonProperty("reference")
    public String reference;
    @JsonProperty("key")
    public String key;
    @JsonProperty("status")
    public Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPaymentAuthorizationId() {
        return paymentAuthorizationId;
    }

    public void setPaymentAuthorizationId(String paymentAuthorizationId) {
        this.paymentAuthorizationId = paymentAuthorizationId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
