package com.infomind.axboot.domain.attendanceMst;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceMstRepository extends AXBootJPAQueryDSLRepository<AttendanceMst, AttendanceMst.AttendanceMstId> {
}
