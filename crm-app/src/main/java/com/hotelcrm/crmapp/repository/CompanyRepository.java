package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.dto.company.CompanySummaryDto;
import com.hotelcrm.crmapp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    @Query("""
        SELECT new com.hotelcrm.crmapp.dto.company.CompanySummaryDto(
            c.id,
            c.name,
            u.username,
            (
              SELECT MAX(i.scheduledAt)
              FROM Interaction i
              WHERE i.contactPerson.company = c
            ),
            (
              SELECT COALESCE(SUM(e1.estimatedTotalGrossRevenue), 0)
              FROM Event e1
              WHERE e1.company = c
                AND year(e1.eventDate) = :ytdYear
            ),
            (
              SELECT COALESCE(SUM(e2.estimatedTotalGrossRevenue), 0)
              FROM Event e2
              WHERE e2.company = c
                AND year(e2.eventDate) = :lyYear
            )
        )
        FROM Company c
        JOIN c.createdBy u
        """)
    List<CompanySummaryDto> fetchCompanySummaryTable(
            @Param("ytdYear") int ytdYear,
            @Param("lyYear") int lyYear
    );
}
