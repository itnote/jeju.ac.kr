package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.file.CommonFileService;
import com.infomind.axboot.domain.scoreDtl.ScoreDtlService;
import com.infomind.axboot.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.infomind.axboot.domain.scoreMst.ScoreMst;
import com.infomind.axboot.domain.scoreMst.ScoreMstService;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/scoremst")
public class ScoreMstController extends BaseController {

    @Inject
    private ScoreMstService scoreMstService;
    @Inject
    private CommonFileService commonFileService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<Map> requestParams) {
        List<ScoreMst> list = scoreMstService.getStudentScoreList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<ScoreMst> request) {
        scoreMstService.save(request);
        return ok();
    }

    @RequestMapping(value="/saveScore", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse saveScore(@RequestBody List<ScoreMst> request) {
        String msg = scoreMstService.saveScore(request);
        return ok(msg);
    }

    @RequestMapping(value = "/scoreExcel", method = RequestMethod.POST)
    public void scoreExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());

        scoreMstService.scoreExcel(map, response);
    }

    @ResponseBody
    @RequestMapping(value = "/scoreUpload", method = {RequestMethod.POST},  produces = APPLICATION_JSON)
    public ApiResponse scoreUpload(@RequestParam(value = "excelFile") MultipartFile excelFile) throws Exception {
        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        File destFile = commonFileService.multiPartFileToFile(excelFile);

        scoreMstService.scoreUpload(destFile);
        destFile.delete();

        return ok();
    }
}