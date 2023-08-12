appClient.controller('bodyController', ['$scope', 'UserService', 'CartService', function ($scope, UserService, CartService) {
    var username = UserService.getCookiesUsername();

    $scope.checkLogin = function () {
        if (username) {
            window.location.href = '/client/cart';
        } else {
            window.location.href = '/client/signin';
        }
    }

    $scope.addToCart = function (productId) {
        if (username) {
            CartService.addToCart(productId, username);
            notificationDATA('Thêm sản phẩm thành công', 'success');
        } else {
            notificationDATA('Hãy đăng nhập để thêm sản phẩm', 'error');
        }
    };

    $scope.showProductDetails = function (product) {
        $scope.selectedProduct = product;
        $('#product-modal').modal('show');
    };

    $scope.modalAddToCart = function (productId) {
        if (username) {
            CartService.addToCart(productId, username);
            notificationDATA('Thêm sản phẩm thành công', 'success');
        } else {
            notificationDATA('Hãy đăng nhập để thêm sản phẩm', 'error');
        }
    };

    $scope.viewProductDetails = function (productId) {
        console.log("Xem chi tiết sản phẩm có ID: " + productId);
        window.location.href = `/client/single-product?productId=${productId}`
    }
}]);
