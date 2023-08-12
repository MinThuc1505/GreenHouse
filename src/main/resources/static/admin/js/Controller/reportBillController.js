app.controller('reportBillController', function ($scope, $http, urlReportBill) {
    let host = urlReportBill;
    $scope.form = {};
    $scope.items = {};
    $scope.load_all = function(){
        var url = `${host}`;
        $http.get(url).then(resp => {
        	$scope.items = resp.data;
            console.log($scope.items);
        }).catch(Error =>{
            console.log(Error);
        })
    }

    $scope.printInvoice = function(invoice) {
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
                }
                .invoice-container {
                    margin: 0 auto;
                    border: 1px solid #ccc;
                    padding: 20px;
                }
                .left-column {
                    text-align: left;
                }
                h2 {
                    margin-bottom: 10px;
                }
                p {
                    margin: 5px 0;
                }
            </style>
        </head>
        <body>
            <div class="container invoice-container">
                <h2>Hóa đơn chi tiết</h2>
                <div class="row">
                    <div class="col-md-6 left-column">
                        <p>Họ và tên: ${invoice.account.fullName}</p>
                        <p>Giảm giá: ${ invoice.discountPercent || 0}</p>
                    </div>
                    <div class="col-md-6">
                        <p>Tiền sản phẩm: ${ $scope.formatCurrency(invoice.amount)}</p>
                        <p>Tổng tiền: ${  $scope.formatCurrency(invoice.newAmount)}</p>
                        <p>Ngày thanh toán: ${ $scope.formatDate(invoice.createDate)}</p>
                    </div>
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

