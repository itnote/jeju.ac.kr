package com.infomind.axboot.domain.scoreMst;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreMstRepository extends AXBootJPAQueryDSLRepository<ScoreMst, ScoreMst.ScoreMstId> {
}
