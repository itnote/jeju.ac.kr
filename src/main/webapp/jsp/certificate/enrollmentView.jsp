<%@ page import="com.infomind.axboot.utils.SessionUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>
<ax:set key="function1Label" value='<i class="cqc-download"></i> 확인서 발급'/>
<ax:set key="function2Label" value='<i class="cqc-download"></i> 재학증명서 발급'/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/certificate/enrollmentView.js?v=3'/>"></script>
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
                        <ax:td label='학생' width="300px">
                            <input type="text" class="form-control" data-ax-path="schStdt"  id="schStdt" placeholder="학번, 한글, 영문명"/>
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
                        <table style="float: right;width:500px">
                            <tr>
                                <th style="width:80px">확인서 종료일자</th>
                                <td style="width:120px">
                                    <div class="input-group" data-ax5picker="date" style="width: 120px" id="calendar2">
                                        <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="confEndDt" id="confEndDt"/>
                                        <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                                    </div>
                                </td>
                                <th style="width:50px">출력일자</th>
                                <td style="width:120px">
                                    <div class="input-group" data-ax5picker="date" style="width: 120px" id="calendar">
                                        <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="printDt" id="printDt"/>
                                        <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01"></div>
            </ax:split-panel>
        </ax:split-layout>
        <iframe name="downFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>