package com.infomind.axboot.domain.attendanceDtl;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceDtlRepository extends AXBootJPAQueryDSLRepository<AttendanceDtl, AttendanceDtl.AttendanceDtlId> {
}
