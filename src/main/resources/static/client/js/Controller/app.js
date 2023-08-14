const appClient = angular.module('appClient', ['ngRoute', 'ngCookies',]);

// Constant - start
appClient.constant('urlIndexClient', 'http://localhost:8081/client/rest/index');
appClient.constant('urlShopClient', 'http://localhost:8081/client/rest/shop');
appClient.constant('urlMenuClient', 'http://localhost:8081/client/rest/menu');
appClient.constant('urlSignInClient', 'http://localhost:8081/client/rest/signIn');
appClient.constant('urlHeaderClient', 'http://localhost:8081/client/rest/header');
appClient.constant('urlCartClient', 'http://localhost:8081/client/rest/cart');
appClient.constant('urlCheckoutClient', 'http://localhost:8081/client/rest/checkout');
appClient.constant('urlSignUpClient', 'http://localhost:8081/client/rest/signup');
appClient.constant('urlProfileClient', 'http://localhost:8081/client/rest/profile');
appClient.constant('urlChangePasswordClient', 'http://localhost:8081/client/rest/change-password');
appClient.constant('urlForgotPasswordClient', 'http://localhost:8081/client/rest/forgot-password');
appClient.constant('urlSingleProductClient', 'http://localhost:8081/client/rest/single-product');
// Constant - end

//Service - start
// User service - start
appClient.service('UserService', ['$cookies', function ($cookies) {

    this.logoutUser = function () {
        $cookies.remove('username');
        $cookies.remove('role');
        window.location.href = "/client/index";
        
    };

    this.getCookiesUsername = function () {
        return $cookies.get('username');
    }

    this.getRoleString = function () {
        return $cookies.get('role');
    }
}]);
// User service - end

// Cart service - start
appClient.service('CartService', ['$http', 'urlCartClient', '$rootScope', function ($http, urlCartClient, $rootScope) {
    var host = urlCartClient;

    this.getProductInCart = function (username) {
        var url = `${host}?username=${username}`;
        $http.get(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
            if (resp.data.status == 'success') {
                // Chuyển đổi danh sách carts thành mảng JSON
                $rootScope.carts = JSON.parse(resp.data.data);
                console.log($rootScope.carts);
            }
        }).catch(Error => {
            console.log("Lỗi lấy sản phẩm lên giỏ hàng");
            console.log("Error: ", Error);
        })
    }

    this.addToCart = function (productId, username) {
        var url = `${host}/add?productId=${productId}&username=${username}`;
        $http.get(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
            this.getTotalQuantity(username);
        }).catch(Error => {
            console.log("Lỗi thêm giỏ hàng");
            console.log("Error: ", Error);
        })
    };

    this.addToCartFormProductDetail = function (productId, quantity, username) {
        var url = `${host}/addFormProductDetail?productId=${productId}&quantity=${quantity}&username=${username}`;
        $http.get(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
            this.getTotalQuantity(username);
        }).catch(Error => {
            console.log("Lỗi thêm giỏ hàng");
            console.log("Error: ", Error);
        })
    };

    // Hàm xóa sản phẩm khỏi giỏ hàng
    this.removeFromCart = function (cartId, username) {
        var url = `${host}/remove?cartId=${cartId}&username=${username}`;
        $http.get(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
        }).catch(Error => {
            console.log("Lỗi xóa giỏ hàng");
            console.log("Error: ", Error);
        })
    };

    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
    this.updateCartItemQuantity = function (cartId, quantity, index) {
        console.log(index + "------index" + quantity + "------quantity");
        var url = `${host}/update?cartId=${cartId}&quantity=${quantity}`;
        $http.post(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
            // Cập nhật lại tổng giá trị cho sản phẩm tại chỉ mục index
            var totalPriceElement = angular.element(document.getElementsByClassName("cart-totalPrice")[index]);
            totalPriceElement.text(resp.data.totalAmount.toLocaleString() + ' VND');
        }).catch(error => {
            console.log("Lỗi cập nhật giỏ hàng");
            console.log("Error: ", error);
        });

    };


    // Hàm tính tổng số lượng sản phẩm trong giỏ hàng
    this.getTotalQuantity = async function (username) {
        var url = `${host}/getTotalQuantity?username=${username}`;
        $http.get(url).then(resp => {
            // console.log(resp.data.status);
            // console.log(resp.data.message);
            $rootScope.qtyCart = resp.data.qtyCart;
        }).catch(Error => {
            console.log("Lỗi lấy tổng số lượng sản phẩm giỏ hàng");
            console.log("Error: ", Error);
            return 0;
        })
    };
}]);
// Cart service - end
//Service - end
