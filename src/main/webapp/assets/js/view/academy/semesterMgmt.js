var fnObj = {}, CODE = {}, getDate = '';
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester"],
            data: this.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.formView01.clear();
                caller.gridView02.clear();
                jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);
            }
        });
        return false;
    },
    PAGE_UPDATE: function (caller, act, data) {
        var selectedIndex = caller.gridView01.getData("selected")[0];
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.formView01.clear();
                caller.gridView02.clear();
                caller.gridView01.setData(res);
                caller.gridView01.selectData(selectedIndex);
                ACTIONS.dispatch(ACTIONS.ITEM_CLICK, res.list[selectedIndex.__index]);
            },
            options: {
                onError: function (err) {
                }
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
            var checkReturn= false;

            // childList에 parentKey 삽입
            childList.forEach(function (n) {
                if(n.sbjtId == null||n.sbjtId ==""){
                    alert("과목명을 선택해주세요.");
                    checkReturn=true;
                    return;
                }
                n.semeYear = parentData.semeYear;
                n.semeSeq = parentData.semeSeq;
                n.lang.kor = parent.COMMON_CODE["SBJT_ID"].map[n.sbjtId];
            });
            if(checkReturn){
                return;
            }

            axboot.promise()
                .then(function (ok, fail, data) {
                    axboot.ajax({
                        type: "PUT",
                        url: ["/api/v1/semester"],
                        data: JSON.stringify([parentData]),
                        callback: function (res) {
                            ok(res); //get semeSeq
                        }
                    });
                })
                .then(function (ok, fail, data) {

                    childList.forEach(function (n) {
                        n.semeSeq = data.message;
                    });

                    axboot.ajax({
                        type: "PUT",
                        url: ["/api/v1/subject"],
                        data: JSON.stringify(childList),
                        callback: function (res) {
                            ok(res);
                            axToast.push("저장 되었습니다");
                            var isCreated = caller.formView01.getData().__created__;
                            if (isCreated === false) {
                                ACTIONS.dispatch(ACTIONS.PAGE_UPDATE);
                            } else {
                                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                            }

                        }
                    });
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
                //caller.formView01.clear();
                //caller.gridView02.clear();
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            }
        });
    },
    ITEM_CLICK: function (caller, act, data) {

        caller.formView01.setData(data);
        axboot.ajax({
            type: "GET",
            url: "/api/v1/subject",
            data: data,
            callback: function (res) {
                caller.gridView02.setData(res);
            }
        });
        $('#semeYear').attr("disabled", true);
        jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);
    },
    ITEM_DEL: function (caller, act, data) {
        caller.gridView02.delRow("selected");
    },
    ITEM_ADD: function (caller, act, data) {
        caller.gridView02.addRow();
    },
    DOWN: function (caller, act, data) {
        var list = caller.gridView01.getData("selected");
        if (list.length == 0) {
            alert("학기를 선택해 주세요.");
            return;
        }
        $.form({
            action: '/api/v1/semester/scheduleExcel',
            target: 'excelDownFrm',
            data: list
        }).submit();
    }
});



fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "SBJT_ID", useYn: "Y"},
                callback: function (res) {
                    var sbjtId = [];
                    res.list.forEach(function (n) {
                        sbjtId.push({
                            value: n.code, text: n.name
                        });
                    });
                    CODE.sbjtId = sbjtId;

                    ok();
                }
            });
        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.gridView01.initView();
            _this.gridView02.initView();
            _this.formView01.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        })
        .catch(function () {

        });

///////// select 만들기
    var year = new Date().getFullYear() -3;

    for(var i = 0; i<5;i++){
        $('[data-ax-path="semeYear"]').append($('<option>',{
            value:year+i,
            text:year+i
        }));

        $('[data-ax-path="schSemeYear"]').append($('<option>',{
            value:year+i,
            text:year+i
        }));

    }
///////// ax select 만들기

