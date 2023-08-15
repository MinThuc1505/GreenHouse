app.controller('discountController', function ($scope, $http, urlDiscount) {
    let host = urlDiscount;
    $scope.form = {};
    $scope.items = {};
    $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa
 

    $scope.load_all = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {

            // biến phân trang
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

        }).catch(Error => {
            console.log("Error", Error);
        })
    }


    $scope.Edit = function (key,index) {
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
            $scope.form = resp.data;
            $scope.key = key;
            $scope.selectedItemIndex = index; // Lưu chỉ số sản phẩm đang được chỉnh sửa
   
            $scope.form.startDate = formatDateToISOString($scope.form.startDate);
            $scope.form.endDate = formatDateToISOString($scope.form.endDate);
            $scope.form.status = ($scope.form.status === "1");
        }).catch(Error => {
            console.log("Error", Error);
        })
    }
    $scope.Update = function (key) {

        var item = {
            discountCode: $scope.form.discountCode,
            startDate: $scope.form.startDate,
            quantity: $scope.form.quantity,
            endDate: $scope.form.endDate,
            discountPercent: $scope.form.discountPercent,
            status: $scope.form.status
        };
        var url = `${host}/${key}`;
        $http.put(url, item).then(resp => {
            $scope.items[$scope.key] = resp.data;
            $scope.loadPage();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Cập nhật mã ${key}`,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Cập nhật mã ${key} thất bại`,
            });
        })
    }
    $scope.Create = function () {
        var item = {
            discountCode: $scope.discountCode,
            startDate: $scope.startDate,
            quantity: $scope.quantity,
            endDate: $scope.endDate,
            discountPercent: $scope.discountPercent,
            status: $scope.status
        };


        var url = `${host}`;
        $http.post(url, item).then(resp => {
            console.log("Success", resp);
            $scope.loadPage();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đã thêm mã ` + item.discountCode,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Thêm mã ` + item.discountCode + ` thất bại `,
            });
        })
    }
    $scope.Delete = function (key) {
        var url = `${host}/${key}`;

        Swal.fire({
            title: 'Bạn chắc chắn?',
            text: 'Dữ liệu sẽ bị xóa vĩnh viễn.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy',
        }).then((result) => {
            if (result.isConfirmed) {
                $http.delete(url).then(resp => {
                    $scope.loadPage();
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: `Đã xóa mã ${key}`,
                    });
                }).catch(Error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Thất bại',
                        text: `Xóa mã ${key} thất bại`,
                    });
                })
            }
        });
    }

    $scope.load_all();
})

function formatDateToISOString(dateString) {
    var date = new Date(dateString);
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    var hours = date.getHours().toString().padStart(2, '0');
    var minutes = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}