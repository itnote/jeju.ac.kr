package com.infomind.axboot.domain.subject;

import com.infomind.axboot.domain.lang.Lang;
import com.infomind.axboot.domain.lang.LangService;
import com.infomind.axboot.domain.semester.Semester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SubjectService extends BaseService<Subject, Subject.SubjectId> {
    private SubjectRepository subjectRepository;

    @Inject
    private LangService langService;

    @Inject
    public SubjectService(SubjectRepository subjectRepository) {
        super(subjectRepository);
        this.subjectRepository = subjectRepository;
    }


    public List<Subject> gets(RequestParams<Subject> requestParams) {
        String semeYear =requestParams.getString("semeYear","");
        long semeSeq = requestParams.getInt("semeSeq",0);
        String filter = requestParams.getString("filter");

        List<Subject> list = select()
                .from(qSubject)
                .where(qSubject.semeYear.eq(semeYear),qSubject.semeSeq.eq(semeSeq))
                .orderBy(qSubject.sbjtId.asc())
                .fetch();

        return list;
    }

    public List<Subject> getSeq(String semeYear,long semeSeq) {

        return select()
                .from(qSubject)
                .where(qSubject.semeYear.eq(semeYear),qSubject.semeSeq.eq(semeSeq))
                .orderBy(qSubject.sbjtId.desc())
                .limit(1)
                .fetch();

    }

    @Transactional
   public void subjectSave(List<Subject> list){
        for(Subject subject : list){
            Long sbjtNm = subject.getSbjtNm();

            if(sbjtNm == null){
                langService.save(subject.getLang());
                subject.setSbjtNm(subject.getLang().getLangKey());
            }
            save(subject);
        }
    }


}