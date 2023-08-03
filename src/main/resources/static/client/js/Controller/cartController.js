appClient.controller('cartController', ['$scope', '$http', 'urlCartClient', 'UserService', 'CartService', 'urlCheckoutClient', function ($scope, $http, urlCartClient, UserService, CartService, urlCheckoutClient) {
    var username = UserService.getSessionUsername();
    var host = urlCartClient;
    var hostCheckout = urlCheckoutClient;

    $scope.initCart = function () {
        CartService.getProductInCart(username);
    }

    $scope.initCart();

    // Remove cart
    var idCartsDeleted = [];

    
    $scope.isCartItemDeleted = function (cartId) {
        if(idCartsDeleted.includes(cartId)){
            return true;
        }else{
            return false;
        }
    };

    $scope.editCart = false;
    $scope.editMode = false;
    $scope.editCarts = function () {
        $scope.editCart = !$scope.editCart;
        $scope.editMode = !$scope.editMode;
        if (!$scope.editMode) {
            console.log("Lưu nè!!!!!!!!!");
            idCartsDeleted = [];
            $scope.initCart();
        } else {
            // Hàm xóa sản phẩm khỏi giỏ hàng
            $scope.removeFromCart = function (cartId) {
                if (username) {
                    CartService.removeFromCart(cartId, username);
                    idCartsDeleted.push(cartId);
                    notificationDATA('Xóa sản phẩm khỏi giỏ hàng thành công', 'success');
                } else {
                    notificationDATA('Hãy đăng nhập để thêm sản phẩm', 'error');
                }
            };
        }
    }

    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
    $scope.updateCartItemQuantity = function (cart, index) {
        var quantity = $scope.carts[index][4];
        if (quantity != null) {
            console.log("==========" + quantity);
            CartService.updateCartItemQuantity(cart[0], quantity, index);
        }
    };

    $scope.checkQuantityChanged = function (cart, index) {
        var quantity = $scope.carts[index][4];
        if (quantity == null) {
            var url = `${host}/getOldQuantity?cartId=${cart[0]}`;
            $http.get(url).then(resp => {
                $scope.carts[index][4] = resp.data.oldQty;
                console.log("==================" + resp.data.oldQty);
            }).catch(error => {
                console.log("Lỗi cập nhật giỏ hàng");
                console.log("Error: ", error);
            });
        }
    }



    // CHECK ITEM CARTS
    $scope.check = function () {
        var checkboxes = document.querySelectorAll('#cart-tableBody input[type="checkbox"]');
        var allChecked = true;
        checkboxes.forEach(function (checkbox) {
            if (!checkbox.checked) {
                allChecked = false;
            }
        });
        $('#checkAllId').prop('checked', allChecked);
    }

    $scope.checkAll = function () {
        var checkboxes = document.querySelectorAll('#cart-tableBody input[type="checkbox"]');
        checkboxes.forEach(function (checkbox) {
            checkbox.checked = $('#checkAllId').is(':checked');
        }, $('#checkAllId'));
    }

    $scope.pay = function () {
        var checkedIDs = [];
        var checkboxes = document.querySelectorAll('#cart-tableBody input[type="checkbox"]:checked');

        checkboxes.forEach(function (checkbox) {
            checkedIDs.push(checkbox.value);
        });
        var checkedIDsStr = checkedIDs.join(';');
        if (checkedIDsStr.length > 0) {
            // Tạo URL mới với query parameter
            var url = `${host}/checkout?checkedIDsStr=${checkedIDsStr}`;
            $http.get(url).then(resp => {
                console.log(resp.data.status);
                console.log(resp.data.message);
                if (resp.data.status == "error") {
                    var errorMessage = resp.data.productIsNotEnough;
                    console.log(errorMessage);
                    for (let index = 0; index < errorMessage.length; index++) {
                        const element = errorMessage[index];
                        notificationDATA(element, 'error');
                    }
                }
                if (resp.data.status == "success") {
                    var url = `${hostCheckout}/temp?checkedIDsStr=${checkedIDsStr}`;
                    $http.get(url).then(resp => {
                        console.log(resp.data.status);
                        console.log(resp.data.message);
                        window.location.href = "/client/checkout"
                    }).catch(Error => {
                        console.log("Lỗi chuyển sản phẩm sang thanh toán");
                        console.log("Error: ", Error);
                    })
                }

            }).catch(Error => {
                console.log("Lỗi check in stock product");
                console.log("Error: ", Error);
            })
        } else {
            notificationDATA('Vui lòng chọn sản phẩm cần thanh toán', 'error');
        }

    }
}]);
