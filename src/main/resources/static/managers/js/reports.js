$(document).ready(function () {
    // flash msg

// Kiểm tra xem có flash message trong localStorage không
    var flashMessage = localStorage.getItem('flashMessage');
    if (flashMessage) {
        $("#alert").text(flashMessage).show().delay(3000).fadeOut();
        // Xóa flash message khỏi localStorage sau khi đã hiển thị
        localStorage.removeItem('flashMessage');
    }
    // Set input date field is current date
// Lấy ngày hôm nay
    let today = new Date();

// Định dạng ngày tháng
    let year = today.getFullYear();
    let month = String(today.getMonth() + 1).padStart(2, '0');
    let day = String(today.getDate()).padStart(2, '0');

// Tạo chuỗi ngày tháng để sử dụng làm giá trị placeholder
    let placeholderValue = year + '-' + month + '-' + day;

// Đặt giá trị placeholder cho thẻ input
    document.getElementById('dateInput').setAttribute('value', placeholderValue);


    $(".showModalBtn").click(function () {

        const id = $(this).data("id")
        // Gửi yêu cầu AJAX để lấy thông tin chi tiết với ID
        $.ajax({
            url: `/manager/reports/${id}`, method: "GET", // data: { id: id },
            success: function (report) {
                // Xử lý dữ liệu trả về từ yêu cầu AJAX
                console.log(report)
                const byUser = report.createdByUser;
                const toUser = report.user;
                const project = report.project;

                // Hiển thị thông tin chi tiết trong modal
                $(".modal-title-date").text(report.reportDate)
                $("#reportModal .created-by").text(byUser.name);
                $("#reportModal .to-user").text(toUser.name)
                $("#reportModal .project-name").text(project.name)
                // append thì phải clear
                $("#reportModal .actual-work").empty()
                $("#reportModal .reason-cannot-complete").empty()
                $("#reportModal .tomorrow-plan").empty()
                $("#reportModal .working-time").empty()
                //append
                $("#reportModal .actual-work").append(report.actualWork.replaceAll("+", "<br>-"))
                $("#reportModal .reason-cannot-complete").append(report.reasonCannotCompleteWork.replaceAll("+", "<br>-"))
                $("#reportModal .tomorrow-plan").append(report.tomorrowPlan.replaceAll("+", "<br>-"))
                $("#reportModal .working-time").append(report.workingTime.replaceAll("+", "<br>-"))


                // Kích hoạt modal
                // $("#myModal").modal("show");
            }, error: function (e) {
                // Xử lý lỗi nếu có => alert
                $(".alert-danger").text(": " + e.responseText).show();
                setTimeout(function () {
                    $(".alert-danger").hide();
                }, 3000);
            }
        });
    });
});
