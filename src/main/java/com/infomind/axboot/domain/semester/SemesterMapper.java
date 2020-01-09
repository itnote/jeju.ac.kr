package com.infomind.axboot.domain.semester;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface SemesterMapper extends MyBatisMapper {

    List<Semester> getSemesterMgmtList(HashMap map);

    List<Map> getSemeYearCombo(HashMap map);

    List<Map> getSemeSeqCombo(HashMap map);

    List<SemesterScheduleExcel> scheduleExcel(Semester seme);

    Semester getSemesterAmt(HashMap map);
}
