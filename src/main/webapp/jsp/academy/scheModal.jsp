<%@ page import="com.chequer.axboot.core.utils.RequestUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>
<%
    RequestUtils req = RequestUtils.of(request);

    String scheDt =req.getString("scheDt");
    request.setAttribute("scheDt",scheDt);

    String semeSeq =req.getString("semeSeq");
    request.setAttribute("semeSeq",semeSeq);

    String semeYear =req.getString("semeYear");
    request.setAttribute("semeYear",semeYear);

    String scheNm =req.getString("scheNm");
    request.setAttribute("scheNm",scheNm);

    String holiYn =req.getString("holiYn");
    request.setAttribute("holiYn",holiYn);

    String counYn =req.getString("counYn");
    request.setAttribute("counYn",counYn);

    String weekCd =req.getString("weekCd");
    request.setAttribute("weekCd",weekCd);
%>
<ax:set key="pageName" value="File Browser"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="axbody_class" value="baseStyle"/>

<ax:layout name="modal">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/scheModal.js' />"></script>
    </jsp:attribute>
    <jsp:attribute name="header">
    </jsp:attribute>
    <jsp:body>

       <div role="page-header">
           <br/>
           <div class="left">
               <h2><i class="cqc-news"></i>
                   일정입력
               </h2>
           </div>
           <br/>
       </div>
                <ax:form name="formView01">
                <ax:tbl clazz="ax-form-tbl" minWidth="300px">
                    <ax:tr>
                        <ax:td label="날짜" width="250px" >
                            <input type="text" data-ax-path="scheDt" class="form-control"  title="날짜"  data-ax-validate="required" readonly="readonly"  value="${scheDt}"/>
                            <input type="hidden" data-ax-path="semeYear"  class="form-control"  value="${semeYear}"/>
                            <input type="hidden" data-ax-path="semeSeq"  class="form-control"  value="${semeSeq}"/>
                            <input type="hidden" data-ax-path="createdAt"  class="form-control"  value="${createdAt}"/>
                        </ax:td>
                    </ax:tr>
                    <ax:tr>
                        <ax:td label="일정명" width="250px" >
                            <input type="text" data-ax-path="scheNm" class="form-control"  title="일정명"  data-ax-validate="required"  value="${scheNm}"/>
                        </ax:td>
                    </ax:tr>
                    <ax:tr>
                        <ax:td label="휴일여부" width="250px">
                            <select data-ax-path="holiYn" class="form-control W100" id="holiYn">
                                <option value="Y" <c:if test="${holiYn eq 'Y'}">selected</c:if>>Y</option>
                                <option value="N" <c:if test="${holiYn eq 'N'}">selected</c:if>>N</option>
                            </select>
                        </ax:td>
                    </ax:tr>
                    <ax:tr>
                        <ax:td label="출결면담여부" width="250px">
                            <select data-ax-path="counYn" class="form-control W100" id="counYn">
                                <option value="Y" <c:if test="${counYn eq 'Y'}">selected</c:if>>Y</option>
                                <option value="N" <c:if test="${counYn eq 'N'}">selected</c:if>>N</option>
                            </select>
                        </ax:td>
                    </ax:tr>
                    <ax:tr>
                        <ax:td label="수업구분" width="250px">
                            <ax:common-code groupCd="WEEK_CD" dataPath="weekCd" id="weekCd" defaultValue="${weekCd}" emptyText="선택"/>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
                </ax:form>
        <br/>

        <div style="text-align: center;">
            <button type="button" class="btn btn-info" data-page-btn="save">저장</button>
            <button type="button" class="btn btn-fn1" data-page-btn="delete">삭제</button>
            <button type="button" class="btn btn-default" data-page-btn="close"><ax:lang id="ax.admin.sample.modal.button.close"/></button>
        </div>


    </jsp:body>
</ax:layout>