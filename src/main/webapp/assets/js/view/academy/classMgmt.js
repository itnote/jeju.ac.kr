var fnObj = {}, CODE = {};
var itemSeq;
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/classmst"],
            data: this.searchView.getData(),
            async : false,
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.formView01.clear();
                caller.gridView02.clear();
                jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);

                //저장후 선택값 재조회
                if(itemSeq !=null){
                    var idx = 0;
                    var list = caller.gridView01.target.getList();
                    list = ax5.util.filter(list, function () {
                        return this.clasSeq === itemSeq;
                    });

                    if (list.length == 1) {
                        idx = list[0].__index;
                        caller.gridView01.target.select(idx, {selectedClear: true});
                        ACTIONS.dispatch(ACTIONS.ITEM_CLICK, list[0]);
                    }
                }
            }
        });
        return false;
    },
    PAGE_SEARCH_TAP: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/classdtl"],
            data: this.searchView1.getData(),
            async : false,
            callback: function (res) {
                caller.gridView02.setData(res);
            }
        });
        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.formView01.validate()) {
            var parentData = caller.formView01.getData();

            var childList = caller.gridView02.getData();

            childList = childList.concat(caller.gridView02.getData("modified"));
            childList = childList.concat(caller.gridView02.getData("deleted"));

            // childList에 parentKey 삽입
            childList.forEach(function (n) {
                n.semeSeq = parentData.semeSeq;
                n.semeYear = parentData.semeYear;
                n.clasSeq = parentData.clasSeq;
                n.periodCd = parentData.periodCd;
            });

            axboot.promise()
                .then(function (ok, fail, data) {
                    axboot.ajax({
                        type: "PUT", url: ["/api/v1/classmst"], data: JSON.stringify(parentData),
                        callback: function (res) {
                            ok(res);
                        }
                    });
                })
                .then(function (ok, fail, data) {
                    axboot.ajax({
                        type: "PUT", url: ["/api/v1/classdtl"], data: JSON.stringify(childList),
                        callback: function (res) {
                            ok(res);
                        }
                    });
                })
                .then(function (ok) {
                    axToast.push("저장 되었습니다");
                    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                })
                .catch(function () {

                });
        }
    },
    FORM_CLEAR: function (caller, act, data) {
        axDialog.confirm({
            msg: LANG("ax.script.form.clearconfirm")
        }, function () {
            if (this.key == "ok") {
                caller.formView01.clear();
                $('[data-ax-path="semeYear"]').change();
            }
        });
    },
    ITEM_CLICK: function (caller, act, data) {
        caller.gridView02.clear();
        $("#semeSeq option").not(".all").remove();

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester"],
            data:{
                semeYear : data.semeYear
            },
            async : false,
            callback: function (res) {
                for(var i=0;i<res.list.length;i++){
                    $("#semeSeq").append($('<option>',{
                        value:res.list[i].semeSeq,
                        text:res.list[i].kor
                    }));
                }
                caller.formView01.setData(data);
            }
        });

        $('#semeYear').attr("disabled", true);
        $('#semeSeq').attr("disabled", true);
        $('#periodCd').attr("disabled", true);

        jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);
    },
    ITEM_DEL: function (caller, act, data) {
        caller.gridView02.delRow("selected");
    },
    ITEM_ADD: function (caller, act, data) {
        caller.gridView02.addRow();
    },
    TCHR_MODAL: function (caller, act, data) {
        axboot.modal.open({
            modalType: "TCHR-MODAL",
            param: "",
            sendData: function(){
                return {
                    "sendData": "AX5UI"
                };
            },
            callback: function (res) {
                caller.formView01.setModalValue(res, data.setId, data.setNm);
                this.close();
            }
        });
    },
    UPLOAD_MODAL: function (caller, act, data) {
        var datas=caller.formView01.getData();

        axboot.modal.open({
            modalType: "CLAS-MODAL",
            param: {
                clasSeq:datas.clasSeq,
                semeYear:datas.semeYear,
                semeSeq:datas.semeSeq,
                periodCd:datas.periodCd
            },
            sendData: function () {
                return {
                    "sendData": "AX5UI"
                };
            },
            callback: function (data) {
               ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
               ACTIONS.dispatch(ACTIONS.PAGE_SEARCH_TAP);
               jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 1);
            }
        });
    },
    CLAS_STDT_EXCEL: function (caller, act, data) {
        var chkList = caller.gridView01.getData("selected");
        var sendList = [];

        if (chkList.length == 0) {
            axToast.push("수강반을 선택해주세요.");
            return;
        }

        chkList.forEach(function (n) {
            sendList.push({
                clasSeq: n.clasSeq,
                semeYear: n.semeYear,
                semeSeq: n.semeSeq,
                periodCd: n.periodCd,
                clasNm: n.clasNm,
                lctrRoom: n.lctrRoom
            });
        });

        $.form({
            action: '/api/v1/classdtl/clasStdtExcel',
            target: 'excelDownFrm',
            data: sendList
        }).submit();
    },
    CLAS_EXCEL: function (caller, act, data) {

        if(caller.searchView.checkData()){
            return;
        }

        $.form({
            action: '/api/v1/classmst/clasExcel',
            target: 'excelDownFrm',
            data: this.searchView.getData()
        }).submit();
    },
    //학생현황
    STDT_EXCEL: function (caller, act, data) {

        if(caller.searchView.checkData()){
            return;
        }

        $.form({
            action: '/api/v1/classdtl/getClassStudentExcel',
            target: 'excelDownFrm',
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schPeriodCd : $("#schPeriodCd").val(),
                schSemeNm : $("#schSemeSeq option:selected").text(),
                schPeriNm : $("#schPeriodCd option:selected").text()
            }
        }).submit();
    },
    DEL_CLASS: function (caller, act, data) {
        var formData =caller.formView01.getData();
        if(formData.clasSeq == null){
            alert("삭제할 수강반을 선택해주세요.");
            return;
        }

        if(formData.stdtCnt !=0){
            alert("수강반 인원이 존재하지 않는 수강반만 삭제할 수 있습니다.");
            return;
        }

        axboot.ajax({
            type: "DELETE",
            url: ["/api/v1/classmst"],
            data:  JSON.stringify(formData),
            callback: function (res) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            }
        });
    }

});

fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "STATUS_CD", useYn: "Y"},
                callback: function (res) {
                    var status = [];
                    status.push({
                        value: null, text: "선택"
                    });
                    res.list.forEach(function (n) {
                        status.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.status = status;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "USE_YN", useYn: "Y"},
                callback: function (res) {
                    var useYn = [];
                    useYn.push({
                        value: null, text: "선택"
                    });
                    res.list.forEach(function (n) {
                        useYn.push({
                            value: n.code, text: n.code
                        });
                    });
                    CODE.useYn = useYn;
                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "DISC_CD", useYn: "Y"},
                callback: function (res) {
                    var disc = [];
                    disc.push({
                        value: null, text: "선택"
                    });
                    res.list.forEach(function (n) {
                        disc.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.disc = disc;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            //  학년도 select
            axboot.ajax({
                type: "GET",
                url: ["/api/v1/semester/yearCombo"],
                callback: function (res) {
                    for(var i=0;i<res.list.length;i++){
                        $("#schSemeYear").append($('<option>',{
                            value:res.list[i].SEME_YEAR,
                            text:res.list[i].SEME_YEAR
                        }));
                    }
                    for(var i=0;i<res.list.length;i++){
                        $("#semeYear").append($('<option>',{
                            value:res.list[i].SEME_YEAR,
                            text:res.list[i].SEME_YEAR
                        }));
                    }
                    $('[data-ax-path="semeYear"]').change();
                    $('[data-ax-path="schSemeYear"]').change();
                }
            });

            // 과정명 select
            $('[data-ax-path="schSemeYear"]').change(function(){
                $("#schSemeSeq option").remove();
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/semester/semeSeqCombo"],
                    data: {
                        schSemeYear : $(this).val()
                    },
                    callback: function (res) {
                        for(var i=0;i<res.list.length;i++){
                            $("#schSemeSeq").append($('<option>',{
                                value:res.list[i].SEME_SEQ,
                                text:res.list[i].KOR
                            }));
                        }
                        $('[data-ax-path="schSemeSeq"]').change();
                    }
                });
            });

            $('[data-ax-path="semeYear"]').change(function(){
                $("#semeSeq option").remove();
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/semester/semeSeqCombo"],
                    data: {
                        schSemeYear : $(this).val()
                    },
                    callback: function (res) {
                        for(var i=0;i<res.list.length;i++){
                            $("#semeSeq").append($('<option>',{
                                value:res.list[i].SEME_SEQ,
                                text:res.list[i].KOR
                            }));
                        }

                    }
                });
            });

            // 전후반기구분 select
            $('[data-ax-path="schSemeSeq"]').change(function(){
                $("#schPeriodCd option").remove();
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/classmst/periodCombo"],
                    data: {
                        schSemeYear : $("#schSemeYear").val(),
                        schSemeSeq : $(this).val()
                    },
                    callback: function (res) {
                        for(var i=0;i<res.list.length;i++){
                            $("#schPeriodCd").append($('<option>',{
                                value:res.list[i].PERIOD_CD,
                                text:res.list[i].NM
                            }));
                        }
                        $('[data-ax-path="schPeriodCd"]').change();
                        ok();
                    }
                });
            });
        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.searchView1.initView();
            _this.gridView01.initView();
            _this.gridView02.initView();
            _this.formView01.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        })
        .catch(function () {

        });

    ///////모달띄우기
    $('[data-form-view-01-btn="findTchr"]').click(function(){
        ACTIONS.dispatch(ACTIONS.TCHR_MODAL, {setId: "tchrCd", setNm: "tchrNm"});
    });
    $('[data-form-view-01-btn="findSubTchr"]').click(function(){
        ACTIONS.dispatch(ACTIONS.TCHR_MODAL, {setId: "subTchrCd", setNm: "subTchrNm"});
    });


