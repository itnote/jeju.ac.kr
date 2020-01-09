package com.infomind.axboot.domain.scoreLog;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ScoreLogMapper extends MyBatisMapper {

    List<ScoreLog> getScoreLogList(Map map);

}
