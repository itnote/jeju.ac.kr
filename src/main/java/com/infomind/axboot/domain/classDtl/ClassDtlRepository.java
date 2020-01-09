package com.infomind.axboot.domain.classDtl;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassDtlRepository extends AXBootJPAQueryDSLRepository<ClassDtl, ClassDtl.ClassDtlId> {
}
