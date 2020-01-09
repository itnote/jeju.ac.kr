package com.infomind.axboot.domain.schedule;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ScheduleMapper extends MyBatisMapper {

    List<Schedule> getScheduleList(HashMap map);
    void insertSchedule(Schedule schedule);
    void updateSchedule(Schedule schedule);
    void deleteSchedule(Schedule schedule);
}
