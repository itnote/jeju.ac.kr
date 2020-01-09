<%@ page import="com.chequer.axboot.core.utils.RequestUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>
<%
    RequestUtils req = RequestUtils.of(request);

    String semeSeq =req.getString("semeSeq");
    request.setAttribute("semeSeq",semeSeq);

    String semeYear =req.getString("semeYear");
    request.setAttribute("semeYear",semeYear);

    String periodCd =req.getString("periodCd");
    request.setAttribute("periodCd",periodCd);


%>
<ax:set key="pageName" value="File Browser"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="axbody_class" value="baseStyle"/>

<ax:layout name="modal">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/stdtModal.js?v=13' />"></script>
    </jsp:attribute>
    <jsp:attribute name="header">
        <h1 class="title">
            <i class="cqc-browser"></i>
            학생목록
        </h1>
    </jsp:attribute>
    <jsp:body>
        <ax:page-buttons>
            <button type="button" class="btn btn-default" data-page-btn="close"><ax:lang id="ax.admin.sample.modal.button.close"/></button>
            <button type="button" class="btn btn-info" data-page-btn="search"><ax:lang id="ax.admin.sample.modal.button.search"/></button>
            <button type="button" class="btn btn-fn1" data-page-btn="choice"><ax:lang id="ax.admin.sample.modal.button.choice"/></button>
        </ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='ax.admin.sample.search.condition' width="300px">
                            <input type="text" class="form-control" data-ax-path="schStdt" id="schStdt"/>
                            <input type="hidden" class="form-control" data-ax-path="useYn" value="Y"/>
                            <input type="hidden" class="form-control" data-ax-path="schSemeSeq" id="schSemeSeq" value="${semeSeq}"/>
                            <input type="hidden" class="form-control" data-ax-path="schSemeYear" id="schSemeYear" value="${semeYear}"/>
                            <input type="hidden" class="form-control" data-ax-path="schPeriodCd" id="schPeriodCd" value="${periodCd}"/>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="*" style="padding-right: 0px;">


                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>

            </ax:split-panel>
        </ax:split-layout>

    </jsp:body>
</ax:layout>