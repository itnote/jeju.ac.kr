var fnObj = {}, CODE = {} ,cnt=0, getDate = '';
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {

        axboot.promise()
            .then(function (ok, fail, data) {
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/attendancemst/getTchrDiv"],
                    data: caller.searchView.getData(),
                    callback: function (tchrDivRes) {
                        if (tchrDivRes.map) {
                            if ($("#userCd").val() == tchrDivRes.map.SUB_TCHR_CD) {
                                $("#userTchrDiv").val("S");
                            } else {
                                $("#userTchrDiv").val("M");
                            }
                        }
                        ok();
                    }
                });
            })
            .then(function (ok, fail, data) {
                ACTIONS.dispatch(ACTIONS.CALENDAR_VIEW);
            })
            .catch(function () {
            });
        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        var selectHour = $(".selected-day").children(".addon-header").html();
        var tchrHour = selectHour.substr(0,1);
        var subTchrHour = selectHour.substr(2,1);
        var tchrAddCnt = 0;
        var subTchrAddCnt = 0;

        var parentData = caller.gridView01.getData();
        for (var checkCnt = 0; checkCnt < parentData.length; checkCnt++) {
            if (parentData[checkCnt].tchrDiv === "M") {
                tchrAddCnt = tchrAddCnt + 1;
            } else {
                subTchrAddCnt = subTchrAddCnt + 1;
            }
        }

        if(tchrAddCnt > tchrHour || subTchrAddCnt > subTchrHour){
            axToast.push("수업 시수를 초과하여 입력하셨습니다. 확인 후 다시 처리하세요.");
            return;
        }

        if(caller.gridView02.getData().length == 0) {
            axToast.push("출석조회 후 다시 처리하세요.");
            return;
        }

        parentData = parentData.concat(caller.gridView01.getData("modified"));
        //parentData = parentData.concat(caller.gridView01.getData("deleted"));

        var childList = caller.gridView02.getData();

        childList = childList.concat(caller.gridView02.getData("modified"));
        //childList = childList.concat(caller.gridView02.getData("deleted"));

        axboot.promise()
            .then(function (ok, fail, data) {
                axboot.ajax({
                    type: "PUT", url: ["/api/v1/attendancemst"], data: JSON.stringify(parentData),
                    callback: function (res) {
                        ok(res);

                    }
                });
            })
            .then(function (ok, fail, data) {
                axboot.ajax({
                    type: "PUT", url: ["/api/v1/attendancedtl"], data: JSON.stringify(childList),
                    callback: function (res) {
                        ok(res);

                    }
                });
            })
            .then(function (ok) {
                axToast.push("저장 되었습니다");
                caller.gridView01.clear();
                $(".selected-day").trigger("click");
            })
            .catch(function () {

            });
    },
    CALENDAR_VIEW: function (caller, act, data) {
        ACTIONS.dispatch(ACTIONS.CLEAR_FORM);

        var semeSeq =  $("#schSemeSeq").val();
        var semeYear =  $("#schSemeYear").val();
        var sDt, eDt;
        var monCd, tueCd, wedCd, thuCd, friCd;
        var monHours, tueHours, wedHours, thuHours, friHours;

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester"],
            data: {
                semeYear : semeYear,
                semeSeq : semeSeq
            },
            callback: function (res) {
                if (res.list.length == 0) {
                    return;
                }
                sDt = res.list[0].startDt;
                eDt = res.list[0].endDt;
                monCd = res.list[0].monCd;
                tueCd = res.list[0].tueCd;
                wedCd = res.list[0].wedCd;
                thuCd = res.list[0].thuCd;
                friCd = res.list[0].friCd;
                monHours = res.list[0].monHours;
                tueHours = res.list[0].tueHours;
                wedHours = res.list[0].wedHours;
                thuHours = res.list[0].thuHours;
                friHours = res.list[0].friHours;

                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/schedule"],
                    data: {semeYear:semeYear, semeSeq : semeSeq},
                    callback: function (res) {
                        caller.gridView01.clear();

                        var calData ={};
                        var theme = "";
                        var label ="";
                        var header ="";
                        // '2019-02-01': {theme: 'a2', label: '개강'},
                        // '2019-02-07': {theme: 'm4', label: '입학식'},
                        // '2019-02-18': {theme: 'm2', label: '중간고사'},
                        // '2019-02-04': {theme: 'holiday', label: '설날'},
                        //////////일정가져오기
                        var oneDay = 24*3600*1000;
                        var day;

                        for (var ms= new Date(sDt)*1,last= new Date(eDt)*1;ms<=last;ms+=oneDay) {
                            label ="";

                            day = ax5.util.date(new Date(ms), {return:'yyyy-MM-dd'});

                            switch (new Date(ms).getDay()) {
                                case 0:
                                    theme = 'default';
                                    header = '';
                                    break;
                                case 1:
                                    theme = monCd.toLowerCase();
                                    header = monHours;
                                    break;
                                case 2:
                                    theme = tueCd.toLowerCase();
                                    header = thuHours;
                                    break;
                                case 3:
                                    theme = wedCd.toLowerCase();
                                    header = wedHours;
                                    break;
                                case 4:
                                    theme = thuCd.toLowerCase();
                                    header = thuHours;
                                    break;
                                case 5:
                                    theme = friCd.toLowerCase();
                                    header = friHours;
                                    break;
                                case 6:
                                    theme = 'default';
                                    header = '';
                                    break;
                                default:

                            }
                            calData[day] = {theme: theme, label: label, header: header};

                            for (var i = 0; i < res.list.length; i++) {
                                if(day == res.list[i].scheDt){

                                    label = res.list[i].scheNm;

                                    if(res.list[i].holiYn == 'Y'){
                                        theme = 'holiday';
                                        header = "";
                                    }else{
                                        if (res.list[i].weekCd) {
                                            theme = res.list[i].weekCd.toLowerCase();
                                            header = res.list[i].weekHours;
                                        } else {
                                            switch (new Date(ms).getDay()) {
                                                case 0:
                                                    theme = 'default';
                                                    header = "";
                                                    break;
                                                case 1:
                                                    theme = monCd.toLowerCase();
                                                    header = monHours;
                                                    break;
                                                case 2:
                                                    theme = tueCd.toLowerCase();
                                                    header = tueHours;
                                                    break;
                                                case 3:
                                                    theme = wedCd.toLowerCase();
                                                    header = wedHours;
                                                    break;
                                                case 4:
                                                    theme = thuCd.toLowerCase();
                                                    header = thuHours;
                                                    break;
                                                case 5:
                                                    theme = friCd.toLowerCase();
                                                    header = friHours;
                                                    break;
                                                case 6:
                                                    theme = 'default';
                                                    header = "";
                                                    break;
                                                default:
                                            }
                                        }
                                    }
                                    calData[day] = {theme: theme, label: label, header: header};
                                }
                            }
                        }
                        ////////////일정가져오기
                        var today;

                        if(getDate == '') {
                            today = ax5.util.date((new Date()), {return:'yyyy-MM-dd'});
                        } else {
                            today = getDate;
                        }

                        var originDate = ax5.util.date((new Date()), {return:'yyyy-MM-dd'});

                        if (!(sDt < originDate && originDate < eDt)) {
                            // 현재 날짜가 학사 일정과 일치 하지 않으면 학기 시작일 매핑
                            today = sDt;
                        } else {
                            today = originDate;
                        }

                        var myCalendar = new ax5.ui.calendar({
                            target: document.getElementById("calendar-target"),
                            displayDate: (
                                new Date(today)),
                            control: {
                                left: '<',
                                yearTmpl: '%s년',
                                monthTmpl: '%s',
                                right: '>',
                                yearFirst: true
                            },
                            marker: calData,
                            onClick: function () {

                                getDate = myCalendar.getSelection()[0];

                                ACTIONS.dispatch(ACTIONS.CLEAR_FORM);

                                var selectDate= myCalendar.getSelection();

                                $("#schAtdcDt").val(selectDate);

                                axboot.ajax({
                                    type: "GET",
                                    url: ["/api/v1/attendancemst"],
                                    data: caller.searchView.getData(),
                                    callback: function (res) {
                                        caller.gridView01.setData(res);
                                        caller.gridView02.clear();
                                    },
                                    options: {
                                        onError: function (err) {
                                        }
                                    }
                                });
                            },
                            onStateChanged: function () {
                            },
                            selectable: { range: [{from: sDt, to: eDt}] }
                        }).setSelection([today]);
                        myCalendar.onClick();
                    }
                });
            }
        });
        ///////// 달력만들기
    },
    ITEM_CLICK: function (caller, act, data) {

    },
    ITEM_ADD: function (caller, act, data) {
        cnt++;
        if(cnt > 1){
            var toast = new ax5.ui.toast();
            toast.setConfig({
                theme: "default",
                containerPosition: "bottom-right"
            });
            toast.confirm({
                msg: "저장버튼 클릭 후 추가해 주세요.",
                onStateChanged: function () {

                }
            }, function () {

            });
            return;
        }
        caller.gridView01.addRow();

    },
    ITEM_DEL: function (caller, act, data) {
        var getData = caller.gridView01.getData("selected")[0];

        axboot.ajax({
            type: "DELETE", url: ["/api/v1/attendancemst"],
            data:JSON.stringify(caller.gridView01.getData("selected")[0]),
            callback: function (res) {
            }
        });
        caller.gridView01.delRow("selected");
        caller.gridView02.clear();
    },
    dispatch: function (caller, act, data) {
        var result = ACTIONS.exec(caller, act, data);
        if (result != "error") {
            return result;
        } else {
            return false;
        }
    },
    FORM_CLEAR: function (caller, act, data) {
        axDialog.confirm({
            msg: LANG("ax.script.form.clearconfirm")
        }, function () {
            if (this.key == "ok") {
                caller.formView01.setNew();
            }
        });
    },
    APPLY: function (caller, act, data) {
        if($("#atdcSeq").val()==''||$("#atdcSeq").val()==null){
            alert("수업을 선택해주세요.");
            return;
        }

        var _data = caller.gridView01.getData()[$("#atdcSeq").val()-1];
        var atdcSeq= caller.gridView02.getData()[0].atdcSeq;

                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/attendancedtl"],
                    data: {
                        semeSeq: _data.semeSeq,
                        semeYear: _data.semeYear,
                        clasSeq: _data.clasSeq,
                        periodCd: _data.periodCd,
                        atdcSeq: _data.atdcSeq,
                        atdcDt: _data.atdcDt
                    },
                    callback: function (res) {

                        if(res.list.length <=0){
                            alert($("#atdcSeq").val()+"교시 출석데이터가 존재하지 않습니다.");
                            return;
                        }

                        axboot.ajax({
                            type: "GET",
                            url: ["/api/v1/classdtl/atdcDtlList"],
                            data: {
                                stdtId:_data.stdtId,
                                semeSeq:_data.semeSeq,
                                semeYear:_data.semeYear,
                                clasSeq:_data.clasSeq,
                                periodCd:_data.periodCd,
                                atdcSeq:_data.atdcSeq,
                                atdcDt:_data.atdcDt
                            },
                            callback: function (res) {

                                caller.gridView02.clear();
                                caller.gridView02.setData(res);
                                for(var i =0; i<res.list.length;i++){
                                    caller.gridView02.setValue(i,"atdcSeq",atdcSeq);
                                }
                            }
                        });

                    }
                });
    },
    EXCEL: function () {
        $.form({
            action: '/api/v1/attendancemst/atdcExcel',
            target: 'excelDownFrm',
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schSemeNm : $("#schSemeSeq option:selected").text()
            }
        }).submit();
    },
    CLEAR_FORM : function(caller, act, data){
        $("#atdcSeq").val("");
        $("#atdcCd").val("");
        $("#atdcReason").val("");
        cnt=0;
        $("#atdcSeq option").not(".all").remove();
        $("#atdcSeq").attr("disabled",true);
        $("#apply").attr("disabled",true);
        $("#atdcCd").attr("disabled",true);
        $("#atdcReason").attr("disabled",true);
    }
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "TCHR_DIV", useYn: "Y"},
                callback: function (res) {
                    var tchr = [];
                    res.list.forEach(function (n) {
                        tchr.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.tchr = tchr;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "ATDC_CD", useYn: "Y"},
                callback: function (res) {
                    var atdcCd = [];
                    atdcCd.push({
                        value: null, text: "선택"
                    });
                    res.list.forEach(function (n) {
                        atdcCd.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.atdcCd = atdcCd;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "ATDC_REASON", useYn: "Y"},
                callback: function (res) {
                    var atdcReason = [];
                    atdcReason.push({
                        value: null, text: "선택"
                    });
                    res.list.forEach(function (n) {
                        atdcReason.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.atdcReason = atdcReason;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            //  학년도 select
            axboot.ajax({
                type: "GET",
                url: ["/api/v1/semester/yearCombo"],
                data: {
                    schMyClasChk : $("#schMyClasChk").is(":checked")
                },
                callback: function (res) {
                    for(var i=0;i<res.list.length;i++){
                        $("#schSemeYear").append($('<option>',{
                            value:res.list[i].SEME_YEAR,
                            text:res.list[i].SEME_YEAR
                        }));
                    }
                    $('[data-ax-path="schSemeYear"]').change();
                    ok();
                }
            });
        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.gridView01.initView();
            _this.gridView02.initView();
        });


    // 과정명 select
    $('[data-ax-path="schSemeYear"]').change(function(){
        $("#schSemeSeq option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester/semeSeqCombo"],
            data: {
                schSemeYear : $(this).val(),
                schMyClasChk : $("#schMyClasChk").is(":checked")
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

    // 전후반기구분 select
    $('[data-ax-path="schSemeSeq"]').change(function(){
        $("#schPeriodCd option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/classmst/periodCombo"],
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val()
            },
            callback: function (res) {
                for(var i=0;i<res.list.length;i++){
                    $("#schPeriodCd").append($('<option>',{
                        value:res.list[i].PERIOD_CD,
                        text:res.list[i].NM
                    }));
                }
                $('[data-ax-path="schPeriodCd"]').change();
            }
        });
    });

    // 수강반 select
    $('[data-ax-path="schPeriodCd"]').change(function(){
        axboot.promise()
            .then(function (ok, fail, data) {
                $("#schClasSeq option").remove();
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/classmst/clasSeqCombo"],
                    data: {
                        schSemeYear: $("#schSemeYear").val(),
                        schSemeSeq: $("#schSemeSeq").val(),
                        schPeriodCd: $("#schPeriodCd").val(),
                        schMyClasChk: $("#schMyClasChk").is(":checked")
                    },
                    callback: function (res) {
                        for (var i = 0; i < res.list.length; i++) {
                            $("#schClasSeq").append($('<option>', {
                                value: res.list[i].CLAS_SEQ,
                                text: res.list[i].CLAS_NM
                            }));
                        }
                        ok();
                    }
                });
            })
            .then(function (ok) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                ok();
            });
    });

    // 내수강반 click
    $("#schMyClasChk").click(function(){
        $('[data-ax-path="schSemeYear"]').change();
        //$('[data-ax-path="schPeriodCd"]').change();
    });

    ///////////////강사조회 클릭이벤트
    $(document).on("click",".tchr",function(){

        var index = this.getAttribute("data-custom-btn");

        axboot.modal.open({
            modalType: "TCHR-MODAL",
            param: {

            },
            sendData: function(){
                return {
                    "sendData": "AX5UI"
                };
            },
            callback: function (data) {
                _this.gridView01.setValue(index,"tchrCd",data.userCd);
                _this.gridView01.setValue(index,"tchrNm",data.userNm);
                this.close();
            }
        });
    })

/////////////////출석조회
    $(document).on("click",".atd",function() {

        var index = this.getAttribute("data-custom-btn");
        var data=_this.gridView01.getData()[index];

        $("#apply").attr("disabled",false);
        $("#atdcSeq").attr("disabled",false);
        $("#atdcCd").attr("disabled",false);
        $("#atdcReason").attr("disabled",false);
        var atdcDt = data.atdcDt;
        var atdcSeq = data.atdcSeq;

        //var openYn = $("#schOpenYn").val();

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/classdtl/atdcDtlList"],
            data: {
                stdtId:data.stdtId,
                semeSeq:data.semeSeq,
                semeYear:data.semeYear,
                clasSeq:data.clasSeq,
                periodCd:data.periodCd,
                atdcSeq:data.atdcSeq,
                atdcDt:data.atdcDt
            },
            callback: function (res) {

                _this.gridView02.setData(res);
                for(var i =0; i<res.list.length;i++){
                    _this.gridView02.setValue(i,"atdcSeq",atdcSeq);
                    _this.gridView02.setValue(i,"atdcDt",atdcDt);
                }
                $("#atdcSeq option").not(".all").remove();
                for(var i=0;i<_this.gridView01.getData().length;i++){
                    $("#atdcSeq").append($('<option>',{
                        value:i+1,
                        text:i+1
                    }));
                }

            },
            options: {
                onError: function (err) {
                }
            }
        });
    })

    $("#apply").attr("disabled",true);
    $("#atdcSeq").attr("disabled",true);
    $("#atdcCd").attr("disabled",true);
    $("#atdcReason").attr("disabled",true);

    $("#atdcCd").change(function(){
        var list = _this.gridView02.setAtdc("selected");

        for(var i =0; i<list.length;i++){
            _this.gridView02.setValue(list[i].__index,"atdcCd",$(this).val());
        }
    });

    $("#atdcReason").change(function(){
        var list = _this.gridView02.setAtdc("selected");

        for(var i =0; i<list.length;i++){
            _this.gridView02.setValue(list[i].__index,"atdcReason",$(this).val());
        }
    });

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
                ACTIONS.dispatch(ACTIONS.EXCEL);
            }
        });
    }
});

