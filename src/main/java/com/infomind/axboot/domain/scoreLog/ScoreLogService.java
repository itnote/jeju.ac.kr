package com.infomind.axboot.domain.scoreLog;

import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreLogService extends BaseService<ScoreLog, Long> {
    private ScoreLogRepository scoreLogRepository;

    @Inject
    private ScoreLogMapper scoreLogMapper;

    @Inject
    public ScoreLogService(ScoreLogRepository scoreLogRepository) {
        super(scoreLogRepository);
        this.scoreLogRepository = scoreLogRepository;
    }

    public List<ScoreLog> gets(RequestParams<ScoreLog> requestParams) {
        return findAll();
    }

    public List<ScoreLog> getScoreLogList(RequestParams<Map> requestParams) {

        HashMap map = new HashMap<String, Object>();
        map.put("schClasSeq", requestParams.getString("schClasSeq",""));
        map.put("schSemeSeq", requestParams.getString("schSemeSeq",""));
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schPeriodCd", requestParams.getString("schPeriodCd",""));
        map.put("schStdtId", requestParams.getString("schStdtId",""));

        return scoreLogMapper.getScoreLogList(map);
    }
}