<%@ page import="com.infomind.axboot.utils.SessionUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>


<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/scoreMgmt.js?v=16' />"></script>
        <script type="text/javascript">
            var userMenuGrpCd = "${menuGrpCd}";
        </script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='학년도' width="250px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear" name="schSemeYear">
                            </select>
                        </ax:td>
                        <ax:td label='과정명' width="400px">
                            <select data-ax-path="schSemeSeq" class="form-control W250"  id="schSemeSeq" name="schSemeSeq">
                            </select>

                        </ax:td>
                        <ax:td label='전후반기구분' width="250px">
                            <select data-ax-path="schPeriodCd" class="form-control W100"  id="schPeriodCd" name="schPeriodCd">
                            </select>
                        </ax:td>
                        <ax:td label='수강반명' width="300px">
                            <select data-ax-path="schClasSeq" class="form-control W150"  id="schClasSeq" name="schClasSeq">
                            </select>
                        </ax:td>
                        <ax:td label='내수강반' width="300px">
                            <input type="checkbox" class="checkbox-inline" id="schMyClasChk" <c:if test='${menuGrpCd eq "USER"}'>checked</c:if>>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="632" style="padding-right: 10px;">
                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                          학생 목록 </h2>
                    </div>
                    <div class="right">
                        <button type="button" class="btn btn-default" data-grid-view-01-btn="score_calc">
                            <i class="cqc-calculator2"></i>
                            최종성적집계
                        </button>
                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>
            </ax:split-panel>
            <ax:splitter></ax:splitter>
            <ax:split-panel width="*" style="padding-left: 10px;" scroll="scroll">
                <ax:split-layout name="ax2" orientation="horizontal">
                    <ax:split-panel height="360" style="padding-bottom: 5px;">
                        <!-- 목록 -->
                        <div class="ax-button-group" data-fit-height-aside="grid-view-02">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    과목별 성적 </h2>
                            </div>
                        </div>
                        <div data-ax5grid="grid-view-02" data-fit-height-content="grid-view-02" style="height:300px;"></div>
                    </ax:split-panel>
                    <ax:splitter></ax:splitter>
                    <ax:split-panel height="*" style="padding-top: 5px;">
                        <div class="ax-button-group" data-fit-height-aside="grid-view-03">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    성적 수정 이력 </h2>
                            </div>
                        </div>
                        <div data-ax5grid="grid-view-03" data-fit-height-content="grid-view-03" style="height:300px;"></div>
                    </ax:split-panel>
                </ax:split-layout>

            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>