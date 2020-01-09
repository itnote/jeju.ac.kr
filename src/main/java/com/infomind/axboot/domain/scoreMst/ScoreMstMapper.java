package com.infomind.axboot.domain.scoreMst;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.code.CommonCode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ScoreMstMapper extends MyBatisMapper {

    List<ScoreMst> getStudentScoreList(Map map);

    ScoreMst getEvalResult(ScoreMst scoreMst);

    List<ScoreExcel> getScoreExcel(Map map);

    List<CommonCode> getSemeSbjt(Map map);

    List<Map> getScorePeriod(Map map);

}
