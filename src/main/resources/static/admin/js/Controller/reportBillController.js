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

    $scope.printInvoice = function (invoice) {
        var printWindow = window.open('', '_blank');
    
        var content = `
            <!DOCTYPE html>
            <html>
            <head>
                <title>Hóa đơn</title>
                <!-- Thêm liên kết đến tệp CSS của Bootstrap -->
                <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f5f5f5;
                    }
                    .invoice-container {
                        margin: 0 auto;
                        border: 1px solid #ccc;
                        padding: 20px;
                        background-color: white;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        flex-wrap: wrap;
                        max-width: 500px; /* Điều chỉnh độ rộng tối đa ở đây */
                    }                    
                    .invoice-header {
                        text-align: center;
                        width: 100%;
                        margin-bottom: 20px;
                    }
                    .invoice-details {
                        flex: 1;
                        padding: 0 15px;
                    }
                    h2 {
                        margin-bottom: 20px;
                    }
                    p {
                        margin: 10px 0;
                    }
                </style>
            </head>
            <body>
                <div class="container invoice-container">
                    <div class="invoice-header">
                        <h2>Hóa đơn chi tiết</h2>
                    </div>
                    <div class="invoice-details">
                        <p><strong>Họ và tên:</strong> ${invoice.account.fullName}</p>
                        <p><strong>Ngày thanh toán:</strong> ${$scope.formatDate(invoice.createDate)}</p>
                        <p><strong>Tiền sản phẩm:</strong> ${$scope.formatCurrency(invoice.amount)}</p>
                        <p>------------------------------------------------------------------------</p>
                        <p><strong>Giảm giá:</strong> ${invoice.discountPercent || 0}%</p>
                        <p>------------------------------------------------------------------------</p>
                        <p><strong>Thành tiền:</strong> ${$scope.formatCurrency(invoice.newAmount)}</p>
                    </div>
                </div>
    
                <!-- Thêm liên kết đến tệp JavaScript của Bootstrap (tuỳ chọn) -->
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            </body>
            </html>
        `;
        printWindow.document.write(content);
        printWindow.print();
    };
    
    
    
    $scope.formatDate = function (dateString) {
        var date = new Date(dateString);
        var day = date.getDate().toString().padStart(2, '0');
        var month = (date.getMonth() + 1).toString().padStart(2, '0');
        var year = date.getFullYear();
        // var hours = date.getHours().toString().padStart(2, '0');
        // var minutes = date.getMinutes().toString().padStart(2, '0');
        // var seconds = date.getSeconds().toString().padStart(2, '0');
        return `${day}/${month}/${year}`;
    };

    $scope.formatCurrency = function(amount) {
        // Thực hiện chuyển đổi số thành chuỗi định dạng tiền tệ
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    };
    $scope.load_all();
})

