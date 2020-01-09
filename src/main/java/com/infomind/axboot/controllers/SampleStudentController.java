package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.infomind.axboot.domain.report.ReportSampleService;
import com.infomind.axboot.domain.sampleStudent.SampleStudent;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.infomind.axboot.domain.sampleStudent.SampleStudentService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.infomind.axboot.domain.report.ReportSampleService.getDataSource;

/**
 * Memory Leak => GC 정상 작동.
 */

@Controller
@RequestMapping(value = "/api/v1/samplestudent")
@RequiredArgsConstructor
@Slf4j
public class SampleStudentController extends BaseController {

    @Inject
    private SampleStudentService sampleStudentService;
    private JRBeanCollectionDataSource dataSource;
    private final ReportSampleService reportSampleService;


    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "이름", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "studentId", value = "학번", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "filter", value = "검색어", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list(RequestParams<SampleStudent> requestParams) {
        List<SampleStudent> list = sampleStudentService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<SampleStudent> request) {
        sampleStudentService.save(request);
        return ok();
    }



    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST},value = "/report")
    @ResponseBody
    public ResponseEntity<byte[]> generatedReportGet(@RequestBody String studentParam) throws Exception {
        String studentId = "";

        int indexOf = studentParam.indexOf("=");
        if(indexOf > 0) {
            studentId = studentParam.split("=")[1];
        } else {
            studentId = studentParam;
        }
        dataSource = getDataSource(sampleStudentService.findStudent(studentId));
        Map<String, Object> params = new HashMap<>();
        params.put("student_id", studentId);
        byte[] bytes = reportSampleService.generatedToPdf("JNU_User_Report2", params, dataSource);

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "report_" + System.currentTimeMillis() + ".pdf\"")
                .body(bytes);
    }


    /**
     * 증명서가 여러 종류일 경우, Filename을 Path로 받는 방식..
     * studentId는 상황에 맞게 RequestBody로 대체 예정.
     * @param inputFileName : jrxml file(jasper report)
     * @param studentId : student id
     * @return : byte[], application/pdf
     * @throws Exception
     */
    @RequestMapping(value = "/report/{inputFileName}/{studentId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> testGeneratedReports(@PathVariable String inputFileName, @PathVariable String studentId) throws Exception {
        dataSource = getDataSource(sampleStudentService.findStudent(studentId));
        Map<String, Object> params = new HashMap<>();
        params.put("student_id", studentId);
        byte[] bytes = reportSampleService.generatedToPdf(inputFileName, params, dataSource);

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "report_" + System.currentTimeMillis() + ".pdf\"")
                .body(bytes);
    }








}