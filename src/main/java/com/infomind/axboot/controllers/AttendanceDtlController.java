package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.attendanceDtl.AttendanceView;
import com.infomind.axboot.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.infomind.axboot.domain.attendanceDtl.AttendanceDtl;
import com.infomind.axboot.domain.attendanceDtl.AttendanceDtlService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/attendancedtl")
public class AttendanceDtlController extends BaseController {

    @Inject
    private AttendanceDtlService attendanceDtlService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<AttendanceDtl> requestParams) {
        List<AttendanceDtl> list = attendanceDtlService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<AttendanceDtl> request) {
        attendanceDtlService.save(request);
        return ok();
    }

    @RequestMapping(value="/viewList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse viewList(RequestParams<Map> requestParams) {
        List<AttendanceView> list = attendanceDtlService.viewList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value="/dateList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse dateList(RequestParams<Map> requestParams) {
        List<AttendanceView> list = attendanceDtlService.dateList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/atdtMissExcel", method = RequestMethod.POST)
    public void atdtExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());
        attendanceDtlService.missAtdtExcel(map, response);

    }
}