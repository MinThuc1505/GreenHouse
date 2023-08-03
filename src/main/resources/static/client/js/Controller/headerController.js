appClient.controller('headerController', ['$scope', 'UserService', 'CartService', '$rootScope', function ($scope, UserService, CartService, $rootScope) {

    $scope.init = function () {
        $scope.sessionUsername = UserService.getSessionUsername();
        if ($scope.sessionUsername) {
            CartService.getTotalQuantity($scope.sessionUsername);
        }
    };

    // Logout Functionality
    $scope.logoutUser = function () {
        UserService.logoutUser();
    }

    $scope.init();
}])