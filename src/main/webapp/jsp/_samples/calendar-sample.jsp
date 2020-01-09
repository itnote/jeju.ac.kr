<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="false"/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/_samples/calendar-sample.js' />"></script>
        <style type="text/css">
            .calendar-item-day {
                font-size: 30px !important;
            }
        </style>
    </jsp:attribute>
    <jsp:body>

        <div role="page-header">
            <div class="H10"></div>
            <div id="calendar-target" style="
                width:700px;
                border:1px solid #ccc;
                border-radius: 5px;
                padding: 5px;
                overflow: hidden;">

            </div>
        </div>


    </jsp:body>
</ax:layout>