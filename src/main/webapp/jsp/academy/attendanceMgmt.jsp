<%@ page import="com.infomind.axboot.utils.SessionUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>
<ax:set key="userCd" value="${SessionUtils.getCurrentUser().getUserCd()}"/>
<ax:set key="userNm" value="${SessionUtils.getCurrentUser().getUserNm()}"/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/attendanceMgmt.js?v=49' />"></script>
        <style type="text/css">
            .calendar-item-day {
                font-size: 30px !important;
            }
        </style>
    </jsp:attribute>
    <jsp:body>
        <%--<input type="text" value="${menuGrpCd}" id="menuGrpCd"/>--%>

        <input type="hidden" id="userCd" value="<c:if test='${menuGrpCd eq "USER"}'>${userCd}</c:if>">
        <input type="hidden" id="userNm" value="<c:if test='${menuGrpCd eq "USER"}'>${userNm}</c:if>">
        <input type="hidden" id="userTchrDiv" value="M">

        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='학년도' width="250px">
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
                        </ax:td>
                        <ax:td label='내수강반' width="300px">
                            <input type="checkbox" class="checkbox-inline" id="schMyClasChk" <c:if test='${menuGrpCd eq "USER"}'>checked</c:if>>
                            <input type="hidden"  class="form-control" data-ax-path="schAtdcDt" id="schAtdcDt">
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="650" style="padding-right: 10px;" scroll="scroll">
                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                            일정 </h2>
                    </div>
                </div>
                <div id="calendar-target" style="width:640px;border:1px solid #ccc;border-radius: 5px;padding: 5px;overflow: hidden;">
                </div>
            </ax:split-panel>
            <ax:splitter></ax:splitter>
            <ax:split-panel width="*" style="padding-left: 10px;" scroll="scroll">
                <ax:split-layout name="ax2" orientation="horizontal">
                    <ax:split-panel height="208"  style="padding-bottom: 5px;">
                        <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                            <div class="left">
                                <h2><i class="cqc-list"></i>
                                    수업현황 </h2>
                            </div>
                            <div class="right">
                                <button type="button" class="btn btn-default" data-grid-view-01-btn="add">
                                    <i class="cqc-plus"></i>
                                    추가
                                </button>
                                <button type="button" class="btn btn-default" data-grid-view-01-btn="delete">
                                    <i class="cqc-minus"></i>
                                    <ax:lang id="ax.admin.delete"/>
                                </button>
                            </div>
                        </div>
                        <div data-ax5grid="grid-view-01" style="height:158px;"></div>
                    </ax:split-panel>
                    <ax:splitter></ax:splitter>
                    <ax:split-panel height="*" style="padding-top: 5px;">
                        <div class="ax-button-group" data-fit-height-aside="grid-view-02">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    출석현황
                                </h2>
                            </div>
                            <div class="right">
                                <table style="float: right;width:480px">
                                    <tr>
                                        <td style="width: 180px;">
                                            <select  class="form-control"  id="atdcSeq" style="width:60px;float:left">
                                                    <option class="all">선택</option>
                                            </select>
                                            <%--<input type="number" id="atdcSeq" style="width: 40px;" value="1">--%>교시와 동일
                                            <button type="button" class="btn btn-default" data-grid-view-02-btn="apply" id="apply">
                                                적용
                                            </button>
                                        </td>
                                        <td style="width:70px">출결구분</td>
                                        <td style="width:80px">
                                           <ax:common-code groupCd="ATDC_CD" dataPath="atdcCd" id="atdcCd"   emptyText="선택"/>
                                        </td>
                                        <td style="width:70px">결석사유</td>
                                        <td style="width:90px">
                                            <ax:common-code groupCd="ATDC_REASON" dataPath="atdcReason" id="atdcReason"  emptyText="선택" />
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div data-ax5grid="grid-view-02" data-fit-height-content="grid-view-02"></div>
                    </ax:split-panel>
                </ax:split-layout>

            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>