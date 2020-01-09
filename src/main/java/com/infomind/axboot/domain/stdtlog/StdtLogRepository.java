package com.infomind.axboot.domain.stdtlog;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StdtLogRepository extends AXBootJPAQueryDSLRepository<StdtLog, Long> {
    List<StdtLog> findByStdtId(String stdtId);
}
