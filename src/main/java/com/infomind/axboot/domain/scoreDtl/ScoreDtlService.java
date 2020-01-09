package com.infomind.axboot.domain.scoreDtl;

import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreDtlService extends BaseService<ScoreDtl, ScoreDtl.ScoreDtlId> {
    private ScoreDtlRepository scoreDtlRepository;

    @Inject
    public ScoreDtlMapper scoreDtlMapper;

    @Inject
    public ScoreDtlService(ScoreDtlRepository scoreDtlRepository) {
        super(scoreDtlRepository);
        this.scoreDtlRepository = scoreDtlRepository;
    }

    public List<ScoreDtl> gets(RequestParams<ScoreDtl> requestParams) {
        return findAll();
    }

    public List<ScoreDtl> getScoreDtlList(RequestParams<Map> requestParams) {

        HashMap map = new HashMap<String, Object>();
        map.put("schClasSeq", requestParams.getString("schClasSeq",""));
        map.put("schSemeSeq", requestParams.getString("schSemeSeq",""));
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schPeriodCd", requestParams.getString("schPeriodCd",""));
        map.put("schStdtId", requestParams.getString("schStdtId",""));
        map.put("schLv", requestParams.getString("schLv",""));

        return scoreDtlMapper.getScoreDtlList(map);
    }

}