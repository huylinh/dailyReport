$(document).ready(function () {
    jobInitEvent()
});

let selectedRow;
function jobInitEvent() {

    //Todo: load danh sach job
    let processId
    //TODO: load job của một process của project
    $(".viewJobsBtn").click(function () {
        processId = $(this).data("id")
        loadJob(processId)
    })

    $("#addJobBtn").click(function (){
        const jobContent = $("#jobContent")
        const projectJob = {
            content: jobContent.val()
        }
        $.ajax({
            type: "POST",
            url: `http://localhost:8080/manager/project-processes/${processId}/project-jobs`,
            data: JSON.stringify(projectJob),
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
                $("#alert").text("Add job complete").show().delay(3000).fadeOut();
                loadJob(processId)
                jobContent.val("")
            },
        });
    })
        // TODO: Delete job


    $("#jobDeleteModal .btn-primary").click(function (){
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8080/manager/project-jobs/" + selectedRow,
            success: function (res) {
                loadJob(processId)
                $("#alert").text("Delete complete!!").show().delay(3000).fadeOut();
            }
        });
        $("#jobDeleteModal").modal('hide')
    })

    $("#jobUpdateModal .btn-primary").click(function (){
        const projectJob = {
            content: $("#updateJobContent").val()
        }
        $.ajax({
            type: "PUT",
            url: "http://localhost:8080/manager/project-jobs/" + selectedRow,
            data: JSON.stringify(projectJob),
            dataType: "json",
            contentType: "application/json",
            success: function (res) {
                loadJob(processId)
                $("#alert").text("Update complete!!").show().delay(3000).fadeOut();
                $("#jobUpdateModal").modal('hide')
            }
        });


    })

}


function loadJob(processId) {
    const jobsTableBody = $("#jobTableRows")
    jobsTableBody.empty()
    // ajax to load job list
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/manager/project-processes/${processId}/project-jobs`,
        success: function (jobs) {
            if (jobs.length) {
                jobs.forEach((job,index) =>{
                    const rowHtml = `<tr><th scope="row" class="text-center">${index + 1}</th>
                    <td class="text-center col-8">${job.content}</td> 
                    <td class="text-center"> 
                    <button type="button" class="btn btn-warning" data-id=${job.id}>Update</button>
                    </td> 
                    <td class="text-center"> 
                    <button type="submit" class="btn btn-danger" data-id=${job.id}>Delete</button>
                    </td></tr>`
                    jobsTableBody.append(rowHtml)
                    // set up event after insert tag to DOM
                })
                $("#jobTableRows .btn-danger").click(function (){
                    $("#jobDeleteModal").modal('show')
                    selectedRow = $(this).data("id")
                })
                $("#jobTableRows .btn-warning").click(function (){
                    selectedRow = $(this).data("id")
                    //fill form
                    $.ajax({
                        type: "GET",
                        url: "http://localhost:8080/manager/project-jobs/" + selectedRow,
                        success: function (projectJob) {
                            console.log(projectJob)
                            $("#updateJobContent").val(projectJob.content)
                        },
                    });
                    $("#jobUpdateModal").modal('show')
                })
            } else {
                const taskRowHtml = '<tr><td colspan="4" class="text-center">Empty</td></tr>'
                jobsTableBody.append(taskRowHtml)
            }
            $("#processJobs").modal('show')
        },
    });
}

