const mealAjaxUrl = "profile/meals/";
let lastDateFilterInput;
let lastTimeFilterInput;

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

function parseFilterDate(value) {
    const parts = value.split("-");
    if (parts.length !== 3) {
        return null;
    }
    const year = Number(parts[0]);
    const month = Number(parts[1]);
    const day = Number(parts[2]);
    return year && month && day ? new Date(year, month - 1, day).getTime() : null;
}

function parseFilterTime(value) {
    const parts = value.split(":");
    if (parts.length < 2) {
        return null;
    }
    const hours = Number(parts[0]);
    const minutes = Number(parts[1]);
    return hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60 ? hours * 60 + minutes : null;
}

function fixEndBeforeStart(startInput, endInput, startChanged, parseValue) {
    const startValue = parseValue(startInput.val());
    const endValue = parseValue(endInput.val());
    if (startValue !== null && endValue !== null && endValue < startValue) {
        const changedInput = startChanged ? startInput : endInput;
        const pairedInput = startChanged ? endInput : startInput;
        pairedInput.val(changedInput.val());
    }
}

function initDatetimePickers() {
    $.datetimepicker.setLocale(i18n["datetimepickerLocale"]);
    const startDate = $("#startDate");
    const endDate = $("#endDate");
    const startTime = $("#startTime");
    const endTime = $("#endTime");

    startDate.add(endDate).on("focus input change", function () {
        lastDateFilterInput = this.id;
    });
    startTime.add(endTime).on("focus input change", function () {
        lastTimeFilterInput = this.id;
    });

    startDate.datetimepicker({
        timepicker: false,
        format: "Y-m-d"
    });
    endDate.datetimepicker({
        timepicker: false,
        format: "Y-m-d"
    });
    startTime.datetimepicker({
        datepicker: false,
        format: "H:i"
    });
    endTime.datetimepicker({
        datepicker: false,
        format: "H:i"
    });
    $("#dateTime").datetimepicker({
        format: "Y-m-d H:i"
    });
}

function fixFilterRanges() {
    fixEndBeforeStart($("#startDate"), $("#endDate"), lastDateFilterInput !== "endDate", parseFilterDate);
    fixEndBeforeStart($("#startTime"), $("#endTime"), lastTimeFilterInput !== "endTime", parseFilterTime);
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
        fixFilterRanges();
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    lastDateFilterInput = undefined;
    lastTimeFilterInput = undefined;
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
