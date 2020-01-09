package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.utils.JsonUtils;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.infomind.axboot.domain.semester.Semester;
import com.infomind.axboot.domain.semester.SemesterService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/semester")
public class SemesterController extends BaseController {

    @Inject
    private SemesterService semesterService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "semeYear", value = "학년도", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "semeCd", value = "학기구분", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "semeSeq", value = "학기시퀀스", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list(RequestParams<Semester> requestParams) {
        List<Semester> list = semesterService.getSemesterMgmtList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<Semester> request) {
        Long semeSeq = semesterService.semeSave(request);
        return ok(semeSeq+"");
    }

    @RequestMapping(value="/yearCombo", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getSemeYearCombo(RequestParams<Map> requestParams) {
        List<Map> list = semesterService.getSemeYearCombo(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value="/semeSeqCombo", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getSemeSeqCombo(RequestParams<Map> requestParams) {
        List<Map> list = semesterService.getSemeSeqCombo(requestParams);
//        List<Map> list = semesterService.getSemeSeqCombo(requestParams.getString("schSemeYear"));
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/scheduleExcel", method = RequestMethod.POST)
    public void scheduleExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        List<Semester> list = JsonUtils.jsonToAny(data, new TypeToken<List<Semester>>() {}.getType());

        semesterService.scheduleExcel(list.get(0), response);
    }
}