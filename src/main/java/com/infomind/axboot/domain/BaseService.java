package com.infomind.axboot.domain;

import com.infomind.axboot.domain.code.QCommonCode;
import com.infomind.axboot.domain.file.QCommonFile;
import com.infomind.axboot.domain.lang.QLang;
import com.infomind.axboot.domain.program.QProgram;
import com.infomind.axboot.domain.program.menu.QMenu;
import com.infomind.axboot.domain.sampleStudent.QSampleStudent;
import com.infomind.axboot.domain.schedule.QSchedule;
import com.infomind.axboot.domain.semester.QSemester;
import com.infomind.axboot.domain.stdtlog.QStdtLog;
import com.infomind.axboot.domain.student.QStudent;
import com.infomind.axboot.domain.subject.QSubject;
import com.infomind.axboot.domain.user.QUser;
import com.infomind.axboot.domain.user.auth.QUserAuth;
import com.infomind.axboot.domain.user.auth.menu.QAuthGroupMenu;
import com.infomind.axboot.domain.user.role.QUserRole;
import com.chequer.axboot.core.domain.base.AXBootBaseService;
import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;

import java.io.Serializable;


public class BaseService<T, ID extends Serializable> extends AXBootBaseService<T, ID> {

    protected QUserRole qUserRole = QUserRole.userRole;
    protected QAuthGroupMenu qAuthGroupMenu = QAuthGroupMenu.authGroupMenu;
    protected QCommonCode qCommonCode = QCommonCode.commonCode;
    protected QUser qUser = QUser.user;
    protected QProgram qProgram = QProgram.program;
    protected QUserAuth qUserAuth = QUserAuth.userAuth;
    protected QMenu qMenu = QMenu.menu;
    protected QCommonFile qCommonFile = QCommonFile.commonFile;
    protected QStudent qStudent = QStudent.student;
    protected QSampleStudent qSampleStudent = QSampleStudent.sampleStudent;
    protected QLang qLang = QLang.lang;
    protected QSemester qSemester = QSemester.semester;
    protected QSubject qSubject = QSubject.subject;
    protected QStdtLog qStdtLog = QStdtLog.stdtLog;
    protected QSchedule qSchedule = QSchedule.schedule;

    protected AXBootJPAQueryDSLRepository<T, ID> repository;

    public BaseService() {
        super();
    }

    public BaseService(AXBootJPAQueryDSLRepository<T, ID> repository) {
        super(repository);
        this.repository = repository;
    }
}
