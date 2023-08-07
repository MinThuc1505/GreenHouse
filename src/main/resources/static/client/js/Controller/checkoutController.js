appClient.controller('checkoutController', ['$scope', '$http', 'UserService', 'urlCheckoutClient', function ($scope, $http, UserService, urlCheckoutClient) {
    var username = UserService.getCookiesUsername();
    var host = urlCheckoutClient;

    var account;
    // Init
    $scope.init = function (username) {
        var url = `${host}?username=${username}`;
        $http.get(url).then(resp => {
            console.log(resp.data.status);
            console.log(resp.data.message);
            if (resp.data.status == 'success') {
                // Chuyển đổi danh sách carts thành mảng JSON
                $scope.carts = resp.data.carts;
                $scope.totalAmount = resp.data.totalAmount;
                $scope.newTotalAmount = resp.data.totalAmount;
                account = resp.data.account;
                $scope.receiverAddress = account.address;
            } else {
                notificationDATA(resp.data.message, resp.data.status);
            }
        }).catch(Error => {
            console.log("Lỗi font-end lấy dữ liệu trang thanh toán");
            console.log("Error: ", Error);
        })
    }

    if (username) {
        $scope.init(username);
    } else {
        window.location.href = "/client/signin";
    }


    // Payment Method
    $scope.isCOD = false;
    $scope.isVNPay = false;

    $scope.toggleCOD = function () {
        $scope.isCOD = true;
        $scope.isVNPay = false;
    };

    $scope.toggleVNPay = function () {
        $scope.isCOD = false;
        $scope.isVNPay = true;
    };


    $scope.isAppliedDiscountCode = false;

    var discount;
    var discountCode = "";

    $scope.applyDiscount = function () {
        discountCode = $scope.discountCode;
        if (discountCode) {
            console.log("Apply Discount Code: " + discountCode);
            var url = `${host}/apply-discount?discountCode=${discountCode}`;
            $http({
                method: 'POST',
                url: url,
            }).then(function (resp) {
                if (resp.data.status == "success") {
                    notificationDATA(resp.data.message, resp.data.status);
                    discount = resp.data.discount;
                    $scope.newTotalAmount = $scope.totalAmount * (1 - discount.discountPercent / 100);
                    $scope.isAppliedDiscountCode = true;
                } else {
                    notificationDATA(resp.data.message, resp.data.status);
                }
            }, function (error) {
                console.log("Lỗi áp dụng mã giảm giá nè!!");
                console.log(error);
            });
        } else {
            $scope.newTotalAmount = $scope.totalAmount;
            $scope.isAppliedDiscountCode = false;
        }
    };


    // Payment Function
    $scope.payment = function () {
        var paymentMethod = $scope.isCOD ? 'COD' : 'VNPay';
        var bankCode = $scope.isVNPay ? document.querySelector('input[name="card"]:checked').value : "";

        var amount = $scope.totalAmount;
        var newAmount;

        var discountCode;
        var discountPercent;
        if (discount) {
            discountCode = discount.discountCode;
            discountPercent = discount.discountPercent;
            newAmount = amount * (1 - discountPercent / 100);
        }

        var receiverAddress = $scope.receiverAddress;
        if (!receiverAddress) {
            var addressInput = document.getElementById('address');
            addressInput.focus();
            notificationDATA("Bạn vui lòng nhập địa chỉ để chúng tôi gửi đến bạn nào!!!", "error");
            return;
        }
        var currentDate = new Date();

        var url = `${host}/create-payment`;
        var data = {
            username: username,
            createDate: currentDate,
            amount: amount,
            discountCode: discountCode ? discountCode : null,
            bankCode: bankCode ? bankCode : null,
            discountPercent: discountPercent ? discountPercent : null,
            newAmount: newAmount ? newAmount : null,
            receiverAddress: receiverAddress,
            paymentMethod: paymentMethod
        };
        console.log(data);
        $http({
            method: 'POST',
            url: url,
            data: data
        }).then(function (response) {
            if (response.data.status == "COD") {
                window.location.href = response.data.data;
            } else {
                window.location.href = response.data.data;
            }
        }, function (error) {
            console.log("Lỗi thanh toán: [" + paymentMethod + "]");
            console.log(error);
        });
    };

}]);