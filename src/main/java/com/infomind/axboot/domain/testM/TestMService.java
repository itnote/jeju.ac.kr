package com.infomind.axboot.domain.testM;

import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import java.util.List;

@Service
public class TestMService extends BaseService<TestM, Integer> {
    private TestMRepository testMRepository;

    @Inject
    public TestMService(TestMRepository testMRepository) {
        super(testMRepository);
        this.testMRepository = testMRepository;
    }

    public List<TestM> gets(RequestParams<TestM> requestParams) {
        return findAll();
    }
}