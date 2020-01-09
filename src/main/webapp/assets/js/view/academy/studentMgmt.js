var staticStdId = "";
var staticPanelIndex = 0;
var changeIndex = "S";

var fnObj = {}, CODE = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: "/api/v1/student/studentList",
            data: this.searchView.getData(),
            // data: $.extend({}, this.searchView.getData(), this.gridView01.getData()),
            callback: function (res) {
                UPLOAD.clear();
                caller.gridView01.setData(res);
                caller.formView01.clear();
                caller.gridView02.clear();
                caller.gridView04.clear();
            }
        });

        return false;
    },
    PAGE_UPDATE: function (caller, act, data) {
        var selectedIndex = caller.gridView01.getData("selected")[0];
        axboot.ajax({
            type: "GET",
            url: "/api/v1/student/studentList",
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.formView01.clear();
                caller.gridView02.clear();
                caller.gridView04.clear();
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
        var saveList = [].concat(caller.formView01.getData("modified"));

        axboot.ajax({
            type: "PUT",
            url: "/api/v1/student",
            data: JSON.stringify(saveList),
            callback: function (res) {
                var isCreated = caller.formView01.getData().__created__;
                if (isCreated === false) {
                    ACTIONS.dispatch(ACTIONS.PAGE_UPDATE);
                } else {
                    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                }
                axToast.push("저장 되었습니다");
            }
        });

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
        var profileId = caller.gridView01.getData("selected")[0]["photoFileId"];
        if(caller.gridView01.getData("selected")[0]["photoFileId"] != null) {
            profileLoad(profileId)
        } else {
            profileDefault()
        }
        caller.formView01.setData(data);

        axboot.ajax({
            type: "GET",
            url: "/api/v1/classdtl",
            data: caller.formView01.getData(),
            callback: function (res) {
                caller.gridView02.setData(res);
                caller.gridView04.clear();
            }
        });

    },
    LOG_INIT: function (caller, act, data) {
        staticStdId = caller.gridView01.getData("selected")[0]["stdtId"];
        if(staticPanelIndex == 0 ) {
            changeIndex = "S"
        } else if(staticPanelIndex == 1) {
            changeIndex = "T"
        } else if(staticPanelIndex == 2) {
            changeIndex = "A"
        }
       axboot.ajax({
           type: "GET",
           url:  "/api/v1/stdtlog",
           data: "stdtId=" + staticStdId + "&stdtLogCd="+ changeIndex,
           callback: function (res) {
               if(changeIndex === "S" ) {
               } else if(changeIndex === "T") {
                   caller.gridView03.setData(res);
               } else if(changeIndex === "A") {
                   caller.gridView04.setData(res);
               }
           }
       })
    },
    EXCEL_MODAL: function (caller, act, data) {
        axboot.modal.open({
            modalType: "EXCEL-MODAL",
            param: "",
            sendData: function(){
                return {
                    "sendData": "AX5UI"
                };
            },
            callback: function (data) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                this.close();
            }
        });
    },
    EXCEL_DOWN: function (caller, act, data) {
        $.form({
            action: '/api/v1/student/studentExcel',
            target: 'excelDownFrm',
            data: this.searchView.getData()
        }).submit();
    }
});

