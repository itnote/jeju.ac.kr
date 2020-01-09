var fnObj = {};
var CODE = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    EXCEL_UPLOAD: function (caller, act, data) {
        if (check()) {
            var form = $('#excelUploadForm')[0];
            var formData = new FormData(form);

            $.ajax({
                type : "POST",
                enctype: 'multipart/form-data',
                data: formData,
                url: "/api/v1/classdtl/excelUpload",
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
        for(var i=0; i < saveList.length;i++){
            saveList[i]['semeSeq']=$("#semeSeq").val();
            saveList[i]['semeYear']=$("#semeYear").val();
            saveList[i]['periodCd']=$("#periodCd").val();
            saveList[i]['clasSeq']=$("#clasSeq").val();
        }

        axboot.ajax({
            type: "PUT",
            url: ["/api/v1/classdtl/excelStudentSave"],
            data: JSON.stringify(saveList),
            callback: function (res) {
                var toast = new ax5.ui.toast();
                toast.setConfig({
                    theme: "default",
                    containerPosition: "top-right"
                });

                toast.confirm({
                    msg: res.message,
                    onStateChanged: function () {

                    }
                }, function () {

                });

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

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok, fail, data) {
            axboot.ajax({
                type: "GET", url: ["commonCodes"], data: {groupCd: "STATUS_CD", useYn: "Y"},
                callback: function (res) {
                    var status = [];
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
            "save": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE)
            },
            "excel": function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_UPLOAD)
            },
            "close": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
            }
        });
    }
});

//== view 시작

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.status = CODE.status;
        this.useYn = CODE.useYn;
        this.disc = CODE.disc;

        this.target = axboot.gridBuilder({
            showLineNumber: true,
            frozenColumnIndex: 0,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "stdtId", label: "(*)학번", width: 110, align: "center"},
                {key: "stdtNm", label: "이름", width: 120, align: "left"},
                {key: "statusCd", label: "학적상태", width: 70, align: "center", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"value",optionText:"text"
                            },
                            options:this.status
                        }
                    },
                    formatter: function formatter() {
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
                {key: "generalReview", label: "총평", width: 205, align: "left", editor: "text"},
                {key: "clasSeq", label: "수강반시퀀스", width: 70, align: "center", hidden: "true"},
                {key: "semeYear", label: "학년도", width: 70, align: "center", hidden: "true"},
                {key: "semeSeq", label: "학기시퀀스", width: 70, align: "center", hidden: "true"},
                {key: "periodCd", label: "전후반구분", width: 70, align: "center", hidden: "true"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex);

                }
            }
        });

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



