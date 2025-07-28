package com.hotelcrm.crmapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CompanySummaryDto {
    Long companyId;
    String companyName;
    String ownerUsername;
    LocalDateTime lastContactDate;
    BigDecimal totalRevenueYTD;
    BigDecimal totalRevenueLY;

    public CompanySummaryDto(Long companyId, String companyName, String ownerUsername, LocalDateTime lastContactDate, BigDecimal totalRevenueYTD, BigDecimal totalRevenueLY) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.ownerUsername = ownerUsername;
        this.lastContactDate = lastContactDate;
        this.totalRevenueYTD = totalRevenueYTD;
        this.totalRevenueLY = totalRevenueLY;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public BigDecimal getTotalRevenueYTD() {
        return totalRevenueYTD;
    }

    public void setTotalRevenueYTD(BigDecimal totalRevenueYTD) {
        this.totalRevenueYTD = totalRevenueYTD;
    }

    public BigDecimal getTotalRevenueLY() {
        return totalRevenueLY;
    }

    public void setTotalRevenueLY(BigDecimal totalRevenueLY) {
        this.totalRevenueLY = totalRevenueLY;
    }

    public LocalDateTime getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(LocalDateTime lastContactDate) {
        this.lastContactDate = lastContactDate;
    }
}
