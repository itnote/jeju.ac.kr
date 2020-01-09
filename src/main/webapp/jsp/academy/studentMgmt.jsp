<%@ page import="com.chequer.axboot.core.utils.RequestUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>
<%
    RequestUtils requestUtils = RequestUtils.of(request);
    requestUtils.setAttribute("CKEditorFuncNum", requestUtils.getInt("CKEditorFuncNum"));
    requestUtils.setAttribute("targetId", requestUtils.getString("targetId"));
%>
<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="function1Label" value='<i class="cqc-file-excel-o"></i> 엑셀업로드'/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/studentMgmt.js?v=30' />"></script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons>
        </ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='ax.admin.sample.search.condition' width="300px">
                            <ax:input type="text" name="schStdt" id="schStdt" clazz="form-control" placeholder="학번, 한글, 영문명"/>
                        </ax:td>
                        <ax:td label='학년도' width="300px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear">
                                <option value="">전체</option>

                            </select>
                            <div data-ax5select="schSemeYear" data-ax5select-config="{}" ></div>
                        </ax:td>
                        <ax:td label='과정명' width="400px">
                            <select data-ax-path="schSemeSeq" class="form-control W250"  id="schSemeSeq">
                                <option value="">전체</option>
                            </select>
                        </ax:td>
                        <ax:td label='전후반기구분' width="400px">
                            <select data-ax-path="schPeriodCd" class="form-control W250"  id="schPeriodCd">
                                <option value="">전체</option>
                            </select>

                        </ax:td>

                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="50%" style="padding-right: 10px;">
                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                            학생 목록 </h2>
                    </div>
                    <div class="right">

                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>
            </ax:split-panel>
            <ax:splitter></ax:splitter>
            <ax:split-panel width="*" style="padding-left: 10px;" scroll="scroll">
                <!-- 폼 -->
                <div class="ax-button-group" role="panel-header">
                    <div class="left">
                        <h2><i class="cqc-news"></i>
                            학생 정보
                        </h2>
                    </div>
                    <div class="right">
                        <button type="button" class="btn btn-default" data-form-view-01-btn="form-clear">
                            <i class="cqc-erase"></i>
                            <ax:lang id="ax.admin.clear"/>
                        </button>
                    </div>
                </div>
                <ax:form name="formView01">
                    <ax:tbl clazz="ax-form-tbl" minWidth="500px">
                        <ax:tr>
                            <ax:td width="300px" label="사진">
                                    <input type="hidden" data-ax-path="photoFileId" class="form-control"/>
                                    <div data-ax5uploader="upload_img" style="padding: 5px">
                                        <div class="uploadedBox" style="padding: 5px">
                                            <img class = "profile" onerror="profileDefault()" width="120px" height="120px" style="border-radius: 50%; object-fit: cover; object-position: top">
                                        </div>
                                        <button data-ax5uploader-button="selector" class="btn btn-primary">업로드</button>
                                        (최대 5MB)
                                    </div>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.name" width="300px" style="height:179px;">
                                <input type="hidden" data-ax-path="langKey" title="LANG_KEY"/>
                                <input type="text" data-ax-path="stdtNmKor" title="한글명" class="form-control" data-ax-validate="required" placeholder="한글명"/>
                                <br/>
                                <input type="text" data-ax-path="stdtNmEng" title="영문명" class="form-control" data-ax-validate="required" placeholder="영문명"/>
                                <br/>
                                <input type="text" data-ax-path="stdtNmChn" title="중국명" class="form-control" data-ax-validate="required" placeholder="중국명"/>
                            </ax:td>

                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.id" width="300px">
                                <input type="text" data-ax-path="stdtId" title="학번" class="form-control" data-ax-validate="required"/>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.natn" width="300px">
                                <ax:common-code groupCd="NATN_CD" dataPath="natnCd"/>
                            </ax:td>

                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.birth" width="300px">
                                <div class="input-group" data-ax5picker="date">
                                    <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="birthDt"/>
                                    <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                                </div>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.gender" width="300px">
                                <ax:common-code groupCd="GENDER" dataPath="gender"/>
                            </ax:td>
                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.hp" width="300px">
                                <input type="text" id= "hpNo" data-ax-path="hpNo" title="휴대폰번호" class="form-control"/>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.email" width="300px">
                                <input type="text" data-ax-path="email" title="이메일" class="form-control" data-ax-validate="required" />
                            </ax:td>
                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.msgcd1" width="300px">
                                <ax:common-code groupCd="MSG_CD" dataPath="msgCd1" emptyText="선택"/>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.msgid1" width="300px">
                                <input type="text" data-ax-path="msgId1" title="메신저1 ID" class="form-control" data-ax-validate="required" />
                            </ax:td>
                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.msgcd2" width="300px">
                                <ax:common-code groupCd="MSG_CD" dataPath="msgCd2" emptyText="선택"/>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.msgid2" width="300px">
                                <input type="text" data-ax-path="msgId2" title="메신저2 ID" class="form-control" data-ax-validate="required" />
                            </ax:td>
                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.addr" width="300px">
                                <input type="text" data-ax-path="addr" title="현주소" class="form-control" data-ax-validate="required" />
                            </ax:td>
                            <ax:td label="ax.admin.stdt.natn_addr" width="300px">
                                <input type="text" data-ax-path="natnAddr" title="본국주소" class="form-control" data-ax-validate="required" />
                            </ax:td>
                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.bank" width="300px">
                                <ax:common-code groupCd="BANK_CD" dataPath="bankCd" emptyText="선택"/>
                            </ax:td>
                            <ax:td label="ax.admin.stdt.account" width="300px">
                                <input type="text" data-ax-path="accountNo" title="계좌번호" class="form-control" data-ax-validate="required" />
                            </ax:td>
                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.studyPeriod" width="300px">
                                <input type="text" data-ax-path="studyPeriod" title="학습기간" class="form-control" data-ax-validate="required" />
                            </ax:td>
                            <ax:td label="ax.admin.stdt.topikCd" width="300px">
                                <ax:common-code groupCd="TOPIK_CD" dataPath="topikCd" emptyText="선택"/>
                            </ax:td>

                        </ax:tr>
                        <ax:tr>
                            <ax:td label="ax.admin.stdt.korLv" width="300px">
                                <input type="text" data-ax-path="korLv" title="한국어레벨" class="form-control" data-ax-validate="required" />
                            </ax:td>
                            <ax:td label="비자유형" width="300px">
                                <ax:common-code groupCd="VISA_TYPE" dataPath="visaType" emptyText="선택"/>
                            </ax:td>
                        </ax:tr>
                    </ax:tbl>

                </ax:form>
                <div class="H5"></div>
                <div class="ax-button-group">
                    <div class="left">
                        <h2>
                            <i class="cqc-news"></i>
                            학생 정보 이력
                        </h2>
                    </div>
                </div>
                <ax:tab-layout name="ax2" data_fit_height_content="grid-view-01">
                    <ax:tab-panel label="수강이력" scroll="scroll" active="true">
                        <div data-ax5grid="grid-view-02" style="height: 300px;"></div>
                    </ax:tab-panel>
                    <ax:tab-panel label="연락처" scroll="scroll">
                        <div data-ax5grid="grid-view-03" data-value="T" style="height: 300px;"></div>
                    </ax:tab-panel>
                    <ax:tab-panel label="주소" scroll="scroll">
                        <div data-ax5grid="grid-view-04" data-value="A" style="height: 300px;"></div>
                    </ax:tab-panel>
                </ax:tab-layout>
            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>

