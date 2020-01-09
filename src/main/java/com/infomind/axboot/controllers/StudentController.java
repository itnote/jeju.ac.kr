package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.file.CommonFileService;
import com.infomind.axboot.domain.student.Student;
import com.infomind.axboot.domain.student.StudentExcelServiceImpl;
import com.infomind.axboot.utils.JsonUtils;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.infomind.axboot.domain.student.StudentService;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/student")
@Slf4j
public class StudentController extends BaseController {
    @Value("${axboot.excel.upload.repository}")
    public String uploadRepository;

    @Inject
    private StudentService studentService;

    @Inject
    private StudentExcelServiceImpl studentExcelService;

    @Inject
    private CommonFileService commonFileService;


    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stdtId", value = "학번", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "lang.kor", value = "이름", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "검색어", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list(RequestParams<Student> requestParams) {
        List<Student> students = studentService.get(requestParams);
        return Responses.ListResponse.of(students);
    }

//    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON, params = "stdtId")
//    public Student get(RequestParams requestParams) {
//        return jnuStudentService.getStudent(requestParams);
//    }


    @RequestMapping(method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public ApiResponse save(@Valid @RequestBody List<Student> students) throws Exception {
        studentService.saveStudent(students);
        return ok();
    }

    @ResponseBody
    @RequestMapping(value = "/excelUpload", method = {RequestMethod.POST},  produces = APPLICATION_JSON)
    public Responses.ListResponse excelUpload(@RequestParam(value = "excelFile") MultipartFile excelFile) throws Exception {
        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        File destFile = commonFileService.multiPartFileToFile(excelFile);

        List<Student> studentList = studentExcelService.excelFileRead(destFile);
        destFile.delete();

        return Responses.ListResponse.of(studentList);
    }

    @RequestMapping(value="/studentList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getStudentModalList(RequestParams<Student> requestParams) {
        List<Student> students = studentService.getStudentList(requestParams);
        return Responses.ListResponse.of(students);
    }


    @RequestMapping(value="/counStudentList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse getCounStudentList(RequestParams<Student> requestParams) {
        List<Student> students = studentService.getCounStudentList(requestParams);
        return Responses.ListResponse.of(students);
    }

    @RequestMapping(value = "/studentExcel", method = RequestMethod.POST)
    public void studentExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        Map sch = JsonUtils.jsonToAny(data, new TypeToken<Map>() {}.getType());
        String path = request.getSession().getServletContext().getRealPath("/");
        sch.put("path", path);

        studentService.studentExcel(sch, response);
    }

}