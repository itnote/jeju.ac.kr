package com.infomind.axboot.domain.report;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSampleRepository extends AXBootJPAQueryDSLRepository<ReportSample, Integer> {
}
