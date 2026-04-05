const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "rowId": "id",
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled",
                    "className": "text-center"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "<a><span class='fa fa-pencil'></span></a>",
                    "orderable": false
                },
                {
                    "defaultContent": "<a class='delete'><span class='fa fa-remove'></span></a>",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );

    $("#datatable").on("change", ".enabled", function () {
        const checkbox = $(this);
        enable(checkbox, checkbox.closest("tr").attr("id"));
    });
});

function enable(checkbox, id) {
    const enabled = checkbox.is(":checked");
    $.ajax({
        url: userAjaxUrl + id + "?enabled=" + enabled,
        type: "PATCH",
    }).done(function () {
        checkbox.closest("tr").attr("data-user-enabled", enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        checkbox.prop("checked", !enabled);
    });
}
