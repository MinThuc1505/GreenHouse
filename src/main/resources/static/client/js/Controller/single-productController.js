appClient.controller('single-productController', function ($scope, $http, urlSingleProductClient, UserService, CartService) {
    let host = urlSingleProductClient;
    var username = UserService.getCookiesUsername();
    $scope.productQuantity = 1;

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, '\\$&');
        var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    $scope.getProduct = function (productId) {
        var url = `${host}/getProduct?productId=${productId}`
        $http({
            method: 'GET',
            url: url,
        }).then(function (response) {
            if (response.data.status == "success") {
                $scope.product = response.data.product;
                $scope.sizeOfProduct = response.data.size;
                $scope.materialOfProduct = response.data.material;
                console.log(response.data.product);
                console.log(response.data.status);
                console.log(response.data.message);
            } else {
                console.log(response.data.status);
                console.log(response.data.message);
            }
        }).catch(function (error) {
            console.error("Lỗi khi gọi API:", error);
        });
    }

    $scope.getRecommendProduct = function (productId) {
        var url = `${host}/getRecommendProduct?productId=${productId}`
        $http({
            method: 'GET',
            url: url,
        }).then(function (response) {
            if (response.data.status == "success") {
                $scope.relatedProducts = response.data.recommendedProducts;

                console.log(response.data.status);
                console.log(response.data.message);
                console.log(response.data.recommendedProducts);
            } else {
                console.log(response.data.status);
                console.log(response.data.message);
            }
        }).catch(function (error) {
            console.error("Lỗi khi gọi API:", error);
        });
    }

    $scope.init = function () {
        var productId = getParameterByName("productId");
        $scope.getProduct(productId);
        $scope.getRecommendProduct(productId);
    }

    


    $scope.addProductToCart = function (productId) {
        var productQuantity = document.getElementById('product-quantity').value;
        if (username) {
            CartService.addToCartFormProductDetail(productId, productQuantity, username);
            notificationDATA('Thêm sản phẩm thành công', 'success');
        } else {
            notificationDATA('Hãy đăng nhập để thêm sản phẩm', 'error');
        }

    }






    $scope.init();
});