package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.infomind.axboot.domain.subject.Subject;
import com.infomind.axboot.domain.subject.SubjectService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/subject")
public class SubjectController extends BaseController {

    @Inject
    private SubjectService subjectService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "semeYear", value = "학년도", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "semeSeq", value = "학기시퀀스", dataType = "String", paramType = "query")
    })
    public Responses.ListResponse list(RequestParams<Subject> requestParams) {
        List<Subject> list = subjectService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@Valid @NotNull @RequestBody List<Subject> request) {
        subjectService.subjectSave(request);
        return ok();
    }
}