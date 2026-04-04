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
            "createdRow": function (row, data) {
                $(row).attr("data-user-enabled", resolveEnabledValue(data.enabled));
            },
            "columns": [
                {
                    "data": "name",
                    "render": function (data, type) {
                        return type === "display" ? renderText(data) : data;
                    }
                },
                {
                    "data": "email",
                    "render": function (data, type) {
                        if (type !== "display") {
                            return data;
                        }
                        if (isHtmlValue(data)) {
                            return data;
                        }
                        const email = renderText(data);
                        return "<a href='mailto:" + email + "'>" + email + "</a>";
                    }
                },
                {
                    "data": "roles",
                    "render": function (data, type) {
                        const roles = Array.isArray(data) ? "[" + data.join(", ") + "]" : data;
                        return type === "display" ? renderText(roles) : roles;
                    }
                },
                {
                    "data": "enabled",
                    "className": "text-center",
                    "render": function (data, type) {
                        if (type !== "display") {
                            return data;
                        }
                        if (isHtmlValue(data)) {
                            return data;
                        }
                        return "<input type='checkbox' class='enabled'" + (resolveEnabledValue(data) ? " checked" : "") + "/>";
                    }
                },
                {
                    "data": "registered",
                    "render": function (data, type) {
                        if (type !== "display") {
                            return data;
                        }
                        if (isHtmlValue(data)) {
                            return data;
                        }
                        return formatDate(data);
                    }
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
        updateTable();
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        checkbox.prop("checked", !enabled);
    });
}

function formatDate(value) {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
        return renderText(value);
    }
    return date.toLocaleDateString(undefined, {
        day: "2-digit",
        month: "long",
        year: "numeric"
    });
}

function renderText(value) {
    return $("<div>").text(value == null ? "" : value).html();
}

function isHtmlValue(value) {
    return typeof value === "string" && value.indexOf("<") !== -1;
}

function resolveEnabledValue(value) {
    if (typeof value === "boolean") {
        return value;
    }
    if (typeof value === "string") {
        return isHtmlValue(value) ? value.indexOf("checked") !== -1 : value === "true";
    }
    return Boolean(value);
}
