<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="function1Label" value='<i class="cqc-file-excel-o"></i> 반편성표'/>
<ax:set key="function2Label" value='<i class="cqc-file-excel-o"></i> 학생현황'/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/classMgmt.js?ver=4' />"></script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='학년도' width="300px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear">
                            </select>
                        </ax:td>
                        <ax:td label='과정명' width="300px">
                            <select data-ax-path="schSemeSeq" class="form-control W150"  id="schSemeSeq">
                            </select>
                        </ax:td>
                        <ax:td label='전후반기구분' width="300px">
                            <select data-ax-path="schPeriodCd" class="form-control W150"  id="schPeriodCd">
                            </select>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="575" style="padding-right: 10px;">
                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                            수강반 목록 </h2>
                    </div>
                    <div class="right">
                        <button type="button" class="btn btn-default" data-grid-view-01-btn="excel">
                            <i class="cqc-file-excel-o"></i>
                            수강생현황 EXCEL
                        </button>
                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>
            </ax:split-panel>
            <ax:splitter></ax:splitter>
            <ax:split-panel width="*" style="padding-left: 10px;" scroll="scroll">
                <ax:tab-layout name="ax2" data_fit_height_content="layout-view-01" style="height:100%;">
                    <ax:tab-panel label="수강반" scroll="scroll">
                        <!-- 폼 -->
                        <div class="ax-button-group" role="panel-header">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    수강반 정보
                                </h2>
                            </div>
                            <div class="right">
                                <button type="button" class="btn btn-default" data-form-view-01-btn="del-class">
                                    <i class="cqc-minus"></i>삭제
                                </button>
                                <button type="button" class="btn btn-default" data-form-view-01-btn="form-clear">
                                    <i class="cqc-erase"></i>
                                    <ax:lang id="ax.admin.clear"/>
                                </button>
                            </div>
                        </div>
                        <ax:form name="formView01">
                            <ax:tbl clazz="ax-form-tbl" minWidth="500px">
                                <ax:tr>
                                    <ax:td label="(*)학년도" width="300px">
                                        <select data-ax-path="semeYear" class="form-control W100"  data-ax-validate="required" title="학년도" id="semeYear">
<%--                                            <option value="" selected disabled hidden>선택</option>--%>
                                        </select>
                                        <input type="hidden" data-ax-path="clasSeq" class="form-control" id="clasSeq"/>
                                    </ax:td>
                                    <ax:td label="(*)과정명" width="350px">
                                        <select data-ax-path="semeSeq" class="form-control W150"  id="semeSeq"  data-ax-validate="required" title="과정명" >
<%--                                            <option value="" class="all">선택</option>--%>
                                        </select>
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)전후반기구분" width="300px" >
                                        <ax:common-code groupCd="PERIOD_CD" dataPath="periodCd" id="periodCd" />
                                    </ax:td>
                                    <ax:td label="등급" width="300px">
                                        <ax:common-code groupCd="LV" dataPath="lv" emptyText="선택"/>
                                    </ax:td>

                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="연수생구분" width="300px">
                                        <ax:common-code groupCd="CLAS_CD" dataPath="clasCd" emptyText="선택"/>
                                    </ax:td>
                                    <ax:td label="수강반명" width="300px">
                                        <input type="text" data-ax-path="clasNm" class="form-control"  />
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="담임" width="500px" style="display: inline;">
                                        <input type="text" data-ax-path="tchrCd" class="form-control inline-block W100"  readonly="readonly"/>
                                        <input type="text" data-ax-path="tchrNm" class="form-control inline-block W100"   readonly="readonly"/>
                                        <button class="btn btn-default" data-form-view-01-btn="findTchr">강사목록조회</button>
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="부담임" width="500px">
                                        <input type="text" data-ax-path="subTchrCd" class="form-control inline-block W100"  readonly="readonly"/>
                                        <input type="text" data-ax-path="subTchrNm" class="form-control inline-block W100"   readonly="readonly"/>
                                        <button class="btn btn-default" data-form-view-01-btn="findSubTchr">강사목록조회</button>
                                    </ax:td>

                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="강의실" width="250px">
                                        <input type="text" data-ax-path="lctrRoom" class="form-control" title="강의실"  />
                                    </ax:td>
                                </ax:tr>
                            </ax:tbl>
                        </ax:form>
                    </ax:tab-panel>
                    <ax:tab-panel label="수강생" scroll="scroll" >
                        <div class="ax-button-group" role="panel-header">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    수강생 정보
                                </h2>
                            </div>
                            <div class="right">
                                <button type="button" class="btn btn-default" data-grid-view-02-btn="item-add">
                                    <i class="cqc-plus"></i>
                                    <ax:lang id="ax.admin.add"/>
                                </button>
                                <button type="button" class="btn btn-default" data-grid-view-02-btn="item-remove">
                                    <i class="cqc-minus"></i>
                                    <ax:lang id="ax.admin.delete"/>
                                </button>
                                <button type="button" class="btn btn-default" data-grid-view-02-btn="excel">
                                   엑셀 업로드
                                </button>
                                <%--<button type="button" class="btn btn-default" data-grid-view-02-btn="carry">
                                    입학금 이월처리
                                </button>--%>
                            </div>

                        </div>
                        <div role="page-header">
                            <ax:form name="searchView1">
                                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                                    <ax:tr>
                                        <ax:td label='검색' width="300px">
                                            <input type="text" class="form-control" data-ax-path="schInput" id="schInput"/>

                                        </ax:td>

                                    </ax:tr>
                                </ax:tbl>
                            </ax:form>
                            <div class="H10"></div>
                        </div>

                        <div data-ax5grid="grid-view-02" style="height: 535px;"></div>

                    </ax:tab-panel>
                </ax:tab-layout>

            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>