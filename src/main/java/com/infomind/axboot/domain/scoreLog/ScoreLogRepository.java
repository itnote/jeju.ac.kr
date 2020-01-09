package com.infomind.axboot.domain.scoreLog;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreLogRepository extends AXBootJPAQueryDSLRepository<ScoreLog, Long> {
}
