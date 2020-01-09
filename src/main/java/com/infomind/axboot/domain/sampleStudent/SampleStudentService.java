package com.infomind.axboot.domain.sampleStudent;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SampleStudentService extends BaseService<SampleStudent, Integer> {
    private SampleStudentRepository sampleStudentRepository;

    @Inject
    public SampleStudentService(SampleStudentRepository sampleStudentRepository) {
        super(sampleStudentRepository);
        this.sampleStudentRepository = sampleStudentRepository;
    }

    public List<SampleStudent> gets(RequestParams<SampleStudent> requestParams) {
        String name = requestParams.getString("name", "");
        String studentId = requestParams.getString("studentId", "");
        String filter = requestParams.getString("filter");
        BooleanBuilder builder = new BooleanBuilder();

        if (isNotEmpty(name)) {
            builder.and(qSampleStudent.name.eq(name));
        }

        if (isNotEmpty(studentId)) {
            builder.and(qSampleStudent.studentId.eq(studentId));
        }


        List<SampleStudent> stdList = select().from(qSampleStudent).where(builder).orderBy(qSampleStudent.name.asc(), qSampleStudent.studentId.asc()).fetch();

        if (isNotEmpty(filter)) {
            stdList = filter(stdList, filter);
        }

        return stdList;
    }

    public List<Map<String, Object>> findAllStudentToReport() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (SampleStudent sampleStudent : sampleStudentRepository.findAll()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("ID", sampleStudent.getId());
            item.put("NAME", sampleStudent.getName());
            item.put("BIRTHDAY", sampleStudent.getBirthday());
            item.put("STUDENT_ID", sampleStudent.getStudentId());
            item.put("MAJOR", sampleStudent.getMajor());
            item.put("GRADUATION_DATE", sampleStudent.getGraduationDate());
            result.add(item);
        }
        return result;
    }



    /*
    TODO 증명서가 여러 종류일 때, 혹은 증명서마다 SampleStudent 값이 변할 때, 손쉽게 추가할 수 있는 개선 방안 찾기
     */

    public List<Map<String, Object>> findStudent(String studentId) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (SampleStudent sampleStudent : sampleStudentRepository.findByStudentId(studentId)) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("ID", sampleStudent.getId());
            item.put("NAME", sampleStudent.getName());
            item.put("BIRTHDAY", sampleStudent.getBirthday());
            item.put("STUDENT_ID", sampleStudent.getStudentId());
            item.put("MAJOR", sampleStudent.getMajor());
            item.put("GRADUATION_DATE", sampleStudent.getGraduationDate());
            result.add(item);
        }
        return result;
    }
}