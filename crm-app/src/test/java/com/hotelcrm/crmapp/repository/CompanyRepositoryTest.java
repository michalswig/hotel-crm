package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.dto.CompanySummaryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CompanySummaryQueryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testCompanySummaryQuery() {
        int currentYear = 2025;
        int lastYear = 2024;

        List<CompanySummaryDto> summaries =
                companyRepository.fetchCompanySummaryTable(currentYear, lastYear);

        for (CompanySummaryDto summary : summaries) {
            System.out.println("Company: " + summary.getCompanyName());
            System.out.println("Owner: " + summary.getOwnerUsername());
            System.out.println("Last Contact: " + summary.getLastContactDate());
            System.out.println("YTD Revenue: " + summary.getTotalRevenueYTD());
            System.out.println("LY Revenue: " + summary.getTotalRevenueLY());
            System.out.println("------------------------------");
        }
    }
}