fnObj.pageStart = function () {

    var _this = this;
    axboot
        .promise()
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
                    $('[data-ax-path="schSemeYear"]').change();
                    ok();
                }
            });
        })

        .then(function(ok, fail, data) {
            axboot.ajax({
                type: "GET",
                url: ["commonCodes"],
                data: {groupCd: "USER_ROLE", useYn: "Y"},
                callback: function (res) {
                    var userRole = [];
                    res.list.forEach(function (n) {
                        userRole.push({
                            value: n.code, text: n.name + "(" + n.code + ")",
                            roleCd: n.code, roleNm: n.name,
                            data: n
                        });
                    });
                    this.userRole = userRole;
                    ok();
                }
            })
        })
        .then(function (ok, fail) {

            CODE = this; // this는 call을 통해 수집된 데이터들.

            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.gridView01.initView();
            _this.gridView02.initView();
            _this.formView01.initView();
            _this.gridView03.initView();
            _this.gridView04.initView();
            // _this.uploadView.initView();
            ok();

        })
        .then(function (ok, fail) {
            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            ok();
        })




    $('[data-ax-path="schSemeYear"]').change(function(){
        $("#schSemeSeq option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester/semeSeqCombo"],
            data: {
                schSemeYear : $(this).val()
                //schMyClasChk : $("#schMyClasChk").is(":checked")
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
                $("#schPeriodCd").append($('<option>',{
                    value:"",
                    text:"전체"
                }))
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

     $('[data-ax-path="schPeriodCd"]').change(function(){
         ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
     });






    /**
    Tab View
     */
    jQuery('[data-ax5layout="ax2"]').ax5layout({
        onOpenTab: function () {
            staticPanelIndex = this.activePanelIndex;
            ACTIONS.dispatch(ACTIONS.LOG_INIT, this.item);
        }
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
            "excel": function() {
                ACTIONS.dispatch(ACTIONS.EXCEL_DOWN);
            },
            "fn1": function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_MODAL);
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
        this.schStdt = $("#schStdt");
        this.semeYear = $("#schSemeYear");
        this.semeSeq = $("#schSemeSeq");
        this.periodCd = $("#schPeriodCd");
    },
    getData: function () {
        return {
            schStdt: this.schStdt.val(),
            schSemeYear: this.semeYear.val(),
            schSemeSeq: this.semeSeq.val(),
            schPeriodCd: this.periodCd.val(),
            isClassMgmt : 'N'
        }
    }


});




/**
 * gridView01
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    page: {
        pageNumber: 0,
        pageSize: 10
    },
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "stdtId", label: "학번", width: 110, align: "center"},
                {key: "natnNm", label: "국적", width: 100, align: "center"},
                {key: "stdtNmKor", label: "학생명", width: 130, align: "left"},
                {key: "stdtNmEng", label: "학생영문명", width: 200, align: "left"},
                {key: "birthDt", label: "생년월일", width: 100, align: "center"},
                {key: "hpNo", label: "핸드폰", width: 118, align: "center"}
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex);
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                    ACTIONS.dispatch(ACTIONS.LOG_INIT, this.item);
                }
            },
            onPageChange: function (pageNumber) {
                _this.setPageData({pageNumber: pageNumber});
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            }
        });
    },
    selectData: function (_data) {
        this.target.select(_data.__index);
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == "modified" || _type == "deleted") {
            list = ax5.util.filter(_list, function () {
                return this.stdtId;
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
        var _this = this;
    },
    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.
        return $.extend({}, data);
    },
    setData: function (data) {

        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);

        this.target.find('[data-ax-path="stdtId"]').attr("readonly", "readonly");
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
        this.target.find('[data-ax-path="stdtId"]').removeAttr("readonly");
        profileDefault()
    }
});

/**
 * gridView02
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        var _this = this;

        this.target = axboot.gridBuilder({
            showLineNumber: false,
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "semeYear", label: "학년도", width: 80, align: "center"},
                {key: "corsNm", label: "학기명", width: 180, align: "left"},
                {key: "periodNm", label: "전후반기", width: 100, align: "center"},
                {key: "clasNm", label: "반명", width: 80, align: "left"},
                {key: "lv", label: "레벨", width: 50, align: "center"},
                {key: "tchrNm", label: "담임", width: 130, align: "left"},
                {key: "subTchrNm", label: "부담임", width: 130, align: "left"}
            ],
            body: {
                onClick: function () {
                    //this.self.select(this.dindex);
                    //ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-02-btn", {
            "item-add": function () {
                this.addRow();
            },
            "item-remove": function () {
                this.delRow();
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
                return this.key;
            });
        } else {
            list = _list;
        }
        return list;
    },
    align: function () {
        this.target.align();
    }
});


/**
 * gridView03
 */
fnObj.gridView03 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        var _this = this;

        this.target = axboot.gridBuilder({
            showLineNumber: false,
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-03"]'),
            columns: [
                {key: "createdAt", label: "변경일자", width: 250, align: "center", formatter: function () {
                        return ax5.util.date(new Date(this.value || ""), {"return": 'yyyy-MM-dd hh:mm:ss'});
                    }},
                {key: "logData", label: "연락처", width: 200, align: "center"},
            ],
            body: {
                onClick: function () {
                    //this.self.select(this.dindex);
                    //ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-03-btn", {
            "item-add": function () {
                this.addRow();
            },
            "item-remove": function () {
                this.delRow();
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
                return this.stdtId == stdtId;
            });
        } else {
            list = _list;
        }
        return list;
    },
    align: function () {
        this.target.align();
    }
});


