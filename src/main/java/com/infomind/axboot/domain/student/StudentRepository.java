package com.infomind.axboot.domain.student;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends AXBootJPAQueryDSLRepository<Student, String> {
    List<Student> findByStdtId(String stdtId);
    Boolean existsByStdtNm(Long stdtNm);
    Boolean existsByAddr(String addr);

    @Query("select s from Student s join fetch s.lang order by s.stdtId")
    List<Student> findAllJoinFetch();
}
