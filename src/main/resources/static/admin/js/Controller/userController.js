app.controller("userController", function ($scope, $http, urlAccount) {
  let host = urlAccount;
  $scope.form = {};
  $scope.items = {};
  $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa
  $scope.load_all = function(){
        var url = `${host}`;
        $http.get(url).then(resp => {
        	$scope.items = resp.data;

            $scope.currentPage = 0; // Trang hiện tại
            $scope.pageSize = 5; // Số mục trên mỗi trang
            $scope.totalItems  = resp.data.length;
            $scope.totalPages = Math.ceil($scope.totalItems / $scope.pageSize); // Tổng số trang

            // Xử lý phân trang
            $scope.loadPage = function () {
                $http.get(host + "/page", {
                    params: {
                        page: $scope.currentPage,
                        size: $scope.pageSize
                    }
                }).then(function (response) {
                    $scope.items = response.data.content;
                });
            };

            $scope.nextPage = function () {
                if ($scope.currentPage < $scope.totalPages - 1) {
                    $scope.currentPage++;
                    $scope.updatePageNumbers();
                }
            };

            $scope.prevPage = function () {
                if ($scope.currentPage > 0) {
                    $scope.currentPage--;
                    $scope.updatePageNumbers();
                }
            };

            $scope.updatePageNumbers = function () {
                $scope.pageNumbers = [];

                var startPage = Math.max(0, $scope.currentPage - 2);
                var endPage = Math.min($scope.totalPages - 1, $scope.currentPage + 2);

                for (var i = startPage; i <= endPage; i++) {
                    $scope.pageNumbers.push(i + 1);
                }

                // Kiểm tra nếu cần hiển thị dấu ba chấm ở đầu hoặc cuối
                if (startPage > 0) {
                    $scope.pageNumbers.unshift('...');
                }
                if (endPage < $scope.totalPages - 1) {
                    $scope.pageNumbers.push('...');
                }
                $scope.loadPage();
            };

            $scope.goToPage = function (pageNumber) {
                if (pageNumber === '...') {
                    return;
                }
                $scope.currentPage = pageNumber - 1;
                $scope.updatePageNumbers();
            };

            // Gọi hàm loadPage() khi controller được khởi tạo
            $scope.loadPage();
            //hiển thị các số phân trang
            $scope.updatePageNumbers();
            
        }).catch(Error =>{
            console.log("Error", Error);
        })
    };
  $scope.Edit = function (key, index) {
    var url = `${host}/${key}`;
    $http
      .get(url)
      .then((resp) => {
        $scope.form = resp.data;
        $scope.key = key;
        $scope.selectedItemIndex = index; // Lưu chỉ số sản phẩm đang được chỉnh sửa
        displayImages(resp.data.image); // Hiển thị ảnh tương ứng cho sản phẩm đang chỉnh sửa

        $scope.form.createDate = formatDateToISOString($scope.form.createDate);
      })
      .catch((Error) => {
        console.log("Error", Error);
      });
  };
  $scope.Update = function (key) {
    var formData = new FormData();
    formData.append("image", document.getElementById("fileInput33").files[0]);
    formData.append("account", JSON.stringify({
      password: $scope.form.password,
      fullName: $scope.form.fullName,
      email: $scope.form.email,
      phone: $scope.form.phone,
      gender: $scope.form.gender,
      address: $scope.form.address,
      image: $scope.form.image,
      role: $scope.form.role,
      createDate: $scope.form.createDate
    }));
   
    var url = `${host}/${key}`;
    $http
      .put(url,formData, {
        headers: { 'Content-Type': undefined }
    }).then((resp) => {
        $scope.items[$scope.key] = resp.data;
        $scope.load_all();
        Swal.fire({
          icon: "success",
          title: "Thành công",
          text: `Cập nhật ${key}`,
        });
      })
      .catch((Error) => {
        Swal.fire({
          icon: "error",
          title: "Thất bại",
          text: `Cập nhật ${key} thất bại`,
        });
      });
  };

  $scope.Create = function () {
    $scope.errorMessages = {};
    var files = document.getElementById("fileInputCreateUser").files;

    var item = {
      username: $scope.username || "",
      password: $scope.password || "",
      fullName: $scope.fullName || "",
      email: $scope.email || "",
      phone: $scope.phone || "",
      gender: $scope.gender || "",
      address: $scope.address || "",
      image: files[0].name || '',
      role: $scope.form.role || "",
      createDate: new Date(),
    };

    var url = `${host}`;
    $http.post(url,item)
      .then((resp) => {
        console.log("Success", resp);
        $scope.load_all();
        Swal.fire({
          icon: "success",
          title: "Thành công",
          text: `Đã thêm tài khoản ` + item.username,
        });
      })
      .catch((Error) => {
        console.log(Error.data);
        if (Error.data) {
          $scope.errorMessages = Error.data;
          if (Error.data.AccountExists) {
            Swal.fire({
              icon: "info",
              title: "Thông tin",
              text: $scope.errorMessages.AccountExists,
            });
          } else if (Error.data.emailExists) {
            Swal.fire({
              icon: "info",
              title: "Thông tin",
              text: $scope.errorMessages.emailExists,
            });
          } else if (Error.data.phoneExists) {
            Swal.fire({
              icon: "info",
              title: "Thông tin",
              text: $scope.errorMessages.phoneExists,
            });
          }
        }
      });
  };
  $scope.Delete = function (key) {
    var url = `${host}/${key}`;

    Swal.fire({
      title: "Bạn chắc chắn?",
      text: "Dữ liệu sẽ bị xóa vĩnh viễn.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Xác nhận",
      cancelButtonText: "Hủy",
    }).then((result) => {
      if (result.isConfirmed) {
        $http
          .delete(url)
          .then((resp) => {
            $scope.load_all();
            Swal.fire({
              icon: "success",
              title: "Thành công",
              text: `Đã xóa tài khoản ${key}`,
            });
          })
          .catch((Error) => {
            Swal.fire({
              icon: "error",
              title: "Thất bại",
              text: `Xóa tài khoản ${key} thất bại`,
            });
          });
      }
    });
  };

  $scope.searchData = function () {
    if ($scope.searchTerm.trim() !== "") {
      var url = `${host}/search?username=${encodeURIComponent(
        $scope.searchTerm
      )}`; // Sử dụng encodeURIComponent để mã hóa ký tự đặc biệt trong URL
      $http
        .get(url)
        .then((resp) => {
          $scope.items = [resp.data]; // Đảm bảo dữ liệu trả về từ server được đưa vào mảng
          console.log("Success", $scope.items);
        })
        .catch((error) => {
          console.log("Error", error);
        });
    } else {
      // Nếu người dùng không nhập gì hoặc chỉ nhập khoảng trắng, reset kết quả tìm kiếm
      $scope.items = [];
    }
  };

  $scope.load_all();
});

function formatDateToISOString(dateString) {
  var date = new Date(dateString);
  var year = date.getFullYear();
  var month = (date.getMonth() + 1).toString().padStart(2, "0");
  var day = date.getDate().toString().padStart(2, "0");
  var hours = date.getHours().toString().padStart(2, "0");
  var minutes = date.getMinutes().toString().padStart(2, "0");
  return `${year}-${month}-${day}T${hours}:${minutes}`;
}

function displayImages(event) {
  var input = event.target;
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function (e) {
      var imageContainer = document.getElementById("uploadedImages");
      imageContainer.src = e.target.result;
      imageContainer.style.display = "block";
    };
    reader.readAsDataURL(input.files[0]);
  }
}
