package com.infomind.axboot.domain.attendanceDtl;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.attendanceMst.AttendanceMst;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AttendanceDtlMapper extends MyBatisMapper {

    List<AttendanceDtl> getAtdcDtl(HashMap map);

    void deleteAtdcDtl(AttendanceDtl attendanceDtl);

    List<AttendanceView> viewList(HashMap map);

    List<AttendanceView> dateList(HashMap map);

    List<AttendanceMissExcel> getMissAtdtExcel(HashMap map);
}
