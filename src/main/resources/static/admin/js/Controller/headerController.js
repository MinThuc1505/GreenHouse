app.controller('headerAdminController', ['$scope', "$cookies", function ($scope, $cookies) {
    $scope.username = $cookies.get("username");
}]) 