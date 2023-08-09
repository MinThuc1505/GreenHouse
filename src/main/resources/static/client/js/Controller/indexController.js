appClient.controller('indexController', function ($scope, $http, urlIndexClient) {
    let host = urlIndexClient;

    $scope.load_products = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.products = resp.data.products;
            console.log(JSON.stringify($scope.products) + " loaded");
        }).catch(Error => {
            console.log("Error: ", Error);
        })
    }














    $scope.load_products();
})