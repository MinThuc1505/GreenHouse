appClient.controller('indexController', function ($scope, $http, urlIndexClient, $cookies) {
    let host = urlIndexClient;

    $scope.load_products = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.products = resp.data.products;
            console.log(JSON.stringify($scope.products) + " loaded");
        }).catch(Error => {
            console.log("Error: ", Error);
        })
    };

    $scope.init = function () {
        $scope.load_products();
        $scope.sessionUsername = $cookies.get('username');
    };

    // Function show detail product selected
    $scope.showProductDetails = function (product) {
        $scope.selectedProduct = product; // Set selected product data
        $('#product-modal').modal('show'); // Show the modal
    };




    $scope.init();

})