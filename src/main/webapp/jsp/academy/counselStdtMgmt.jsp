<%@ page import="com.infomind.axboot.utils.SessionUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${PAGE_REMARK}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/counselStdtMgmt.js' />"></script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>


        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <%--<ax:td label='학년도' width="250px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear">
                            </select>
                        </ax:td>
                        <ax:td label='과정명' width="400px">
                            <select data-ax-path="schSemeSeq" class="form-control W250"  id="schSemeSeq">
                            </select>
                        </ax:td>
                        <ax:td label='전후반기구분' width="250px">
                            <select data-ax-path="schPeriodCd" class="form-control W100"  id="schPeriodCd">
                            </select>
                        </ax:td>
                        <ax:td label='수강반명' width="300px">
                            <select data-ax-path="schClasSeq" class="form-control W150"  id="schClasSeq">
                            </select>
                        </ax:td>--%>
                        <ax:td label='학생' width="300px">
                            <input type="text" class="form-control" data-ax-path="schStdt"  id="schStdt" placeholder="학번, 한글, 영문명"/>
                            <input type="hidden" class="form-control" data-ax-path="openYn"  id="openYn" value="<c:if test='${menuGrpCd eq "USER"}'>Y</c:if>" />
                        </ax:td>
                        <ax:td label='강사' width="300px">
                            <input type="text" class="form-control" data-ax-path="schTchr"  id="schTchr" placeholder="강사ID, 강사명"/>
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
                            면담현황 </h2>
                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>

            </ax:split-panel>

        </ax:split-layout>



    </jsp:body>
</ax:layout>