/**
 * gridView04
 */
fnObj.gridView04 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        var _this = this;

        this.target = axboot.gridBuilder({
            showLineNumber: false,
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-04"]'),
            columns: [
                {key: "createdAt", label: "변경일자", width: 250, align: "center", formatter: function () {
                        return ax5.util.date(new Date(this.value || ""), {"return": 'yyyy-MM-dd hh:mm:ss'});
                    }},
                {key: "logData", label: "주소", width: 200, align: "left"},
            ],
            body: {
                onClick: function () {
                    //this.self.select(this.dindex);
                    //ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-04-btn", {
            "item-add": function () {
                this.addRow();
            },
            "item-remove": function () {
                this.delRow();
            }
        });
    },
    setData: function (_data) {
        this.target.setData(_data);
    },
    getData: function (stdtId) {
        var list = ax5.util.filter(this.target.getList(), function () {
            return this.stdtId == stdtId;
        });
        return list;
        // var list = [];
        // var _list = this.target.getList(_type);
        //
        // if (_type == "modified" || _type == "deleted") {
        //     list = ax5.util.filter(_list, function () {
        //         return this.key;
        //     });
        // } else {
        //     list = _list;
        // }
        // return list;
    },
    align: function () {
        this.target.align();
    }
});


/**
image upload(ax5ui-uploader)
 */
var DIALOG = new ax5.ui.dialog({
    title: "학사관리시스템"
});

var UPLOAD = new ax5.ui.uploader({
    //debug: true,
    target: $('[data-ax5uploader="upload_img"]'),
    form: {
        action: "/api/v1/image/",
        fileName: "file"
    },
    multiple: false,
    manualUpload: false,
    progressBox: true,
    progressBoxDirection: "left",
    dropZone: {
        target: $('[data-uploaded-box="upload_img"]')
    },
    uploadedBox: {
        target: $('[data-uploaded-box="upload_img"]'),
        icon: {
            "download": '<i class="fa fa-download" aria-hidden="true"></i>',
            "delete": '<i class="fa fa-minus-circle" aria-hidden="true"></i>'
        },
        columnKeys: {
            apiServerUrl: '',
            name: "fileNm",
            type: "extension",
            size: "fileSize",
            uploadedName: "saveNm",
        },
        // lang: {
        //     supportedHTML5_emptyListMsg: '<div class="text-center" style="padding-top: 30px;">Drop files here or click to upload.</div>',
        //     emptyListMsg: '<div class="text-center" style="padding-top: 30px;">Empty of List.</div>'
        // },
        onchange: function () {

        },
        onclick: function () {

            var fileIndex = this.fileIndex;
            var file = this.uploadedFiles[fileIndex];
            switch (this.cellType) {
                case "delete":
                    DIALOG.confirm({
                        title: "학사관리시스템",
                        msg: "정말 삭제하시겠습니까?"
                    }, function () {
                        if (this.key == "ok") {
                            $.ajax({
                                method: "put",
                                url: "api/v1/image",
                                data: {
                                    uploadedPath: file.uploadedPath,
                                    saveName: file.saveName
                                },
                                success: function (res) {
                                    UPLOAD.removeFile(fileIndex);
                                }
                            });
                        }
                    });
                    break;
                case "download":
                    if (file.download) {
                        location.href = file.download;
                    }
                    break;
            }
        }
    },
    validateSelectedFiles: function () {
        var imgCount = 1;
        // 1개 이상 업로드 되지 않도록 제한.
        if (this.uploadedFiles.length + this.selectedFiles.length > imgCount) {
            alert("You can not upload more than " + imgCount + " files.");
            return false;
        }

        return true;
    },
    onprogress: function () {

    },
    onuploaderror: function () {

        DIALOG.alert(this.error.message);
    },
    onuploaded: function () {
    },
    onuploadComplete: function () {
        var uploadedFiles = this.self.uploadedFiles;

        fnObj.formView01.model.set('photoFileId', uploadedFiles[0].id);
        profileLoad(uploadedFiles[0].id);
    }
});

function profileLoad(profileId) {
    $(".profile").attr("src", "/api/v1/image/preview?id=" +profileId);
}

function profileDefault() {
    $(".profile").attr("src", "/assets/images/blank-profile.png");
}