///////// 달력만들기
    jQuery('[data-ax5layout="ax2"]').ax5layout({
        onOpenTab: function () {
            if(this.activePanelIndex == 1){

                var semeSeq =  $("#semeSeq").val();
                var semeYear =  $("#semeYear").val();
                var sDt, eDt;
                var monCd, tueCd, wedCd, thuCd, friCd;
                var monHours, tueHours, wedHours, thuHours, friHours;

                if(semeSeq=="" ||semeSeq==null){
                    axToast.push("학기를 선택해주세요.");
                    jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);
                    return;
                }

                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/semester"],
                    data: {
                        semeYear: semeYear,
                        semeSeq: semeSeq
                    },
                    callback: function (res) {

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
                                var calData ={};
                                var theme = "";
                                var label ="";
                                var header ="";
                                // '2019-02-01': {theme: 'a2', label: '개강'},
                                // '2019-02-07': {theme: 'm4', label: '입학식'},
                                // '2019-02-18': {theme: 'm2', label: '중간고사'},
                                // '2019-02-04': {theme: 'holiday', label: '설날'},
                                //
//////////일정가져오기
                                var oneDay = 24*3600*1000;
                                var m;
                                var y;
                                var d;
                                var day;

                                for (var ms= new Date(sDt)*1,last= new Date(eDt)*1;ms<=last;ms+=oneDay) {
                                    label ="";
                                    y = new Date(ms).getFullYear();
                                    m = Number(new Date(ms).getMonth() + 1);
                                    d = new Date(ms).getDate();

                                    if (m < 10) {
                                        m = '0' + m;
                                    }
                                    if (d < 10) {
                                        d = '0' + d;
                                    }
                                    day = y + "-" + m + "-" + d;

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
                                            } else {
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


                                var today;

                                if(getDate == '') {
                                    today = ax5.util.date((new Date()), {return:'yyyy-MM-dd'});
                                } else {
                                    today = getDate;
                                }


                                //////////// 일정가져오기 /////////////
                                var myCalendar = new ax5.ui.calendar({
                                    target: document.getElementById("calendar-target"),
                                    displayDate: (new Date(today)),
                                    control: {
                                        left: '<',
                                        yearTmpl: '%s년',
                                        monthTmpl: '%s',
                                        right: '>',
                                        yearFirst: true
                                    },
                                    marker: calData,
                                    onClick: function () {
                                        if(userMenuGrpCd=='USER'){
                                            return;
                                        }
                                        getDate = myCalendar.getSelection()[0];
                                        var selectDate= myCalendar.getSelection();

                                        var scheNm ="";
                                        var holiYn ="N";
                                        var counYn ="Y";
                                        var weekCd ="";

                                        axboot.ajax({
                                            type: "GET",
                                            url: ["/api/v1/schedule"],
                                            data: {semeYear:semeYear, semeSeq : semeSeq, scheDt :selectDate[0]},
                                            callback: function (res) {
                                                if(res.list.length > 0){
                                                    scheNm=res.list[0].scheNm;
                                                    holiYn=res.list[0].holiYn;
                                                    counYn=res.list[0].counYn;
                                                    weekCd=res.list[0].weekCd;
                                                }

                                                axboot.modal.open({
                                                    modalType: "SCHE-MODAL",
                                                    param: {
                                                        semeSeq: semeSeq,
                                                        semeYear: semeYear,
                                                        scheDt: selectDate[0],
                                                        scheNm: scheNm ,
                                                        holiYn: holiYn,
                                                        counYn:counYn,
                                                        weekCd:weekCd
                                                    },
                                                    sendData: function () {
                                                        return {
                                                            "sendData": "AX5UI"
                                                        };
                                                    },
                                                    callback: function (data) {
                                                        axToast.push(data);
                                                        jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 0);
                                                        jQuery('[data-ax5layout="ax2"]').ax5layout("tabOpen", 1);

                                                    }
                                                });


                                            }
                                        })
                                    },
                                    onStateChanged: function () {

                                    },
                                    selectable: { range: [{from: sDt, to: eDt}] }
                                });

                            }
                        });
                    }
                });
