const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "rowId": "id",
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "<a class='delete'><span class='fa fa-remove'></span></a>",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );

    $("#filterForm").on("submit", function (event) {
        event.preventDefault();
        syncFilterUrl();
        updateTable();
    });

    $("#resetFilter").on("click", function () {
        $("#filterForm").find(":input").val("");
        ctx.datatableApi.search("");
        syncFilterUrl();
        updateTable();
    });
});

function add() {
    const mealForm = $('#detailsForm');
    mealForm.find(":input").val("");
    mealForm.find("#dateTime").val(getCurrentDateTime());
    mealForm.find("#calories").val("1000");
    $("#editRow").modal();
}

function updateTable() {
    $.get(mealAjaxUrl, $("#filterForm").serialize(), function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function syncFilterUrl() {
    const query = $("#filterForm").serialize();
    const url = query ? window.location.pathname + "?" + query : window.location.pathname;
    history.replaceState(null, "", url);
}

function getCurrentDateTime() {
    const now = new Date();
    now.setSeconds(0, 0);
    return new Date(now.getTime() - now.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
}