//////////////////탭화면이벤트
    jQuery('[data-ax5layout="ax2"]').ax5layout({
        onOpenTab: function () {
            if (this.activePanelIndex == 1) {
                var clasSeq=$("#clasSeq").val();

                if(clasSeq=="" ||clasSeq==null){
                    axToast.push("수강반을 선택해주세요.");
                    jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);
                    return;
                }
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH_TAP);
            }else{
                $("#schInput").val("");
            }
        }
    });
////////////////

    ///////////////학생조회 클릭이벤트
    $(document).on("click","[data-custom-btn]",function(){

                var index = this.getAttribute("data-custom-btn");

                axboot.modal.open({
                    modalType: "STDT-MODAL",
                    param: {
                        semeYear : $("#semeYear").val(),
                        semeSeq:$("#semeSeq").val(),
                        periodCd:$("#periodCd").val()
                    },
                    sendData: function(){
                        return {
                            "sendData": "AX5UI"
                        };
            },
            callback: function (data) {

                _this.gridView02.setValue(index,"stdtId",data.stdtId);
                _this.gridView02.setValue(index,"stdtNm",data.stdtNmKor);
                _this.gridView02.setValue(index,"stdtNmEng",data.stdtNmEng);
                _this.gridView02.setValue(index,"birthDt",data.birthDt);
                _this.gridView02.setValue(index,"natnNm",data.natnNm);

                this.close();
            }
        });
    })
};


fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "search": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            "save": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE);
            },
            "excel": function () {
                ACTIONS.dispatch(ACTIONS.CLAS_EXCEL);
            },
            "fn1": function () {
                ACTIONS.dispatch(ACTIONS.CLAS_EXCEL);
            },
            "fn2": function () {
                ACTIONS.dispatch(ACTIONS.STDT_EXCEL);
            }
        });
    }
});

