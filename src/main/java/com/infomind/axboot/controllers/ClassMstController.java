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
import com.infomind.axboot.domain.classMst.ClassMst;
import com.infomind.axboot.domain.classMst.ClassMstService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/classmst")
public class ClassMstController extends BaseController {

    @Inject
    private ClassMstService classMstService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<ClassMst> requestParams) {
        List<ClassMst> list = classMstService.getClassList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody ClassMst request) {
        classMstService.classMstSave(request);
        return ok();
    }

    @RequestMapping(value="/periodCombo", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getPeriodCombo(RequestParams<Map> requestParams) {
        List<Map> list = classMstService.getPeriodCombo(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value="/clasSeqCombo", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getClasSeqCombo(RequestParams<Map> requestParams) {
        List<Map> list = classMstService.getClasSeqCombo(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/clasExcel", method = RequestMethod.POST)
    public void clasExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());

        classMstService.getClasExcel(map, response);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, produces = APPLICATION_JSON)
    public ApiResponse deleteClassMst(@RequestBody ClassMst classMst) {
        classMstService.deleteClassMst(classMst);
        return ok();
    }

}