appClient.controller('singUpController', function ($scope, $http, urlSignUpClient, $cookies) {
    let host = urlSignUpClient;
    $scope.createAccount = function () {
        $scope.errorMessages = {};
        var fullName = $scope.fullName || "";
        var username = $scope.username || "";
        var email = $scope.email || "";
        var phone = $scope.phone || "";
        var password = $scope.password || "";

        var item = {
            fullName: fullName,
            username: username,
            email: email,
            phone: phone,
            password: password
        };

        var url = `${host}`;
        $http.post(url, item).then(resp => {
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đăng ký thành công.`,
            });
        }).catch(Error => {
            if (Error.data) {
                $scope.errorMessages = Error.data;
                if (Error.data.AccountExists) {
                    Swal.fire({
                        icon: 'info',
                        title: 'Thông tin',
                        text: $scope.errorMessages.AccountExists,
                    });
                } else if (Error.data.emailExists) {
                    Swal.fire({
                        icon: 'info',
                        title: 'Thông tin',
                        text: $scope.errorMessages.emailExists,
                    });
                } else if (Error.data.phoneExists) {
                    Swal.fire({
                        icon: 'info',
                        title: 'Thông tin',
                        text: $scope.errorMessages.phoneExists,
                    });
                }
            }
        })
    }
})