/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView0"]);
        this.target.attr("onsubmit", "return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);");
        this.semeYear = $("#schSemeYear");
        this.semeSeq = $("#schSemeSeq");
        this.periodCd = $("#schPeriodCd");

    },
    getData: function () {
        return {
            semeYear: this.semeYear.val(),
            semeSeq: this.semeSeq.val(),
            periodCd:this.periodCd.val(),
            schSemeNm : $("#schSemeSeq option:selected").text(),
            schPeriNm : $("#schPeriodCd option:selected").text()
        }
    },
    checkData:function(){
        if(this.semeYear.val()==''||this.semeSeq.val()==''||this.periodCd.val()==''){
            alert("학년도, 과정명, 전후반기구분을 모두 선택해 주세요.");
            return true;
        }
        return false;
    }

});

/**
 * gridView01
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            multipleSelect: true,
            frozenColumnIndex: 0,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "semeNm", label: "학기", width: 110, align: "center"},
                {key: "corsNm", label: "과정명", width: 150, align: "left"},
                {key: "periodNm", label: "전후반기", width: 80, align: "center"},
                {key: "clasNm", label: "수강반명", width: 80, align: "center"},
                {key: "stdtCnt", label: "인원", width: 50, align: "right"},
                {key: "semeYear", label: "학기", hidden:"true"},
                {key: "semeSeq", label: "학기시퀀스", hidden:"true"},
                {key: "periodCd", label: "전후반기코드", hidden:"true"},
                {key: "clasSeq", label: "수강반시퀀스", hidden:"true"},
                {key: "semeCd", label: "학기코드", hidden:"true"},
                {key: "clasCd", label: "연수생구분", hidden:"true"},
                {key: "lv", label: "등급", hidden:"true"},
                {key: "tchrCd", label: "담임ID", hidden:"true"},
                {key: "tchrNm", label: "담임", hidden:"true"},
                {key: "subTchrCd", label: "부담임ID", hidden:"true"},
                {key: "subTchrNm", label: "부담임", hidden:"true"},
                {key: "lctrRoom", label: "강의실", hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                    itemSeq = this.item.clasSeq;
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-01-btn", {
            "excel":function(){
                ACTIONS.dispatch(ACTIONS.CLAS_STDT_EXCEL);
            },
        });
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == "modified" || _type == "deleted") {
            list = ax5.util.filter(_list, function () {
                return this.key;
            });
        } else {
            list = _list;
        }
        return list;
    }
});

/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
        $('#semeYear').attr("disabled", false);
        $('#semeSeq').attr("disabled", false);
        $('#periodCd').attr("disabled", false);
        return $.extend({}, axboot.formView.defaultData, {
        });
    },
    initView: function () {
        this.target = $("#formView01");
        this.target.find('[data-ax5picker="date"]').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model); // 모델 포메터 시작

        axboot.buttonClick(this, "data-form-view-01-btn", {
            "form-clear": function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            },
            "del-class": function () {
                axDialog.confirm({
                    msg: "정말 삭제하시겠습니까?"
                }, function () {
                    if (this.key == "ok") {
                        ACTIONS.dispatch(ACTIONS.DEL_CLASS);
                    }
                });

            }
        });
    },
    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.
        return $.extend({}, data);
    },
    setData: function (data) {

        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);

        //$('#semeYear').not(":selected").attr("disabled", "disabled");

        this.model.setModel(data);
        this.modelFormatter.formatting(); // 입력된 값을 포메팅 된 값으로 변경
    },
    validate: function () {
        var rs = this.model.validate();
        if (rs.error) {
            alert(LANG("ax.script.form.validate", rs.error[0].jquery.attr("title")));
            rs.error[0].jquery.focus();
            return false;
        }
        return true;
    },
    clear: function () {
        this.model.setModel(this.getDefaultData());
        //$('#semeYear').not(":selected").attr("disabled", "");

    },
    setModalValue : function(data,v1,v2){
        this.model.set(v1,data.userCd);
        this.model.set(v2,data.userNm);
    }
});


/**
 * searchView1
 */
fnObj.searchView1 = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView1"]);
        this.target.attr("onsubmit", "return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH_TAP);");
        this.clasSeq = $("#clasSeq");
        this.schInput = $("#schInput");
    },
    getData: function () {
        return {
            clasSeq: this.clasSeq.val(),
            schInput: this.schInput.val()
        }
    }
});


