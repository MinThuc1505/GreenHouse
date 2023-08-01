appClient.controller('headerController', function ($scope, $http, urlHeaderClient, $cookies) {

    var host = urlHeaderClient;

    $scope.init = function () {
        $scope.sessionUsername = $cookies.get('username');
        if($scope.sessionUsername){
            $scope.load_quantityCart();
        }
        
    };

    $scope.load_quantityCart = function () {
        var username = $cookies.get('username');
        var url = `${host}/quantityCart?username=${username}`;
        $http.get(url).then(resp => {
            $scope.qtyCart = resp.data.qtyCart;
            console.log(  $scope.qtyCart+"=========================");
        }).catch(Error => {
            console.log("Error: ", Error);
        })
    };

    // Logout Functionality
    $scope.logoutUser = function () {
        $cookies.remove('username');
        window.location.href = "/client/index";
    }



    $scope.init();


})