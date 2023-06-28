//PHÂN TRANG CỦA REPORT THỐNG KÊ THEO LOẠI
$(document).ready(function () {
	$('#paginationCa').on('click', 'a.page-link', function (e) {
		e.preventDefault(); // Ngăn chặn hành vi mặc định khi nhấp vào liên kết phân trang

		var url = $(this).attr('href');

		$.ajax({
			type: 'GET',
			url: url,
			success: function (response) {
				// Xử lý phản hồi thành công ở đây
				// Ví dụ: Cập nhật bảng dữ liệu trong trang với phản hồi từ máy chủ
				$('#product-table-bodyCa').empty().html($(response).find('#product-table-bodyCa').html());
				$('#paginationCa').empty().html($(response).find('#paginationCa').html());
			},
			error: function (xhr, status, error) {
				// Xử lý lỗi ở đây
			}
		});
	});
});


//PHÂN TRANG THỐNG KÊ THEO THỜI GIAN

//PHÂN TRANG THỐNG KÊ BILL
//   $(document).ready(function() {
// 	$('#paginationBill').on('click', 'a', function(e){
// 	  e.preventDefault();
// 	  var url = $(this).attr("href");
// 	  $.ajax({
// 		type: 'GET',
// 		url: url,
// 		success: function(data){
// 		  console.log(data);
// 		  $('#product-table-bodyBill').empty().html($(data).find('#product-table-bodyBill').html());
// 		  $('#paginationBill').empty().html($(data).find('#paginationBill').html());
// 		}
// 	  });
// 	});
//   });

$(document).ready(function () {
	// Gắn sự kiện click cho các liên kết phân trang
	$('#paginationBill').on('click', 'a', function (e) {
		e.preventDefault();

		// Lấy URL từ liên kết được bấm
		var url = $(this).attr('href');

		// Gửi yêu cầu AJAX
		$.ajax({
			url: url,
			type: 'GET',
			success: function (data) {
				// Cập nhật nội dung bảng dữ liệu
				$('#product-table-bodyBill').html($(data).find('#product-table-bodyBill').html());
				$('#paginationBill').empty().html($(data).find('#paginationBill').html());
			},
			error: function () {
				console.log('Đã xảy ra lỗi trong quá trình tải dữ liệu.');
			}
		});
	});
});

$(document).ready(function () {
	$('#formBill').on('submit', function (e) {
		e.preventDefault(); // Ngăn chặn hành vi mặc định của sự kiện submit

		var form = $(this);
		var url = form.attr('action');
		var formData = form.serialize();

		$.ajax({
			type: 'GET',
			url: url,
			data: formData,
			success: function (response) {
				// Xử lý phản hồi thành công ở đây
				// Ví dụ: Cập nhật bảng dữ liệu trong trang với phản hồi từ máy chủ
				$('#product-table-bodyBill').empty().html($(response).find('#product-table-bodyBill').html());
				$('#formBill').replaceWith($(response).find('#formBill')); // Thay thế nội dung của biểu mẫu
			},
			error: function (xhr, status, error) {
				// Xử lý lỗi ở đây
			}
		});
	});
});

  
//   MONTHLY STATIC
$(document).ready(function() {
	$('#myForm').submit(function(e) {
	  e.preventDefault(); // Chặn hành vi mặc định của biểu mẫu
  
	  var form = $(this);
	  var url = form.attr('action');
	  var formData = form.serialize();
  
	  $.ajax({
		type: 'GET',
		url: url,
		data: formData,
		success: function(response) {
		  // Xử lý phản hồi thành công ở đây
		  // Ví dụ: Cập nhật bảng dữ liệu trong trang với phản hồi từ máy chủ
		  $('#product-table-body1').empty().html($(response).find('#product-table-body1').html());
		  form.trigger('reset'); // Đặt lại biểu mẫu để xóa giá trị đã nhập
  
		  // Hoặc, nếu bạn không muốn đặt lại giá trị của biểu mẫu, bạn có thể xóa dòng trên và bổ sung các dòng sau:
		  // $('#startDate').val('');
		  // $('#endDate').val('');
		},
		error: function(xhr, status, error) {
		  // Xử lý lỗi ở đây
		}
	  });
	});
  });
// $(document).ready(function () {
// 	$('#myForm').submit(function (e) {
// 		e.preventDefault(); // Chặn hành vi mặc định của biểu mẫu

// 		var form = $(this);
// 		var url = form.attr('action');
// 		var formData = form.serialize();

// 		$.ajax({
// 			type: 'GET',
// 			url: url,
// 			data: formData,
// 			success: function (response) {
// 				// Xử lý phản hồi thành công ở đây
// 				// Ví dụ: Cập nhật bảng dữ liệu trong trang với phản hồi từ máy chủ
// 				$('#product-table-body1').empty().html($(response).find('#product-table-body1').html());
// 				form.replaceWith($(response).find('#myForm')); // Thay thế nội dung của biểu mẫu
// 			},
// 			error: function (xhr, status, error) {
// 				// Xử lý lỗi ở đây
// 			}
// 		});
// 	});
// });


