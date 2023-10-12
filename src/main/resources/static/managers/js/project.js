$(document).ready(function () {
    projectInitEvent()
});

function projectInitEvent() {
    let selectedRowId;
    // TODO: chọn bản ghi bằng click
    $("#project #row").click(function () {
        selectedRowId = $(this).data("uuid")

        $("#project #row").removeClass("selected")
        $(this).addClass("selected")

    })

    // TODO: hiển thị thông tin bản ghi muốn sửa
    $("#updateProject").click(function () {
        if (!selectedRowId) alert("Please choose record to edit!")
        else {
            // fill form project
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/manager/projects/" + selectedRowId,
                success: function (project) {
                    //TODO: lấy dữ liệu project ở database
                    const inputElements = $("#update-project-modal input,#update-project-modal select");
                    for (const input of inputElements) {
                        //vẽ thông tin lên form
                        const propVal = input["id"];
                        const projectProperty = propVal.replace("update-", "");
                        input.value = project[projectProperty];
                    }
                },
            });
            $("#update-project-modal").modal("show")
        }
    })

    //hiển thị thông tin modal để xóa dữ liệu
    $("#deleteProject").click(function () {
        if (!selectedRowId) {
            $("#deleteProjectBtn").hide()
            $("#deleteModal .modal-body").text("Please choose record to delete!")
        } else {
            $("#deleteModal .modal-body").text(" Do you want to delete this record?")
            $("#deleteProjectBtn").show()
        }
    })

    // TODO: cập nhật thông tin vừa sửa
    $("#update-submit").click(function () {
        const project = {
            "name": $("#update-name").val(),
            "endDate": $("#update-endDate").val(),
            "startDate": $("#update-startDate").val(),
            "status": $("#update-status").val(),
        }

        $.ajax({
            type: "PUT",
            url: "http://localhost:8080/manager/projects/" + selectedRowId,
            data: JSON.stringify(project),
            dataType: "json",
            async: false,
            contentType: "application/json",
            success: function (response) {
                location.reload();
                localStorage.setItem('flashMessage', 'Edit complete');
                console.log(response)
            },
        });

        $("#update-project-modal").modal("hide")
    })

    //Xóa dữ liệu
    $("#deleteProjectBtn").click(function (){
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8080/manager/projects/" + selectedRowId,
            success: function (project) {
                location.reload();
                localStorage.setItem('flashMessage', 'Delete complete!');
                // console.log(project)
            }
    });
    })
}