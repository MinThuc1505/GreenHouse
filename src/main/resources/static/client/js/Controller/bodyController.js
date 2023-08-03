appClient.controller('bodyController', ['$scope', 'UserService', 'CartService', function ($scope, UserService, CartService) {
    var username = UserService.getSessionUsername();

    $scope.checkLogin = function () {
        if (username) {
            window.location.href = '/client/cart';
        } else {
            window.location.href = '/client/signin';
        }
    }

    // Hàm thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function (productId) {
        if (username) {
            CartService.addToCart(productId, username);
            notificationDATA('Thêm sản phẩm thành công', 'success');
        } else {
            notificationDATA('Hãy đăng nhập để thêm sản phẩm', 'error');
        }
    };

}]);
