package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.dto.CompanySummaryDto;
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
            SELECT new com.hotelcrm.crmapp.dto.CompanySummaryDto(
                        c.id,
                        c.name,
                        u.username,
                        (
                        SELECT MAX(i.interactionDate)
                        FROM Interaction i
                        WHERE i.contactPerson.company = c
                                    ),
                        (
                        SELECT COALESCE(SUM(e.estimatedTotalGrossRevenue), 0)
                        FROM Event e
                        WHERE e.company = c
                        AND EXTRACT(YEAR FROM e.eventDate) = :ytdYear
                                    ),
                        (
                        SELECT COALESCE(SUM(e.estimatedTotalGrossRevenue), 0)
                        FROM Event e
                        WHERE e.company = c
                        AND EXTRACT(YEAR FROM e.eventDate) = :lyYear
                                                  )
                                                               )
                        from Company c
                        join c.createdBy u
            
            """)
    List<CompanySummaryDto> fetchCompanySummaryTable(
            @Param("ytdYear") int ytdYear,
            @Param("lyYear") int lyYear
    );


}
