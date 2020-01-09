package com.infomind.axboot.domain.counseling;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselingRepository extends AXBootJPAQueryDSLRepository<Counseling, Long> {
}
