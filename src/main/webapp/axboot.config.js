(function () {
    if (axboot && axboot.def) {

        axboot.def["DEFAULT_TAB_LIST"] = [
            {menuId: "00-dashboard", id: "dashboard", progNm: '홈', menuNm: '홈', progPh: '/jsp/dashboard.jsp', url: '/jsp/dashboard.jsp?progCd=dashboard', status: "on", fixed: true}
        ];

        axboot.def["API"] = {
            "users": "/api/v1/users",
            "commonCodes": "/api/v1/commonCodes",
            "programs": "/api/v1/programs",
            "menu": "/api/v2/menu",
            "manual": "/api/v1/manual",
            "errorLogs": "/api/v1/errorLogs",
            "files": "/api/v1/files",
            "samples": "/api/v1/samples",
            "students": "/api/v1/jnustudent"
        };

        axboot.def["MODAL"] = {
            "ZIPCODE": {
                width: 500,
                height: 500,
                iframe: {
                    url: "/jsp/common/zipcode.jsp"
                }
            },
            "SAMPLE-MODAL": {
                width: 500,
                height: 500,
                iframe: {
                    url: "/jsp/_samples/modal.jsp"
                },
                header: false
            },
            "COMMON_CODE_MODAL": {
                width: 600,
                height: 400,
                iframe: {
                    url: "/jsp/system/system-config-common-code-modal.jsp"
                },
                header: false
            },
            "TEST-MODAL": {
                width: 500,
                height: 500,
                iframe: {
                    url: "/jsp/_samples/test-modal.jsp"
                },
                header: false
            },
            "SCHE-MODAL": {
                width: 300,
                height: 320,
                iframe: {
                    url: "/jsp/academy/scheModal.jsp"
                },
                header: false
            },
            "TCHR-MODAL": {
                width: 600,
                height: 500,
                iframe: {
                    url: "/jsp/academy/tchrModal.jsp"
                },
                header: false
            },
            "STDT-MODAL": {
                width: 800,
                    height: 500,
                    iframe: {
                    url: "/jsp/academy/stdtModal.jsp"
                },
                header: false
            },
            "CLAS-MODAL": {
                width: 1600,
                height: 600,
                iframe: {
                    url: "/jsp/academy/clasModal.jsp"
                },
                header: false
            },
            "EXCEL-MODAL": {
                width: 1000,
                height: 500,
                iframe: {
                    url: "/jsp/academy/excelModal.jsp"
                },
                header: false
            },
            "EXCEL-UPLOAD": {
                width: 600,
                height: 200,
                iframe: {
                    url: "/jsp/academy/excelUpload.jsp"
                },
                header: false
            },
            "CHANGE_PASSWORD_MODAL": {
                width: 400,
                height: 210,
                iframe: {
                    url: "/jsp/academy/changePwModal.jsp"
                },
                header: false
            }
        };
    }


    var preDefineUrls = {
        "manual_downloadForm": "/api/v1/manual/excel/downloadForm",
        "manual_viewer": "/jsp/system/system-help-manual-view.jsp"
    };
    axboot.getURL = function (url) {
        if (ax5.util.isArray(url)) {
            if (url[0] in preDefineUrls) {
                url[0] = preDefineUrls[url[0]];
            }
            return url.join('/');

        } else {
            return url;
        }
    }

    ;jQuery(function($) { $.extend({
        form: function( options ) {
            var defaults = {
                method: 'post',
                target: '_self',
                data: {}
            }
            var settings = $.extend( {}, defaults, options );

            var form = $('<form>').attr({
                method: settings.method,
                target: settings.target,
                action: settings.action
            }).css({
                display: 'none'
            });

            // var addData = function(name, data) {
            //     if ($.isArray(data)) {
            //         for (var i = 0; i < data.length; i++) {
            //             addData(name + '[]', data[i]);
            //         }
            //         return;
            //     }
            //     if (typeof data === 'object') {
            //         for (var key in data) {
            //             if (data.hasOwnProperty(key)) {
            //                 addData(name + '[' + key + ']', data[key]);
            //             }
            //         }
            //         return;
            //     }
            //     if (data != null) {
            //         form.append($('<input>').attr({
            //             type: 'hidden',
            //             name: String(name),
            //             value: String(data)
            //         }));
            //     }
            // };

            // for (var key in settings.data) {
            //     if (settings.data.hasOwnProperty(key)) {
            //         addData("data", settings.data[key]);
            //     }
            // }

            form.append($('<input>').attr({
                type: 'hidden',
                name: 'data',
                value: JSON.stringify(settings.data)
            }));

            form.append($('<input>').attr({
                type: 'hidden',
                name: 'schForm',
                value: JSON.stringify(settings.schForm)
            }));

            return form.appendTo('body');
        }

    }); });

})();