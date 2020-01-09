package com.infomind.axboot.domain.semester;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends AXBootJPAQueryDSLRepository<Semester, Semester.SemesterId> {
}
