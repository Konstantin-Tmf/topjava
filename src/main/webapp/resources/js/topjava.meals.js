const mealAjaxUrl = "profile/meals/";

function formatDateTime(dateTime) {
    return dateTime.replace("T", " ").substring(0, 16);
}

function convertDateTime(data) {
    if (Array.isArray(data)) {
        data.forEach(convertDateTime);
    } else if (data && data.dateTime) {
        data.dateTime = formatDateTime(data.dateTime);
    }
    return data;
}

function getMaxStartTime(endTimeValue) {
    const parts = endTimeValue.split(":");
    if (parts.length < 2) {
        return false;
    }
    const hours = Number(parts[0]);
    const minutes = Number(parts[1]);
    const totalMinutes = hours * 60 + minutes;
    if (!Number.isInteger(hours) || !Number.isInteger(minutes) ||
        hours < 0 || hours > 23 || minutes < 0 || minutes > 59 ||
        totalMinutes === 23 * 60 + 59) {
        return false;
    }
    const maxStartMinutes = totalMinutes + 1;
    const maxStartHours = Math.floor(maxStartMinutes / 60);
    const maxStartMinute = maxStartMinutes % 60;
    return `${String(maxStartHours).padStart(2, "0")}:${String(maxStartMinute).padStart(2, "0")}`;
}

function initDatetimePickers() {
    $.datetimepicker.setLocale(i18n["datetimepickerLocale"]);
    const startDate = $("#startDate");
    const endDate = $("#endDate");
    const startTime = $("#startTime");
    const endTime = $("#endTime");

    startDate.datetimepicker({
        timepicker: false,
        format: "Y-m-d",
        onShow: function () {
            this.setOptions({
                maxDate: endDate.val() || false
            });
        },
        onChangeDateTime: function () {
            endDate.datetimepicker("setOptions", {
                minDate: startDate.val() || false
            });
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        format: "Y-m-d",
        onShow: function () {
            this.setOptions({
                minDate: startDate.val() || false
            });
        },
        onChangeDateTime: function () {
            startDate.datetimepicker("setOptions", {
                maxDate: endDate.val() || false
            });
        }
    });
    startTime.datetimepicker({
        datepicker: false,
        format: "H:i",
        onShow: function () {
            this.setOptions({
                maxTime: getMaxStartTime(endTime.val())
            });
        },
        onChangeDateTime: function () {
            endTime.datetimepicker("setOptions", {
                minTime: startTime.val() || false
            });
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        format: "H:i",
        onShow: function () {
            this.setOptions({
                minTime: startTime.val() || false
            });
        },
        onChangeDateTime: function () {
            startTime.datetimepicker("setOptions", {
                maxTime: getMaxStartTime(endTime.val())
            });
        }
    });
    $("#dateTime").datetimepicker({
        format: "Y-m-d H:i"
    });
}

function initAjaxDateTimeConverter() {
    $.ajaxSetup({
        converters: {
            "text json": function (json) {
                if (!json) {
                    return null;
                }
                return convertDateTime(JSON.parse(json));
            }
        }
    });
}

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    initDatetimePickers();
    initAjaxDateTimeConverter();

    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return formatDateTime(date);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", String(data.excess));
            }
        })
    );
});
