var fnObj = {};

var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/counseling/getCounList"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.gridView02.clear();
                caller.formView01.clear();
                caller.gridView02.getData();
            },
            options: {
                onError: function (err) {
                }
            }
        });
        return false;
    },
    PAGE_UPDATE: function (caller, act, data) {
        var selectedIndex = caller.gridView01.getData("selected")[0];
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/counseling/getCounList"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.gridView01.selectData(selectedIndex);
                caller.formView01.clear();
            },
            options: {
                onError: function (err) {
                }
            }
        });
        return false;
    },
    PAGE_SAVE: function (caller, act, data) {

        var parentData = caller.formView01.getData();
        if($("[data-ax-path='clasSeq']").val()=="" ){
            alert("좌측에 학생을 선택해주세요.");
            return ;
        }
        if($("#counDt").val()=="" ){
            alert("면담일자는 필수입력입니다.");
            return ;
        }
        
        axboot.ajax({
            type: "PUT",
            url: ["/api/v1/counseling"],
            data: JSON.stringify(parentData),
            callback: function (res) {
                var selectData = caller.gridView01.getData("selected")[0];
                ACTIONS.dispatch(ACTIONS.PAGE_UPDATE);
                axToast.push("저장 되었습니다");
            }
        });
    },
    ITEM_CLICK: function (caller, act, data) {
        var openYn = $("#schOpenYn").val();

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/counseling"],
            data: {
                stdtId:data.stdtId,
                semeSeq:data.semeSeq,
                semeYear:data.semeYear,
                clasSeq:data.clasSeq,
                periodCd:data.periodCd,
                openYn : openYn
            },
            callback: function (res) {
                caller.gridView02.setData(res);
                caller.formView01.setData(data);
            },
            options: {
                onError: function (err) {

                }
            }
        });
    },
    ITEM_CLICK2: function (caller, act, data) {
        caller.formView01.setData(data);
    },
    ITEM_ADD: function (caller, act, data) {
        caller.gridView01.addRow();
    },
    ITEM_DEL: function (caller, act, data) {
        axboot.ajax({
            type: "PUT",
            url: ["/api/v1/counseling/deleteCoun"],
            data: JSON.stringify({
                counSeq : caller.gridView02.getData("selected")[0].counSeq
            }),
            callback: function (res) {
                axToast.push("삭제 되었습니다")
            }
        });
        caller.gridView02.delRow("selected");
        ACTIONS.dispatch(ACTIONS.PAGE_UPDATE);

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
    DOWN: function (caller, act, data) {
        $.form({
            action: '/api/v1/counseling/tchrCounExcel',
            target: 'excelDownFrm',
            data: this.searchView.getData()
        }).submit();
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
            _this.formView01.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        })
        .catch(function () {

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
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schPeriodCd: this.schPeriodCd.val(),
            schClasSeq: this.schClasSeq.val(),
            schSemeNm : $("#schSemeSeq option:selected").text(),
            schPeriNm : $("#schPeriodCd option:selected").text()
        }
    }
});

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: false,
            frozenColumnIndex: 0,
            lineNumberColumnWidth: 30,
            multipleSelect: false,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "natnNm", label: '국적', width: 80, align: "center"},
                {key: "stdtNmKor", label: '학생명', width: 130, align: "left"},
                {key: "stdtNmEng", label: '학생 영문명', width: 210, align: "left"},
                {key: "genderNm", label: '성별', width: 50, align: "center"},
                {key: "birthDt", label: '생년월일', width: 80, align: "center"},
                {key: "mcnt", label: '출결상담(담임)', width: 100, align: "center"},
                {key: "scnt", label: '출결상담(부)', width: 90, align: "center"},
                {key: "semeYear", label: '학년도', width: 100, align: "left",hidden:"true"},
                {key: "semeSeq", label: '학기', width: 100, align: "left",hidden:"true"},
                {key: "clasSeq", label: '수강반', width: 100, align: "left",hidden:"true"},
                {key: "periodCd", label: '전후반기', width: 100, align: "left",hidden:"true"},
                {key: "stdtId", label: '학번', width: 100, align: "left",hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                }
            }
        });
    },
    getData: function (_type) {
        var list = this.target.getList(_type);
        return list;
    }, selectData: function (_data) {
        this.target.select(_data.__index);
        ACTIONS.dispatch(ACTIONS.ITEM_CLICK, _data);
    }
});

/**
 * gridView
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: false,
            frozenColumnIndex: 0,
            multipleSelect: false,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "counDt", label: "면담일자", width: 130, align: "center"},
                {key: "counNm", label: "면담구분", width: 100, align: "center"},
                {key: "createdByNm", label: "작성자", width: 130, align: "left"},
                {
                    key: "createdAt", label: "작성일자", width: 180, align: "center", formatter: function () {
                        return ax5.util.date(new Date(this.value || ""), {"return": 'yyyy-MM-dd hh:mm:ss'});
                    }
                },
                {key: "counCd", label: "면담구분", width: 350, align: "left",hidden:"true"},
                {key: "contents", label: "내용", width: 100, align: "center",hidden:"true"},
                {key: "counSeq", label: "면담시퀀스", width: 100, align: "center",hidden:"true"},
                {key: "semeYear", label: "학년도", width: 100, align: "center",hidden:"true"},
                {key: "semeSeq", label: "학기", width: 100, align: "center",hidden:"true"},
                {key: "periodCd", label: "전후반기", width: 100, align: "center",hidden:"true"},
                {key: "stdtId", label: "학생아이디", width: 100, align: "center",hidden:"true"},
                {key: "openYn", label: "열람여부", width: 100, align: "center",hidden:"true"},
                {key: "delYn", label: "삭제여부", width: 100, align: "center",hidden:"true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK2, this.item);
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-02-btn", {
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
        var list = this.target.getList(_type);
        return list;
    }
});

/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
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
            "item-add": function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            }
        });
        var openYn = $("#schOpenYn").val();
        if(openYn == "Y"){
            $("#openYn").attr("disabled", true);
        }

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
        //$('#semeYear').not(":selected").attr("disabled", "");
    },
    setNew:function(){

        var data = this.getData();
        data.counSeq = "";
        data.contents = "";
        data.counDt = "";
        data.openYn = "";
        data.counCd = "";

        data = $.extend({}, data);

        this.model.setModel(data);
        this.modelFormatter.formatting(); // 입력된 값을 포메팅 된 값으로 변경
    }
});
