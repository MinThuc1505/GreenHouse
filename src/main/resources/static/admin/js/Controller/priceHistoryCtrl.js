app.controller('priceHistoryCtrl', function($scope, $http, urlPriceHistoryCtrl) {
    let host = urlPriceHistoryCtrl;
    $scope.form = {};
    $scope.item = {};
    $scope.priceHistorys = [];
    $scope.orderByField = ""; // Khởi tạo giá trị mặc định của cột sắp xếp
    $scope.reverseSort = true; // Khởi tạo giá trị mặc định của hướng sắp xếp

    $scope.setOrderByField = function (field) {
        if ($scope.orderByField === field) {
          $scope.reverseSort = !$scope.reverseSort; // Đảo ngược hướng sắp xếp nếu cùng một cột được nhấp liên tiếp
        } else {
          $scope.orderByField = field;
          $scope.reverseSort = true; // Đặt hướng sắp xếp mặc định khi chọn một cột mới
        }
      };
    

    $scope.loadPriceHistory = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.priceHistorys = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    };
      
    $scope.loadPriceHistory();
});

