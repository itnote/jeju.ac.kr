package com.infomind.axboot.domain.testM;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMRepository extends AXBootJPAQueryDSLRepository<TestM, Integer> {
}
