package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.infomind.axboot.domain.schedule.Schedule;
import com.infomind.axboot.domain.schedule.ScheduleService;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/schedule")
public class ScheduleController extends BaseController {

    @Inject
    private ScheduleService scheduleService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<Schedule> requestParams) {
        List<Schedule> list = scheduleService.getScheduleList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody Schedule schedule) {
        scheduleService.insertSchedule(schedule);
        return ok();
    }

    @RequestMapping(method = {RequestMethod.DELETE}, produces = APPLICATION_JSON)
    public ApiResponse delete(@RequestBody Schedule schedule) {
        scheduleService.deleteSchedule(schedule);
        return ok();
    }
}