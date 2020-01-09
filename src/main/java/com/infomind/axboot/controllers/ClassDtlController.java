package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.classDtl.ClassDtlExcelServiceImpl;
import com.infomind.axboot.domain.classMst.ClassMst;
import com.infomind.axboot.domain.file.CommonFileService;
import com.infomind.axboot.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.infomind.axboot.domain.classDtl.ClassDtl;
import com.infomind.axboot.domain.classDtl.ClassDtlService;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/classdtl")
public class ClassDtlController extends BaseController {
    @Value("${axboot.excel.upload.repository}")
    public String uploadRepository;

    @Inject
    private ClassDtlService classDtlService;

    @Inject
    private CommonFileService commonFileService;

    @Inject
    private ClassDtlExcelServiceImpl classDtlExcelService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<ClassDtl> requestParams) {
        List<ClassDtl> list = classDtlService.getClassStudentList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<ClassDtl> request) {
        classDtlService.save(request);
        return ok();
    }

    @RequestMapping(value="/excelStudentSave", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public ApiResponse saveExcel(@RequestBody List<ClassDtl> request) {

        return  ok(classDtlService.saveExcel(request));
    }

    @ResponseBody
    @RequestMapping(value = "/excelUpload", method = {RequestMethod.POST},  produces = APPLICATION_JSON)
    public Responses.ListResponse excelUpload(@RequestParam(value = "excelFile") MultipartFile excelFile, RequestParams<ClassDtl> requestParams) throws Exception {

        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        File destFile = commonFileService.multiPartFileToFile(excelFile);

        List<ClassDtl> classDtlList = classDtlExcelService.excelFileRead(destFile);
        destFile.delete();

        return Responses.ListResponse.of(classDtlList);
    }

    @RequestMapping(value="/atdcDtlList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getAttendanceDtl(RequestParams<ClassDtl> requestParams) {
        List<ClassDtl> students = classDtlService.getAttendanceDtl(requestParams);
        return Responses.ListResponse.of(students);
    }

    @RequestMapping(value = "/clasStdtExcel", method = RequestMethod.POST)
    public void clasStdtExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");

        List<ClassMst> classMstList = JsonUtils.jsonToAny(data, new TypeToken<List<ClassMst>>() {}.getType());

        classDtlService.getClasStdtExcel(classMstList, response);
    }

    @RequestMapping(value = "/getClassStudentExcel", method = RequestMethod.POST)
    public void classStudentExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>() {}.getType());

        classDtlService.getExcelClassStudentList(map, response);
    }



}