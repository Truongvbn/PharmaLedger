package com.pharmaledger.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Blockchain Anchor Data Transfer Object
 */
public class BlockchainAnchorDTO implements Serializable {

    private int anchorId;
    private String referenceType;
    private int referenceId;
    private String dataHash;
    private String txHash;
    private Long blockNumber;
    private String networkName;
    private String status;
    private Date anchoredAt;
    private Date confirmedAt;

    public BlockchainAnchorDTO() {
    }

    // Getters and Setters
    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAnchoredAt() {
        return anchoredAt;
    }

    public void setAnchoredAt(Date anchoredAt) {
        this.anchoredAt = anchoredAt;
    }

    public Date getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Date confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
