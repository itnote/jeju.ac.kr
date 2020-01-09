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
        <script type="text/javascript" src="<c:url value='/assets/js/view/certificate/documentBook.js' />"></script>
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
                        <ax:td label='연도' width="250px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear" name="schSemeYear">
                            </select>
                        </ax:td>
                        <ax:td label='문서번호' width="300px">
                            <input type="text" class="form-control" data-ax-path="schDocSeq"  id="schDocSeq" placeholder="문서번호"/>
                        </ax:td>
                        <ax:td label="발급일" width="250px">
                            <div class="input-group" data-ax5picker="date">
                                <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="schCreatedAt"  id="schCreatedAt"/>
                                <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                            </div>
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
                            문서 목록 </h2>
                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01"></div>
            </ax:split-panel>
        </ax:split-layout>
    </jsp:body>
</ax:layout>