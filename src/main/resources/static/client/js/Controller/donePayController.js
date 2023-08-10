appClient.controller('donePayController', ['$scope', '$http', 'urlCheckoutClient', function ($scope, $http, urlCheckoutClient) {
    var host = urlCheckoutClient;

    $scope.title = 'Thanh toán thành công';
    $scope.invoiceId = ''; // Cập nhật giá trị này từ dữ liệu hóa đơn
    $scope.isError = false; // Cập nhật giá trị này khi xảy ra lỗi
    $scope.messageError = '';
    $scope.cod = true;

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, '\\$&');
        var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }
    // Hàm để call API và truyền dữ liệu từ URL vào API
    $scope.init = function () {
        var currentURL = window.location.href;
        var startIndex = currentURL.indexOf("donePay?");

        if (startIndex === -1) {
            console.log("Không tìm thấy vnpay-ipn trong URL.");
            return;
        }
        $scope.cod = false;
        var vnpayData = {
            vnp_Amount: getParameterByName('vnp_Amount'),
            vnp_BankCode: getParameterByName('vnp_BankCode'),
            vnp_BankTranNo: getParameterByName('vnp_BankTranNo'),
            vnp_CardType: getParameterByName('vnp_CardType'),
            vnp_OrderInfo: getParameterByName('vnp_OrderInfo'),
            vnp_PayDate: getParameterByName('vnp_PayDate'),
            vnp_ResponseCode: getParameterByName('vnp_ResponseCode'),
            vnp_TmnCode: getParameterByName('vnp_TmnCode'),
            vnp_TransactionNo: getParameterByName('vnp_TransactionNo'),
            vnp_TransactionStatus: getParameterByName('vnp_TransactionStatus'),
            vnp_TxnRef: getParameterByName('vnp_TxnRef'),
            vnp_SecureHash: getParameterByName('vnp_SecureHash')
        };

        var url = `${host}/vnpay-ipn`;

        $http({
            method: 'POST',
            url: url,
            data: vnpayData,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            console.log("Kết quả từ API:", response.data);
            $scope.invoiceId = response.data.invoiceId;
            if (response.data.RspCode == "00") {
                notificationDATA(response.data.Message, "success");
            } else {
                notificationDATA(response.data.Message, "error");
                $scope.isError = true;
                $scope.title = 'Thanh toán thất bại';
                $scope.messageError = response.data.Message;
            }
        }).catch(function (error) {
            console.error("Lỗi khi gọi API:", error);
        });
    };




    $scope.init();
}]);
