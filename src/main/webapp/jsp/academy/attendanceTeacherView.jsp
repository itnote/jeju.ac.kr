<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>
<ax:set key="function1Label" value='<i class="cqc-download"></i> 출강부다운로드'/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/attendanceTeacherView.js?v=49' />"></script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>


        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='학년도' width="250px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear">
                            </select>
                        </ax:td>
                        <ax:td label='과정명' width="300px">
                            <select data-ax-path="schSemeSeq" class="form-control W180"  id="schSemeSeq">
                            </select>

                        </ax:td>
                        <ax:td label='전후반기구분' width="250px">
                            <select data-ax-path="schPeriodCd" class="form-control W100"  id="schPeriodCd">
                            </select>
                        </ax:td>
                    </ax:tr>
                    <ax:tr>
                        <ax:td label='시작일자' width="250px">
                            <div class="input-group calendar" data-ax5picker="date" style="width: 120px">
                                <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="schBeginAtdcDt" id="schBeginAtdcDt"/>
                                <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                            </div>
                        </ax:td>
                        <ax:td label='종료일자' width="300px">
                            <div class="input-group calendar" data-ax5picker="date" style="width: 120px">
                                <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="schEndAtdcDt" id="schEndAtdcDt"/>
                                <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                            </div>
                        </ax:td>
                        <ax:td label="강사명" width="300px">
                            <input type="text" class="form-control inline-block W100" data-ax-path="schTchrNm" id="schTchrNm"  readonly/>
                            <input type="hidden" class="form-control" data-ax-path="schTchrCd" id="schTchrCd" readonly/>
                            <button type="button" class="btn btn-primary tchr"><i class="cqc-magnifier"></i>검색</button></div>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="horizontal">
            <ax:split-panel width="*" style="">

                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                            프로그램 목록 </h2>
                    </div>

                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>

            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>