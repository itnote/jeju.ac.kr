package com.infomind.axboot.domain.scoreDtl;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreDtlRepository extends AXBootJPAQueryDSLRepository<ScoreDtl, ScoreDtl.ScoreDtlId> {
}
