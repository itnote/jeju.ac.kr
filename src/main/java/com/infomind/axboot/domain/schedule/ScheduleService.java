package com.infomind.axboot.domain.schedule;

import com.infomind.axboot.domain.semester.SemesterMapper;
import com.infomind.axboot.utils.SessionUtils;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;

import java.util.HashMap;
import java.util.List;

@Service
public class ScheduleService extends BaseService<Schedule, Schedule.ScheduleId> {
    private ScheduleRepository scheduleRepository;

    @Inject
    public ScheduleService(ScheduleRepository scheduleRepository) {
        super(scheduleRepository);
        this.scheduleRepository = scheduleRepository;
    }

    @Inject
    private ScheduleMapper scheMapper;

    public List<Schedule> gets(RequestParams<Schedule> requestParams) {
        return findAll();
    }

    public List<Schedule> getScheduleList(RequestParams<Schedule> requestParams) {
        String semeYear =requestParams.getString("semeYear","");
        String semeSeq = requestParams.getString("semeSeq","");
        String scheDt = requestParams.getString("scheDt","");

        HashMap map = new HashMap<String, Object>();
        map.put("semeYear", semeYear);
        map.put("semeSeq", semeSeq);
        map.put("scheDt", scheDt);

        return scheMapper.getScheduleList(map);
    }

    public void insertSchedule(Schedule schedule){
        HashMap map = new HashMap<String, Object>();
        map.put("semeYear", schedule.getSemeYear());
        map.put("semeSeq", schedule.getSemeSeq());
        map.put("scheDt", schedule.getScheDt());
        int selectSize = scheMapper.getScheduleList(map).size();

        schedule.setCreatedBy(SessionUtils.getCurrentLoginUserCd());
        schedule.setUpdatedBy(SessionUtils.getCurrentLoginUserCd());
        if(selectSize == 0){
            scheMapper.insertSchedule(schedule);
        }else{
            scheMapper.updateSchedule(schedule);
        }
    }

    public void deleteSchedule(Schedule schedule){
        scheMapper.deleteSchedule(schedule);
    }
}