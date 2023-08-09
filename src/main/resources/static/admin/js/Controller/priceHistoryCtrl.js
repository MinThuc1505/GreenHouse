app.controller('priceHistoryCtrl', function($scope, $http, urlPriceHistoryCtrl) {
    let host = urlPriceHistoryCtrl;
    $scope.form = {};
    $scope.item = {};
    $scope.priceHistorys = [];
 

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

