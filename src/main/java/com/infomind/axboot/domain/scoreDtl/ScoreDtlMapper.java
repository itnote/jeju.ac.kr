package com.infomind.axboot.domain.scoreDtl;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ScoreDtlMapper extends MyBatisMapper {

    List<ScoreDtl> getScoreDtlList(Map map);

}
