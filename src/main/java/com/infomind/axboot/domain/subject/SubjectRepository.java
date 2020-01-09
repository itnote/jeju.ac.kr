package com.infomind.axboot.domain.subject;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends AXBootJPAQueryDSLRepository<Subject, Subject.SubjectId> {
}
