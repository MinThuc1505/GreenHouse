appClient.controller('headerController', ['$scope', 'UserService', 'CartService', function ($scope, UserService, CartService) {

    $scope.init = function () {
        $scope.cookiesUsername = UserService.getCookiesUsername();
        $scope.roleString = UserService.getRoleString();
        if ($scope.cookiesUsername) {
            CartService.getTotalQuantity($scope.cookiesUsername);
        }
    };

    // Logout Functionality
    $scope.logoutUser = function () {
        UserService.logoutUser();
    }

    $scope.init();
}]) 