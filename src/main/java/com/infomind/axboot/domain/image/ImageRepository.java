package com.infomind.axboot.domain.image;

import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends AXBootJPAQueryDSLRepository<Image, Long> {
    Image deleteById(Long id);
}
