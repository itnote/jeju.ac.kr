package com.infomind.axboot.domain.schedule;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends AXBootJPAQueryDSLRepository<Schedule, Schedule.ScheduleId> {
}