$(document).ready(function () {
	$('#example1').DataTable({
		dom: 'Bfrtip',
		buttons: [
			'copy', 'csv', 'excel', 'pdf', 'print'
		],
		searching: false,
		paging: false,
		info: false
	});
  });
  
  
//   REPORT
function printReport(billId) {
	var url = '/admin/print-bill/' + billId;

	// Gửi yêu cầu in hóa đơn
	$.ajax({
		url: url,
		type: 'GET',
		xhrFields: {
			responseType: 'blob' // Thiết lập kiểu dữ liệu trả về là blob (file)
		},
		success: function (data) {
			// Tạo một đường dẫn tạm thời cho file PDF
			var fileURL = URL.createObjectURL(data);

			// Tạo một thẻ a ẩn để tải xuống file PDF
			var a = document.createElement('a');
			a.style.display = 'none';
			a.href = fileURL;
			a.download = 'bill.pdf';
			document.body.appendChild(a);
			a.click();

			// Xóa thẻ a sau khi tải xuống hoàn tất
			document.body.removeChild(a);
		}
	});
}

$(document).ready(function () {
	// Gắn sự kiện click cho các liên kết phân trang
	$('#paginationContainer').on('click', 'a', function (e) {
		e.preventDefault();

		// Lấy URL từ liên kết được bấm
		var url = $(this).attr('href');

		// Gửi yêu cầu AJAX
		$.ajax({
			url: url,
			type: 'GET',
			success: function (data) {
				// Cập nhật nội dung bảng dữ liệu
				$('#tableContainer').html($(data).find('#tableContainer').html());
				$('#paginationContainer').empty().html($(data).find('#paginationContainer').html());
			},
			error: function () {
				console.log('Đã xảy ra lỗi trong quá trình tải dữ liệu.');
			}
		});
	});
});
$(document).ready(function() {
	$('#sortTable').DataTable({
	  dom: 'Bfrtip',
	  buttons: [
		{
		  extend: 'copy',
		  text: '<i class="fas fa-copy"></i> copy',
		  className: 'btn btn-primary'
		},
		{
		  extend: 'csv',
		  text: '<i class="fas fa-file-csv"></i> Xuất CSV',
		  className: 'btn btn-primary'
		},
		{
		  extend: 'excel',
		  text: '<i class="fas fa-file-excel"></i> Xuất Excel',
		  className: 'btn btn-primary'
		},
		{
		  extend: 'pdf',
		  text: '<i class="fas fa-file-pdf"></i> Xuất PDF',
		  className: 'btn btn-primary'
		},
		{
		  extend: 'print',
		  text: '<i class="fas fa-print"></i> In',
		  className: 'btn btn-primary'
		}
	  ],
	  searching: false,
	  paging: false,
	  info: false,
	  ordering: true
	});
  });
  

function showConfirmation(message, event, formElement, formAction) {
	event.preventDefault();
  
	return new Promise(function(resolve) {
	  const confirmationDialog = document.createElement('div');
	  confirmationDialog.className = 'confirmation-dialog';
	  confirmationDialog.innerHTML = `
		<div class="message">${message}</div>
		<div class="timer">10</div>
		<div class="buttons">
		  <button class="confirm"><i class="fas fa-check"></i> Xác nhận</button>
		  <button class="cancel"><i class="fas fa-times"></i> Hủy</button>
		</div>
	  `;
	  document.body.appendChild(confirmationDialog);
  
	  const timerElement = confirmationDialog.querySelector('.timer');
	  const confirmButton = confirmationDialog.querySelector('.confirm');
	  const cancelButton = confirmationDialog.querySelector('.cancel');
  
	  let countdown = 10;
  
	  function closeDialog() {
		document.body.removeChild(confirmationDialog);
	  }
  
	  function updateTimer() {
		timerElement.textContent = countdown;
	  }
  
	  function countdownTimer() {
		countdown--;
		updateTimer();
  
		if (countdown === 0) {
		  closeDialog();
		  resolve(false);
		} else {
		  setTimeout(countdownTimer, 1000);
		}
	  }
  
	  confirmButton.addEventListener('click', function() {
		closeDialog();
		resolve(true);
	  });
  
	  cancelButton.addEventListener('click', function() {
		closeDialog();
		resolve(false);
	  });
  
	  confirmationDialog.addEventListener('click', function(event) {
		if (event.target === confirmationDialog) {
		  closeDialog();
		  resolve(false);
		}
	  });
  
	  countdownTimer();
	}).then(function(confirmed) {
	  if (confirmed) {
		formElement.setAttribute('action', formAction);
		formElement.submit();
	  }
	});
  }
 
