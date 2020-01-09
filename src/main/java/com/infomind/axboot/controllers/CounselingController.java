package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.classDtl.ClassDtl;
import com.infomind.axboot.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.infomind.axboot.domain.counseling.Counseling;
import com.infomind.axboot.domain.counseling.CounselingService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/counseling")
public class CounselingController extends BaseController {

    @Inject
    private CounselingService counselingService;

    @RequestMapping(value="/getCounList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getCounList(RequestParams<Map> requestParams) {
        List<ClassDtl> list = counselingService.getCounList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<Counseling> requestParams) {
        List<Counseling> list = counselingService.getStudentCounList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody Counseling request) {
        counselingService.save(request);
        return ok();
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON,value = "/deleteCoun")
    public ApiResponse deleteCoun(@RequestBody Counseling request) {

        counselingService.deleteCoun(request);
        return ok();
    }

    @RequestMapping(value = "/tchrCounExcel", method = RequestMethod.POST)
    public void tchrCounExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());

        counselingService.tchrCounExcel(map, response);
    }
}