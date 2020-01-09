<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="pageName" value="File Browser"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="axbody_class" value="baseStyle"/>

<ax:layout name="modal">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/changePwModal.js?v=2'/>"></script>
    </jsp:attribute>
    <jsp:attribute name="header">
    </jsp:attribute>
    <jsp:body>

       <div role="page-header">
           <br/>
           <div class="left">
               <h2><i class="cqc-news"></i>
                   비밀번호 변경
               </h2>
           </div>
           <br/>
       </div>
                <ax:form name="formView01">
                <ax:tbl clazz="ax-form-tbl" minWidth="310px">
                    <ax:tr>
                        <ax:td label="비밀번호" width="260px" >
                            <input type="password" data-ax-path="newPw" class="form-control"  title="비밀번호"  data-ax-validate="required"  />
                        </ax:td>
                    </ax:tr>
                    <ax:tr>
                        <ax:td label="비밀번호 확인" width="260px" >
                            <input type="password" data-ax-path="userPs" class="form-control"  title="비밀번호 확인"  data-ax-validate="required"  />

                        </ax:td>
                    </ax:tr>
                </ax:tbl>
                    <p>영문과 숫자를 조합한 8자리 이상 비밀번호를 입력해주세요.</p>
                </ax:form>


        <div style="text-align: center;">
            <button type="button" class="btn btn-info" data-page-btn="save">변경</button>
        </div>


    </jsp:body>
</ax:layout>