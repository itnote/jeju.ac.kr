package com.infomind.axboot.domain.docLog;

import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import java.util.List;

@Service
public class DocLogService extends BaseService<DocLog, Long> {
    private DocLogRepository docLogRepository;

    @Inject
    public DocLogService(DocLogRepository docLogRepository) {
        super(docLogRepository);
        this.docLogRepository = docLogRepository;
    }

    public List<DocLog> gets(RequestParams<DocLog> requestParams) {
        return findAll();
    }
}