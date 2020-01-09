package com.infomind.axboot.domain.docLog;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocLogRepository extends AXBootJPAQueryDSLRepository<DocLog, Long> {
}
