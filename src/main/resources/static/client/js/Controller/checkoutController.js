appClient.controller('checkoutController', ['$scope', '$http', 'UserService', 'urlCheckoutClient', function ($scope, $http, UserService, urlCheckoutClient) {
    var username = UserService.getSessionUsername();
    var host = urlCheckoutClient;


    $scope.init = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
            if (resp.data.status == 'success') {
                // Chuyển đổi danh sách carts thành mảng JSON
                $scope.carts = resp.data.data;
                $scope.totalAmount = resp.data.totalAmount;
            }
        }).catch(Error => {
            console.log("Lỗi font-end lấy dữ liệu trang thanh toán");
            console.log("Error: ", Error);
        })
    }
 
    $scope.init();

    $scope.isCOD = false;
    $scope.isVNPay = false;

    $scope.toggleCOD = function () {
        $scope.isCOD = true;
        $scope.isVNPay = false;
    };

    $scope.toggleVNPay = function () {
        $scope.isCOD = false;
        $scope.isVNPay = true;
    };

}]);