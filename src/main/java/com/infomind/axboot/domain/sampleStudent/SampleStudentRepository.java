package com.infomind.axboot.domain.sampleStudent;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleStudentRepository extends AXBootJPAQueryDSLRepository<SampleStudent, Integer> {
    List<SampleStudent> findByStudentId(String studentId);
}
