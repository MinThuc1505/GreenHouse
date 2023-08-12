app.controller('reportBillController', function ($scope, $http, urlReportBill) {
    let host = urlReportBill;
    $scope.form = {};
    $scope.items = {};
    $scope.load_all = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {

            // biến phân trang
            $scope.currentPage = 0; // Trang hiện tại
            $scope.pageSize = 5; // Số mục trên mỗi trang
            $scope.totalItems = resp.data.length;
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
    $scope.formatDate = function (dateString) {
        var date = new Date(dateString);
        var day = date.getDate().toString().padStart(2, '0');
        var month = (date.getMonth() + 1).toString().padStart(2, '0');
        var year = date.getFullYear();
        var hours = date.getHours().toString().padStart(2, '0');
        var minutes = date.getMinutes().toString().padStart(2, '0');
        var seconds = date.getSeconds().toString().padStart(2, '0');
        return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    };

    $scope.load_all();
})

