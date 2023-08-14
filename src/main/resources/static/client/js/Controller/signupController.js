appClient.controller('singUpController', function ($scope, $http, urlSignUpClient, $cookies) {
    let host = urlSignUpClient;
    var item = {};

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, '\\$&');
        var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    $scope.validateToken = function (token) {
        var url = `${host}/validateToken?token=${token}`;
        $http({
            method: 'POST',
            url: url,
        }).then(function (response) {
            Swal.fire({
                icon: response.data.status,
                title: 'Đăng ký tài khoản',
                text: response.data.message,
            });
            window.location.href = "/client/signin";
        }).catch(function (error) {
            console.error(error);
            Swal.fire({
                icon: response.data.status,
                title: 'Đăng ký tài khoản',
                text: response.data.message,
            });
        });
    }

    $scope.init = function () {
        var token = getParameterByName("token");
        if (token) {
            $scope.validateToken(token);
        }
    }

    $scope.sendEmail = function (email, username) {
        showLoading();
        var url = `${host}/sendmail?email=${email}&username=${username}`;
        $http({
            method: 'POST',
            url: url,
        }).then(function (response) {
            hideLoading();
            if (response.data.status == "success") {
                Swal.fire({
                    icon: 'success',
                    title: 'Gửi mail thành công',
                    text: response.data.message
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi khi gửi mail',
                    text: response.data.message
                });
            }
        }).catch(function (error) {
            console.error(error);
        });
    };

    $scope.validate = function () {
        $scope.errorMessages = {};
        var fullName = $scope.fullName || "";
        var username = $scope.username || "";
        var email = $scope.email || "";
        var phone = $scope.phone || "";
        var password = $scope.password || "";

        item = {
            fullName: fullName,
            username: username,
            email: email,
            phone: phone,
            password: password
        };
        var url = `${host}/validate`;
        $http.post(url, item).then(resp => {
            var account = resp.data
            console.log(account);
            // Xử lí khi nhấn nút "Gửi Mail"
            $scope.sendEmail(account.email, account.username);
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

    $scope.createAccount = function (item) {
        var url = `${host}/create`;
        $http.post(url, item).then(resp => {
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đăng ký thành công.`,
            });
        }).catch(Error => {

        })
    }

    $scope.init();
})