//== view 시작
/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView0"]);
        this.target.attr("onsubmit", "return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);");
        this.schSemeYear = $("#schSemeYear");
        this.schSemeSeq = $("#schSemeSeq");
        this.schPeriodCd = $("#schPeriodCd");
        this.schClasSeq = $("#schClasSeq");
        this.schAtdcDt = $("#schAtdcDt");

    },
    getData: function () {
        return {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schPeriodCd: this.schPeriodCd.val(),
            schClasSeq: this.schClasSeq.val(),
            schAtdcDt:this.schAtdcDt.val()
        }
    }
});


/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        this.tchr = CODE.tchr;
        var _this = this;

        this.target = axboot.gridBuilder({
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "atdcDt", label: '출결일자', width: 120, align: "center"},
                {key: "atdcSeq", label: '교시', width: 50, align: "center",
                    editor: {
                        type: "number",
                        attributes: {maxlength: 1}
                    }
                },
                {key: "tchrDiv", label: "수업구분", width: 80, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.tchr
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["TCHR_DIV"].map[this.value];
                    }
                },
                {key: "tchrCd", label: '강사ID', width: 120, align: "left"},
                {key: "tchrNm", label: '강사명', width: 130, align: "left"},
                {key: "btn", label: "강사조회", width: 100, align: "center",
                    formatter:function(){
                        var btn = "<button type='button' class='btn btn-primary tchr' data-custom-btn='"+this.dindex+"'><i class='cqc-magnifier'></i>검색</button></div>";

                        return btn;
                    }
                },
                {key: "btn2", label: "출석조회", width: 100, align: "center",
                    formatter:function(){
                        var btn = "<button type='button' class='btn btn-primary atd' data-custom-btn='"+this.dindex+"' ><i class='cqc-magnifier'></i>조회</button></div>";

                        return btn;
                    }
                },
                {key: "semeYear", label: '학년도', width: 100, align: "left", editor: "text",hidden:true},
                {key: "semeSeq", label: '학기시퀀스', width: 100, align: "left", editor: "text",hidden:true},
                {key: "periodCd", label: '전후반기구분', width: 100, align: "left", editor: "text",hidden:true},
                {key: "clasSeq", label: '수강반시퀀스', width: 100, align: "left", editor: "text",hidden:true}
            ],
            body: {
                columnHeight: 32,
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                }
            },
            page: {
                display: false
            }
        });

        axboot.buttonClick(this, "data-grid-view-01-btn", {

            "add": function () {
                if($("#schAtdcDt").val()==""||$("#schAtdcDt").val()==null){
                    alert("일정을 선택해주세요.");
                    return;
                }
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            },
            "delete": function () {
                if($("#schAtdcDt").val()==""||$("#schAtdcDt").val()==null){
                    alert("일정을 선택해주세요.");
                    return;
                }
                axDialog.confirm({
                    msg: "정말 삭제하시겠습니까?"
                }, function () {
                    if (this.key == "ok") {
                        ACTIONS.dispatch(ACTIONS.ITEM_DEL);
                    }
                });
            }
        });
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == "modified" || _type == "deleted" ) {
            list = ax5.util.filter(_list, function () {
                return this.semeYear&&this.semeSeq&&this.periodCd&&this.clasSeq&&this.atdcDt&&this.atdcSeq;
            });
        } else {
            list = _list;
        }
        return list;
    },
    addRow: function () {

        this.target.addRow({__created__: true, atdcDt :$("#schAtdcDt").val(),
            semeYear:$("#schSemeYear").val(),
            periodCd:$("#schPeriodCd").val(),
            clasSeq:$("#schClasSeq").val(),
            semeSeq:$("#schSemeSeq").val(),
            tchrDiv : $("#userTchrDiv").val(),
            tchrCd : $("#userCd").val(),
            tchrNm : $("#userNm").val(),
            atdcSeq: this.target.list.length+1
            }, "last");
    },
    setValue:function(index,target,_data){
        this.target.setValue(index,target,_data);
    }

});

