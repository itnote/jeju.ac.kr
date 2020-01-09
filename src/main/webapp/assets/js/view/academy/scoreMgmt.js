var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/scoremst"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.gridView02.clear();
                caller.gridView03.clear();
            },
            options: {
                onError: function (err) {

                }
            }
        });
        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        var saveList = [].concat(caller.gridView02.getData("modified"));
        var valid = true;

        //점수 수정시 변경사유 필수입력
        // saveList.some(function(item) {
        //     //점수 최초입력이 아니면
        //     if (item.oriScor != undefined && (item.reason == undefined || item.reason == "")) {
        //         axToast.push({ msg: LANG("ax.script.form.validate", "변경사유"), theme: "danger"});
        //
        //         // caller.gridView02.target.select(item.__index, {selectedClear: true}); //grouping row가 존재하는경우 실제 index랑 달라서 사용 불가
        //         valid = false;
        //         return true;    //break some
        //     }
        // });

        if (valid) {
            axboot.ajax({
                type: "PUT",
                url: ["/api/v1/scoredtl"],
                data: JSON.stringify(saveList),
                callback: function (res) {
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, caller.gridView01.getData("selected")[0]);
                    axToast.push(LANG("onsave"));
                }
            });
        }
    },
    ITEM_CLICK: function (caller, act, data) {
        axboot.promise()
            .then(function (ok) {
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/scoredtl"],
                    data: {
                        schSemeYear : data.semeYear,
                        schSemeSeq  : data.semeSeq,
                        schClasSeq  : data.clasSeq,
                        schPeriodCd : data.periodCd,
                        schStdtId   : data.stdtId,
                        schLv       : data.lv
                    },
                    callback: function (res) {
                        caller.gridView02.setData(res);
                        ok();
                    },
                    options: {
                        onError: function (err) {

                        }
                    }
                });
            })
            .then(function (ok) {
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/scorelog"],
                    data: {
                        schSemeYear : data.semeYear,
                        schSemeSeq  : data.semeSeq,
                        schClasSeq  : data.clasSeq,
                        schPeriodCd : data.periodCd,
                        schStdtId   : data.stdtId
                    },
                    callback: function (res) {
                        caller.gridView03.setData(res);
                    },
                    options: {
                        onError: function (err) {
                        }
                    }
                });
            })
            .catch(function () {

            });
    },
    EXCEL: function () {
        $.form({
            action: '/api/v1/scoremst/scoreExcel',
            target: 'excelDownFrm',
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schClasSeq : $("#schClasSeq").val(),
                schPeriodCd : $("#schPeriodCd").val(),
                schMyClasChk : $("#schMyClasChk").is(":checked").toString(),
                schSemeNm : $("#schSemeSeq option:selected").text()
            }
        }).submit();
    },
    dispatch: function (caller, act, data) {
        var result = ACTIONS.exec(caller, act, data);
        if (result != "error") {
            return result;
        } else {
            return false;
        }
    }
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
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
                }
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
                    }
                });
            });

            // 수강반 select
            $('[data-ax-path="schPeriodCd"]').change(function(){
                $("#schClasSeq option").remove();
                axboot.ajax({
                    type: "GET",
                    url: ["/api/v1/classmst/clasSeqCombo"],
                    data: {
                        schSemeYear : $("#schSemeYear").val(),
                        schSemeSeq : $("#schSemeSeq").val(),
                        schPeriodCd : $(this).val(),
                        schMyClasChk : $("#schMyClasChk").is(":checked")
                    },
                    callback: function (res) {
                        for(var i=0;i<res.list.length;i++){
                            $("#schClasSeq").append($('<option>',{
                                value:res.list[i].CLAS_SEQ,
                                text:res.list[i].CLAS_NM
                            }));
                        }
                        ok();
                    }
                });
            });

            // 내수강반 click
            $("#schMyClasChk").click(function(){
                $('[data-ax-path="schSemeYear"]').change();
                //$('[data-ax-path="schPeriodCd"]').change();
            });
        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.gridView01.initView();
            _this.gridView02.initView();
            _this.gridView03.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        })
        .catch(function () {

        });

};