/**
 * gridView02
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        var _this = this;
        this.status = CODE.status;
        this.useYn = CODE.useYn;
        this.disc = CODE.disc;

        this.target = axboot.gridBuilder({
            showLineNumber: false,
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "stdtId", label: "(*)학번", width: 90, align: "center"},
                {key: "natnNm", label: "국적", width: 80, align: "center"},
                {key: "stdtNm", label: "이름", width: 120, align: "left"},
                {key: "stdtNmEng", label: "영문명", width: 180, align: "left"},
                {key: "birthDt", label: "생년월일", width: 80, align: "center"},
                {key: "btn", label: "학생조회", width: 70, align: "center",
                    formatter:function(){
                        var disp = "none";
                        if (this.item.__created__)
                            disp = "block";
                        var btn = "<button type='button' class='btn btn-primary' data-custom-btn='"+this.dindex+"' style='display: "+ disp +";'><i class='cqc-magnifier'></i>검색</button>";

                        return btn;
                    }
                },
                {key: "statusCd", label: "학적상태", width: 70, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.status
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return parent.COMMON_CODE["STATUS_CD"].map[this.value];
                    }
                },
                {key: "freshYn", label: "신입생여부", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.useYn
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return this.value;
                    }
                },
                {key: "dormPayYn", label: "기숙사비납부대상", width: 120, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.useYn
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return this.value;
                    }
                },
                {key: "appPayYn", label: "전형료납부대상", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.useYn
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return this.value;
                    }
                },
                {key: "insPayYn", label: "의료보험납부대상", width: 120, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.useYn
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return this.value;
                    }
                },
                {key: "bedPayYn", label: "침구납부대상", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.useYn
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return this.value;
                    }
                },
                {key: "prePayYn", label: "등록금선납부여부", width: 120, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.useYn
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return this.value;
                    }
                },
                {key: "feeDt", label: "등록금납부일자", width: 100, align: "center", editor:{
                        type:"date",config:{}
                    }
                },
                {key: "discCd", label: "등록금할인코드", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.disc
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return parent.COMMON_CODE["DISC_CD"].map[this.value];
                    }
                },
                {key: "feeAmt", label: "등록금납부금액", width: 100, align: "right", editor: "number",formatter:"money"},
                {key: "dormAmt", label: "기숙사비납부금액", width: 120, align: "right", editor: "number",formatter:"money"},
                {key: "generalReview", label: "총평", width: 200, align: "left", editor: "text"},
                {key: "clasSeq", label: "수강반시퀀스", width: 70, align: "center", editor: "text",hidden:"true"},
                {key: "semeYear", label: "학년도", width: 70, align: "center",hidden:"true"},
                {key: "semeSeq", label: "학기시퀀스", width: 70, align: "center",hidden:"true"},
                {key: "periodCd", label: "전후반구분", width: 70, align: "center",hidden:"true"}
            ],
            body: {
                columnHeight: 32,
                onClick: function () {
                    // this.self.select(this.dindex);
                    // ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);
                    this.self.select(this.dindex, {selectedClear: true});
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-02-btn", {
            "item-add": function () {
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            },
            "item-remove": function () {

                axDialog.confirm({
                    msg: "정말 삭제하시겠습니까?"
                }, function () {
                    if (this.key == "ok") {
                        ACTIONS.dispatch(ACTIONS.ITEM_DEL);
                    }
                });
            },
            "excel":function(){
                ACTIONS.dispatch(ACTIONS.UPLOAD_MODAL);
            },
            "carry":function(){

            }
        });
    },
    setData: function (_data) {
        this.target.setData(_data);
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == "modified" || _type == "deleted") {
            list = ax5.util.filter(_list, function () {
                return this.semeYear&&this.semeSeq&&this.clasSeq&&this.periodCd;
            });
        } else {
            list = _list;
        }
        return list;
    },
    align: function () {
        this.target.align();
    },
    addRow: function () {
        this.target.addRow({__created__: true}, "last");

    },
    setValue:function(index,target,_data){
        this.target.setValue(index,target,_data);
    }

});
