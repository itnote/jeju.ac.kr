package com.infomind.axboot.domain.stdtlog;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;

import java.util.List;

@Service
public class StdtLogService extends BaseService<StdtLog, Long> {
    private StdtLogRepository stdtLogRepository;

    @Inject
    public StdtLogService(StdtLogRepository stdtLogRepository) {
        super(stdtLogRepository);
        this.stdtLogRepository = stdtLogRepository;
    }

    public List<StdtLog> gets(RequestParams<StdtLog> requestParams) {
        String stdtId = requestParams.getString("stdtId", "");
        String logCd = requestParams.getString("stdtLogCd", "");
        BooleanBuilder builder = new BooleanBuilder();

        if (isNotEmpty(stdtId)) {
            builder.and(qStdtLog.stdtId.eq(stdtId));
        }

        if (isNotEmpty(logCd)) {
            builder.and(qStdtLog.stdtLogCd.eq(logCd));
        }

        List<StdtLog> list = select().from(qStdtLog).where(builder).orderBy(qStdtLog.stdtId.asc(), qStdtLog.stdtLogCd.asc()).fetch();
        return list;
    }

    public List<StdtLog> findByStdtId(String stdtId) {
        return stdtLogRepository.findByStdtId(stdtId);
    }



//    @Transactional
//    public void saveLogs(List<StdtLog> stdtLogs) {
//        Student jnuStudent = new Student();
//        for (StdtLog stdtLog : stdtLogs) {
//            jnuStudent = jnuStudentService.findOne(jnuStudent.getStdtId());
//            stdtLog.setStdtId(jnuStudent.getStdtId());
//        }
//    }

}