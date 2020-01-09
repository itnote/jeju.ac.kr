var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    EXCEL_UPLOAD: function (caller, act, data) {
        if (check()) {
            var form = $('#excelUploadForm')[0];
            var formData = new FormData(form);

            $.ajax({
                type : "POST",
                enctype: 'multipart/form-data',
                data: formData,
                url: "/api/v1/student/excelUpload",
                async: false,
                processData: false,
                contentType: false,
                cache: false,
                success : function(data) {
                    fnObj.gridView01.setData(data["list"]);
                    axToast.push("모든 데이터가 업로드 되었습니다.");
                }
            });
        }
    },
    PAGE_SAVE: function (caller, act, data) {

        if(caller.gridView01.getData().length <= 0){
            alert("파일을 업로드해주세요.");
            return;
        }

        var saveList = [].concat(caller.gridView01.getData());

        axboot.ajax({
            type: "PUT",
            url: "/api/v1/student",
            data: JSON.stringify(saveList),
            callback: function (res) {
                axToast.push("저장 되었습니다");
            }
        });
    },
    PAGE_CLOSE: function (caller, act, data) {
        if (parent) {
            parent.axboot.modal.callback();
            parent.axboot.modal.close();
        }
    }
});

var CODE = {};

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "NATN_CD", useYn: "Y"},
                callback: function (res) {
                    var natn = [];
                    res.list.forEach(function (n) {
                        natn.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.natn = natn;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "GENDER", useYn: "Y"},
                callback: function (res) {
                    var gender = [];
                    res.list.forEach(function (n) {
                        gender.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.gender = gender;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "MSG_CD", useYn: "Y"},
                callback: function (res) {
                    var msg = [];
                    res.list.forEach(function (n) {
                        msg.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.msg = msg;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "BANK_CD", useYn: "Y"},
                callback: function (res) {
                    var bank = [];
                    res.list.forEach(function (n) {
                        bank.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.bank = bank;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "TOPIK_CD", useYn: "Y"},
                callback: function (res) {
                    var topik = [];
                    res.list.forEach(function (n) {
                        topik.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.topik = topik;

                    ok();
                }
            });
        })
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "VISA_TYPE", useYn: "Y"},
                callback: function (res) {
                    var visaType = [];
                    res.list.forEach(function (n) {
                        visaType.push({
                            value: n.code, text: n.name + "(" + n.code + ")"
                        });
                    });
                    CODE.visaType = visaType;

                    ok();
                }
            });
        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.gridView01.initView();
        })
        .catch(function () {
        });

};

fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "excel": function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_UPLOAD)
            },
            "save": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE)
            },
            "close": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
            }
        });
    }
});

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.natn = CODE.natn;
        this.gender = CODE.gender;
        this.msg = CODE.msg;
        this.bank = CODE.bank;
        this.topik = CODE.topik;
        this.visaType = CODE.visaType;

        this.target = axboot.gridBuilder({
            showLineNumber: true,
            frozenColumnIndex: 0,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                // 공통코드들 에디터 타입 수정하기.
                {key: "stdtId", label: '학번', width: 110, align: "center", editor: "text"},
                {key: "stdtNmKor", label: '이름', width: 130, align: "left", editor: "text"},
                {key: "stdtNmEng", label: "영문명", width: 200, align: "left", editor: "text"},
                {key: "stdtNmChn", label: "중국어명", width: 100, align: "left", editor: "text"},
                {key: "natnCd", label: "국적", width: 80, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.natn
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["NATN_CD"].map[this.value];
                    }
                },
                {key: "birthDt", label: '생년월일', width: 100, align: "center", editor: "text"},
                {key: "gender", label: "성별", width: 50, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.gender
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["GENDER"].map[this.value];
                    }
                },
                {key: "hpNo", label: '핸드폰번호', width: 100, align: "center", editor: "text"},
                {key: "msgCd1", label: "메신저1", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.msg
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["MSG_CD"].map[this.value];
                    }
                },
                {key: "msgId1", label: '메신저1 ID', width: 100, align: "center", editor: "text"},
                {key: "msgCd2", label: "메신저2", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.msg
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["MSG_CD"].map[this.value];
                    }
                },
                {key: "msgId2", label: '메신저2 ID', width: 100, align: "center", editor: "text"},
                {key: "email", label: '이메일', width: 150, align: "left", editor: "text"},
                {key: "addr", label: '현주소', width: 200, align: "left", editor: "text"},
                {key: "natnAddr", label: '본국주소', width: 200, align: "left", editor: "text"},
                {key: "bankCd", label: "은행", width: 100, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.bank
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["BANK_CD"].map[this.value];
                    }
                },
                {key: "accountNo", label: '계좌번호', width: 150, align: "center", editor: "text"},
                {key: "studyPeriod", label: '학습기간', width: 100, align: "center", editor: "text"},
                {key: "topikCd", label: "토픽등급", width: 80, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.topik
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["TOPIK_CD"].map[this.value];
                    }
                },
                {key: "korLv", label: '한국어레벨', width: 80, align: "center", editor: "text"},
                {key: "visaType", label: "비자유형", width: 80, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.visaType
                        }
                    },
                    formatter: function formatter() {
                        return parent.COMMON_CODE["VISA_TYPE"].map[this.value];
                    }
                }
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex);
                }
            }
        });
    },
    getData: function (_type) {
        var _list = this.target.getList(_type);

        return _list;
    }
});

function checkFileType(filePath) {
    var fileFormat = filePath.split(".");

    if (fileFormat.indexOf("xls") > -1 || fileFormat.indexOf("xlsx") > -1) {
        return true;
    } else {
        return false;
    }
}

function check() {
    var file = $("#excelFile").val();

    if (file == "" || file == null) {
        alert("파일을 선택해주세요.");

        return false;
    } else if (!checkFileType(file)) {
        alert("엑셀 파일만 업로드 가능합니다.");

        return false;
    }
    return true;
}




