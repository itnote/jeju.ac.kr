<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${PAGE_REMARK}"/>
<ax:set key="page_auto_height" value="true"/>

<ax:layout name="base">
    <jsp:attribute name="script">

        <script type="text/javascript" src="<c:url value='/assets/js/view/_samples/grid-value.js?ver=1' />"></script>
    </jsp:attribute>
    <jsp:body>
        <ax:page-buttons></ax:page-buttons>


        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label="ax.admin.search" width="300px">
                            <input type="text" name="filter" id="filter" class="form-control" value="" placeholder="<ax:lang id="ax.admin.input.search"/>"/>
                        </ax:td>
                        <ax:td label='ax.admin.sample.form.period' width="300px" labelWidth="80px">

                            <div class="input-group" data-ax5picker="date">
                                <input type="text" class="form-control" placeholder="yyyy/mm/dd">
                                <span class="input-group-addon">~</span>
                                <input type="text" class="form-control" placeholder="yyyy/mm/dd">
                                <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                            </div>

                        </ax:td>
                        <ax:td label='ax.admin.sample.form.show.code.list' width="450px" labelWidth="120px">

                            <div class="form-inline">
                                <div class="form-group">
                                    <input type="text" data-ax-path="etc3" class="form-control inline-block W60" readonly="readonly" />
                                    <input type="text" data-ax-path="etc4" class="form-control inline-block W150" />
                                    <button type="button" class="btn btn-primary" data-searchview-btn="modal">
                                        <i class="cqc-magnifier"></i>
                                        <ax:lang id="ax.admin.search"/>
                                    </button>
                                </div>
                            </div>

                        </ax:td>
                        <ax:td label='ax.admin.sample.form.date' width="250px">

                            <div class="input-group" data-ax5picker="date">
                                <input type="text" class="form-control" placeholder="yyyy/mm/dd"/>
                                <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                            </div>

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
                    <div class="right">
                        <button type="button" class="btn btn-default" data-grid-view-01-btn="add"><i class="cqc-circle-with-plus"></i> 추가</button>
                        <button type="button" class="btn btn-default" data-grid-view-01-btn="delete"><i class="cqc-circle-with-plus"></i> 삭제</button>
                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>

            </ax:split-panel>
        </ax:split-layout>

    </jsp:body>
</ax:layout>