package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.attendanceMst.AttendanceTchrView;
import com.infomind.axboot.domain.file.CommonFileService;
import com.infomind.axboot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.infomind.axboot.domain.attendanceMst.AttendanceMst;
import com.infomind.axboot.domain.attendanceMst.AttendanceMstService;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/attendancemst")
@Slf4j
public class AttendanceMstController extends BaseController {

    Logger logger = LoggerFactory.getLogger(AttendanceMstController.class);

    @Inject
    private AttendanceMstService attendanceMstService;
    @Inject
    private CommonFileService commonFileService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<AttendanceMst> requestParams) {
        List<AttendanceMst> list = attendanceMstService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<AttendanceMst> request) {
        attendanceMstService.save(request);
        return ok();
    }

    @RequestMapping(value = "/getTchrDiv", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.MapResponse getTchrDiv(RequestParams<AttendanceMst> requestParams) {
        Map map = attendanceMstService.getTchrDiv(requestParams);
        return map == null ? null : Responses.MapResponse.of(map);
    }

    @RequestMapping(value = "/atdcExcel", method = RequestMethod.POST)
    public void atdcExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());

        attendanceMstService.atdcExcel(map, response);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, produces = APPLICATION_JSON)
    public ApiResponse deleteAtdtMst(@RequestBody AttendanceMst request) {
        attendanceMstService.deleteAtdtMst(request);
        return ok();
    }

    @ResponseBody
    @RequestMapping(value = "/atdcUpload", method = {RequestMethod.POST},  produces = APPLICATION_JSON)
    public ApiResponse atdcUpload(@RequestParam(value = "excelFile") MultipartFile excelFile) throws Exception {
        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        File destFile = commonFileService.multiPartFileToFile(excelFile);

        attendanceMstService.atdcUpload(destFile);
        destFile.delete();

        return ok();
    }

    @RequestMapping(value = "/tchrAtdcList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse tchrAtdcList(RequestParams<AttendanceTchrView> requestParams) {
        List<AttendanceTchrView> list = attendanceMstService.getTchrAtdcList(requestParams);
        return Responses.ListResponse.of(list);
    }


    @RequestMapping(value = "/tchrAtdcExcel", method = RequestMethod.POST)
    public void tchrAtdcExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());
        logger.info("##########map" + map);

        attendanceMstService.getTchrAtdcExcel(map, response);
    }
}