fnObj.pageResize = function () {

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

/**
 * 검색조건
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView0"]);
        this.target.attr("onsubmit", "return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);");
        this.schSemeYear = $("#schSemeYear");
        this.schSemeSeq = $("#schSemeSeq");
        this.schPeriodCd = $("#schPeriodCd");
        this.schClasSeq = $("#schClasSeq");
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schPeriodCd: this.schPeriodCd.val(),
            schClasSeq: this.schClasSeq.val()
        }
    }
});


/**
 * 학생목록
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "natnNm", label: '국적', width: 70, align: "center"},
                {key: "stdtNmKor", label: '학생명', width: 110, align: "left"},
                {key: "stdtNmEng", label: '학생 영문명', width: 140, align: "left"},
                {key: "genderNm", label: '성별', width: 40, align: "center"},
                {key: "birthDt", label: '생년월일', width: 80, align: "center"},
                {key: "totScor", label: '최종점수', width: 60, align: "right"},
                {key: "scorNm", label: '이수구분', width: 70, align: "center"},
                {key: "failResn", label: '유급사유', width: 100, align: "center"},
                {key: "semeYear", label: '학년도', hidden:"true"},
                {key: "semeSeq", label: '학기', hidden:"true"},
                {key: "clasSeq", label: '수강반', hidden:"true"},
                {key: "periodCd", label: '전후반기', hidden:"true"},
                {key: "stdtId", label: '학번', hidden:"true"},
                {key: "tchrCd", label: '담임', hidden:"true"},
                {key: "subTchrCd", label: '부담임', hidden:"true"},
                {key: "lv", label: 'LEVEL', hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                }
            }
        });

        /**
         * 성적 집계
         */
        axboot.buttonClick(this, "data-grid-view-01-btn", {
            "score_calc": function () {
                if (confirm(LANG("ax.script.calc.confirm"))) {
                    var saveList = [].concat(this.getData());

                    axboot.ajax({
                        type: "PUT",
                        url: ["/api/v1/scoremst/saveScore"],
                        data: JSON.stringify(saveList),
                        callback: function (res) {
                            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                            axToast.push({msg: LANG(res.message), theme: "success"});
                        },
                        options: {
                            onError: function (err) {

                            }
                        }
                    });
                }
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
    }
});

/**
 * 과목별성적
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "examNm", label: '시험구분', width: 90, align: "center"},
                {key: "sbjtNmKor", label: '과목명', width: 120, align: "center"},
                {key: "scor", label: '시험', width: 70, align: "right",
                    editor: {
                        type: "number",
                        attributes: {maxlength: 4},
                        updateWith: ['totScor', 'excScor'],
                        disabled: fnChkDisable
                    }
                },
                {key: "perf", label: '수행평가', width: 70, align: "right",
                    editor: {
                        type: "number",
                        attributes: {maxlength: 4},
                        updateWith: ['totScor', 'excScor'],
                        disabled: fnChkDisable
                    }
                },
                {key: "totScor", label: '총점', width: 70, align: "right",
                    formatter: function() {
                        if (this.item.__modified__) {
                            console.log(this.item);
                            if (parseFloat((Number(this.item.scor) + Number(this.item.perf)).toFixed(1)) > 100) {
                                alert("총점은 100점을 초과할 수 없습니다.");

                                var idx =  this.item.__index;
                                if (this.item.examCd == "F") idx++; //__index에서는 소계grouping row를 인식 못하기 때문에 기말고사면 idx+1 해준다.

                                // _this.target.updateRow($.extend({}, _this.target.list[idx], {scor: Number(this.item.oriScor), perf: Number(this.item.oriPerf)}), idx);
                                if (this.item.scor != this.item.oriScor) {
                                    _this.target.setValue(idx, "scor", Number(this.item.oriScor==undefined ? 0 : this.item.oriScor));
                                }
                                if (this.item.perf != this.item.oriPerf) {
                                    _this.target.setValue(idx, "perf", Number(this.item.oriPerf==undefined ? 0 : this.item.oriPerf));
                                }
                            }
                        }
                        return parseFloat((Number(this.item.scor) + Number(this.item.perf)).toFixed(1));
                    }
                },
                {key: "excScor", label: '환산점수', width: 70, align: "right",
                    formatter: function() {
                        return parseFloat(((Number(this.item.scor) + Number(this.item.perf)) * (this.item.evalRatio * 0.01)).toFixed(2));
                    }
                },
                {key: "reason", label: '변경사유', width: 200, align: "center",
                    editor: {
                        type: "text",
                        disabled: function() {
                            return this.item.oriScor==undefined;
                        }
                    }
                },
                {key: "updatedByNm", label: '작성자', width: 100, align: "center"},
                {key: "updatedAt", label: '작성일자', width: 150, align: "center",
                    formatter: function () {
                        return this.value == undefined ? "" : ax5.util.date(new Date(this.value), {"return": 'yyyy-MM-dd hh:mm:ss'});
                    }
                },
                {key: "semeYear", label: '학년도', hidden:"true"},
                {key: "semeSeq", label: '학기', hidden:"true"},
                {key: "clasSeq", label: '수강반', hidden:"true"},
                {key: "periodCd", label: '전후반기', hidden:"true"},
                {key: "stdtId", label: '학번', hidden:"true"},
                {key: "sbjtId", label: '과목코드', hidden:"true"},
                {key: "examCd", label: '시험구분코드', hidden:"true"},
                {key: "oriScor", label: '수정전 성적', hidden:"true"},
                {key: "oriPerf", label: '수정전 수행평가', hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                },
                mergeCells: ["examNm"],
                grouping: {
                    by: ["examNm"],
                    columns: [
                        {
                            label: function () {
                                return this.groupBy.labels.join(", ") + " 결과";
                            }, colspan: 5, align: "center"
                        },
                        {
                            key: "excScor", collector: function () {
                                var value = Number(0);
                                this.list.forEach(function (n) {
                                    if (!n.__isGrouping) {
                                        value += parseFloat(((Number(n.scor) + Number(n.perf)) * (Number(n.evalRatio) * 0.01)).toFixed(2));
                                    }
                                });
                                return parseFloat(value.toFixed(2));
                            }, align: "right"
                        }
                    ]
                }
            },
            page: {
                display: false
            }
        });

        this.target.setColumnSort({
            examNm: {orderBy: "desc", seq: 0}
        });
    },
    getData: function (_type) {
        return this.target.getList(_type);
    }
});

/**
 * 성적수정이력
 */
fnObj.gridView03 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            target: $('[data-ax5grid="grid-view-03"]'),
            columns: [
                {key: "examNm", label: '시험구분', width: 100, align: "center"},
                {key: "sbjtNmKor", label: '과목명', width: 120, align: "center"},
                {key: "oldScor", label: '이전성적', width: 80, align: "right"},
                {key: "newScor", label: '현재성적', width: 80, align: "right"},
                {key: "reason", label: '변경사유', width: 230, align: "left"},
                {key: "createdByNm", label: '작성자', width: 100, align: "center"},
                {key: "createdAt", label: '작성일자', width: 150, align: "center", formatter: function () {
                        return ax5.util.date(new Date(this.value), {"return": 'yyyy-MM-dd hh:mm:ss'});
                }},
                {key: "logId", label: '성적이력ID', hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                }
            }
        });
    }
});

/**
 * 성적 수정 권한체크
 */
var fnChkDisable = function () {
    var pGrid = fnObj.gridView01.getData("selected")[0];
    return (userMenuGrpCd != "SYSTEM_MANAGER" && pGrid.tchrCd != SCRIPT_SESSION.userCd && pGrid.subTchrCd != SCRIPT_SESSION.userCd);
};