/**
 * gridView
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        this.atdcCd = CODE.atdcCd;
        this.atdcReason = CODE.atdcReason;

        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            frozenColumnIndex: 0,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "semeYear", label: "학년도", width: 130, align: "center",editor:"text",hidden:true},
                {key: "semeSeq", label: "학기시퀀스", width: 130, align: "center",editor:"text",hidden:true},
                {key: "periodCd", label: "전후반기", width: 130, align: "center",editor:"text",hidden:true},
                {key: "clasSeq", label: "수강반시퀀스", width: 130, align: "center",editor:"text",hidden:true},
                {key: "atdcDt", label: "출결일자", width: 130, align: "center",editor:"text",hidden:true},
                {key: "atdcSeq", label: "교시", width: 130, align: "center",editor:"text",hidden:true},
                {key: "stdtId", label: "학번", width: 130, align: "center",editor:"text",hidden:true},
                {key: "natnCd", label: "국적", width: 100, align: "center"},
                {key: "stdtNmKor", label: "학생명", width: 130, align: "left"},
                {key: "stdtNmEng", label: "학생영문명", width: 200, align: "left"},
                {key: "gender", label: "성별", width: 60, align: "center"},
                {key: "birthDt", label: "생년월일", width: 100, align: "center"},
                {key: "atdcCd", label: "출결구분", width: 70, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.atdcCd
                        }
                    },
                    formatter: function formatter() {
                        if(this.value == null || this.value == ""){
                            return "선택";
                        }
                        return parent.COMMON_CODE["ATDC_CD"].map[this.value];
                    }
                },
                {key: "atdcReason", label: "결석사유", width: 70, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.atdcReason
                        }
                    },
                    formatter: function formatter() {
                    if(this.value == null || this.value == ""){
                        return "선택";
                    }
                        return parent.COMMON_CODE["ATDC_REASON"].map[this.value];
                    }
                }
            ],
            body: {

                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-02-btn", {
            "apply": function () {
                ACTIONS.dispatch(ACTIONS.APPLY);
            },
            "delete": function () {
                axDialog.confirm({
                    msg: "정말 삭제하시겠습니까?"
                }, function () {
                    if (this.key == "ok") {
                        ACTIONS.dispatch(ACTIONS.ITEM_DEL);
                    }
                });
            }
        });
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == "modified" || _type == "deleted") {
            list = ax5.util.filter(_list, function () {
                delete this.deleted;
                return this.key;
            });
        } else {
            list = _list;
        }
        return list;
    },
    addRow: function () {
        this.target.addRow({__created__: true}, "last");
    },
    setValue:function(index,target,_data){
        this.target.setValue(index,target,_data);
    },
    setAtdc:function(_type){
        var list = [];
        if (_type == "selected" ) {
            list= this.target.getList(_type);
        }
        return list;
    }

});

