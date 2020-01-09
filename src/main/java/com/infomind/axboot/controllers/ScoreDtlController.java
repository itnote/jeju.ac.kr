package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.infomind.axboot.domain.scoreLog.ScoreLog;
import com.infomind.axboot.domain.scoreLog.ScoreLogService;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.infomind.axboot.domain.scoreDtl.ScoreDtl;
import com.infomind.axboot.domain.scoreDtl.ScoreDtlService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/scoredtl")
public class ScoreDtlController extends BaseController {

    @Inject
    private ScoreDtlService scoreDtlService;

    @Inject
    private ScoreLogService scoreLogService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<Map> requestParams) {
        List<ScoreDtl> list = scoreDtlService.getScoreDtlList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<ScoreDtl> request) {
        scoreDtlService.save(request);

        List<ScoreLog> scorLogList = new ArrayList();
        for (ScoreDtl dtl : request) {
            if (dtl.getOriScor() != null && (dtl.getOriScor() != dtl.getScor() || dtl.getOriPerf() != dtl.getPerf())) {
                ScoreLog scoreLog = new ScoreLog();
                scoreLog.setSemeYear(dtl.getSemeYear());
                scoreLog.setSemeSeq(dtl.getSemeSeq());
                scoreLog.setPeriodCd(dtl.getPeriodCd());
                scoreLog.setClasSeq(dtl.getClasSeq());
                scoreLog.setStdtId(dtl.getStdtId());
                scoreLog.setSbjtId(dtl.getSbjtId());
                scoreLog.setExamCd(dtl.getExamCd());
                scoreLog.setOldScor(dtl.getOriScor().add(dtl.getOriPerf()));
                scoreLog.setNewScor(dtl.getScor().add(dtl.getPerf()));
                scoreLog.setReason(dtl.getReason());

                scorLogList.add(scoreLog);
            }
        }
        scoreLogService.save(scorLogList);

        return ok();
    }
}