///////// 달력만들기 end

            }

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
            "fn1": function () {
                ACTIONS.dispatch(ACTIONS.DOWN);
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
        this.semeYear = $("#schSemeYear");
        this.semeCd = $("#schSemeCd");

    },
    getData: function () {
        return {
            semeYear: this.semeYear.val(),
            semeCd: this.semeCd.val(),
        }
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
            frozenColumnIndex: 0,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "semeYear", label: "학기", width: 100, align: "center",hidden:"true"},
                {key: "semeSeq", label: "학기시퀀스", width: 80, align: "left",hidden:"true"},
                {key: "corsNm", label: "과정명(키)", width: 120, align: "left",hidden:"true"},
                {key: "semeNm", label: "학기", width: 120, align: "center"},
                {key: "kor", label: "과정명", width: 150, align: "left"},
                {key: "eng", label: "과정명", width: 120, align: "left",hidden:"true"},
                {key: "chn", label: "과정명", width: 120, align: "left",hidden:"true"},
                {key: "semeCd", label: "학기코드", width: 80, align: "left",hidden:"true"},
                {key: "totHours", label: "기준시수", width: 120, align: "right",hidden:"true"},
                {key: "startDt", label: "시작일", width: 120, align: "center"},
                {key: "endDt", label: "종료일", width: 120, align: "center"},
                {key: "monCd", label: "월요일_수업여부", width: 120, align: "left",hidden:"true"},
                {key: "tueCd", label: "화요일_수업여부", width: 120, align: "left",hidden:"true"},
                {key: "wedCd", label: "수요일_수업여부", width: 120, align: "left",hidden:"true"},
                {key: "thuCd", label: "목요일_수업여부", width: 120, align: "left",hidden:"true"},
                {key: "friCd", label: "금요일_수업여부", width: 120, align: "left",hidden:"true"},
                {key: "newRegStdAmt", label: "신입생 등록금 납부 기준금액", width: 70, align: "right",hidden:"true"},
                {key: "newDormStdAmt", label: "신입생 기숙사 납부 기준금액", width: 70, align: "right",hidden:"true"},
                {key: "oldRegStdAmt", label: "재학생 등록금 납부 기준금액", width: 70, align: "right",hidden:"true"},
                {key: "newAppStdAmt", label: "신입생전형료", width: 70, align: "right",hidden:"true"},
                {key: "newInsStdAmt", label: "신입생의료보험료", width: 70, align: "right",hidden:"true"},
                {key: "newBedStdAmt", label: "신입생침구", width: 70, align: "right",hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-01-btn", {
            "add": function () {
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            },
            "delete": function () {
                ACTIONS.dispatch(ACTIONS.ITEM_DEL);
            }
        });
    },
    getData: function (_type) {
        var list = this.target.getList(_type);
        return list;
    }, selectData: function (_data) {
        this.target.select(_data.__index);
    }
});

/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
        $('#semeYear').attr("disabled", false);
        return $.extend({}, axboot.formView.defaultData, {
            lang: {}
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
        this.initEvent();

        axboot.buttonClick(this, "data-form-view-01-btn", {
            "form-clear": function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);

            }
        });
    },
    initEvent: function () {
    },
    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.
        return $.extend({}, data);
    },
    setData: function (data) {
        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);

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
    }
});

/**
 * gridView02
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        var _this = this;
        this.sbjtId = CODE.sbjtId;

        this.target = axboot.gridBuilder({
            showLineNumber: false,
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "semeYear", label: "학년도", width: 70, align: "center", editor: "text", hidden:"true"},
                {key: "semeSeq", label: "학기시퀀스", width: 70, align: "center", editor: "text", hidden:"true"},
                {key: "sbjtNm", label: "과목명", width: 70, align: "center", editor: "text", hidden: "true"},
                {key: "sbjtId", label: "과목명", width: 120, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.sbjtId
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["SBJT_ID"].map[this.value];
                    }
                },
                {key: "lang.kor", label: "한국과목명", width: 120, align: "left", editor: "text", hidden: "true"},
                {key: "lang.eng", label: "영문과목명", width: 200, align: "left", editor: "text"},
                {key: "lang.chn", label: "중국과목명", width: 180, align: "left", editor: "text"},
                {key: "printYn", label: "성적증명서표시여부", width: 180, align: "center", editor: "checkYn"}

            ],
            body: {
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
                return this.semeYear&&this.semeSeq&&this.sbjtId;
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
        this.target.addRow({__created__: true, printYn: "Y", lang :{}}, "last");
    }

});