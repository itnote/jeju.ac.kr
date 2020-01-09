var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    EXCEL_UPLOAD: function (caller, act, data) {
        if (check()) {
            var form = $('#excelUploadForm')[0];
            var formData = new FormData(form);
            formData.append("schSemeYear",$("#schSemeYear").val());
            formData.append("schSemeSeq",$("#schSemeSeq").val());

            $.ajax({
                type : "POST",
                enctype: 'multipart/form-data',
                data: formData,
                url: "/api/v1/certificate/excelUpload",
                async: false,
                processData: false,
                contentType: false,
                cache: false,
                success : function(data) {

                    parent.axboot.modal.callback(data["list"]);
                    //fnObj.gridView01.setData(data["list"]);

                    axToast.push("모든 데이터가 업로드 되었습니다.");
                }
            });
        }
    },
    PAGE_CLOSE: function (caller, act, data) {
        if (parent) {
            parent.axboot.modal.close();
        }
    },
    EXCEL_DOWN: function () {
        $.form({
            action: '/api/v1/certificate/tuitionExcelFormDown',
            target: 'excelDownFrm',
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schPeriodCd : $("#schPeriodCd").val()
            }
        }).submit();
    }
});

var CODE = {};

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok) {
            _this.pageButtonView.initView();

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
            "close": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
            },
            "down" : function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_DOWN);
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




