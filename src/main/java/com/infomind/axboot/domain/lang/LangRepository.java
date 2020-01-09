package com.infomind.axboot.domain.lang;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LangRepository extends AXBootJPAQueryDSLRepository<Lang, Long> {
    List<Lang> findByLangKey(Long langKey);
}
