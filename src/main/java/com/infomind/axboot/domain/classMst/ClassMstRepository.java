package com.infomind.axboot.domain.classMst;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassMstRepository extends AXBootJPAQueryDSLRepository<ClassMst, ClassMst.ClassMstId> {
}
