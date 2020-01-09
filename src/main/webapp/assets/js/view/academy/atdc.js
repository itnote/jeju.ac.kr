var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    ATDC_UPLOAD: function (caller, act, data) {
        var form = $('#excelUploadForm')[0];
        var formData = new FormData(form);

        $.ajax({
            type : "POST",
            enctype: 'multipart/form-data',
            data: formData,
            url: "/api/v1/attendancemst/atdcUpload",
            async: false,
            processData: false,
            contentType: false,
            cache: false,
            success : function(data) {
                axToast.push("모든 데이터가 업로드 되었습니다.");
            }
        });
    },
    SCORE_UPLOAD: function (caller, act, data) {
        var form = $('#excelUploadForm')[0];
        var formData = new FormData(form);

        console.log(formData);

        $.ajax({
            type : "POST",
            enctype: 'multipart/form-data',
            data: formData,
            url: "/api/v1/scoremst/scoreUpload",
            async: false,
            processData: false,
            contentType: false,
            cache: false,
            success : function(data) {
                axToast.push("모든 데이터가 업로드 되었습니다.");
            }
        });
    }
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    this.pageButtonView.initView();
};


fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "atdc": function () {
                ACTIONS.dispatch(ACTIONS.ATDC_UPLOAD)
            },
            "score": function () {
                ACTIONS.dispatch(ACTIONS.SCORE_UPLOAD)
            }
        });
    }
});
