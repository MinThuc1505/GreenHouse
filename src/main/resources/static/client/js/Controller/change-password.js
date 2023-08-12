appClient.controller('change-passwordController', function ($scope, $http, urlChangePasswordClient, $cookies) {
    let host = urlChangePasswordClient;
    var cookieUsername = $cookies.get('username');
    $scope.accountData = {};


    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, '\\$&');
        var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    $scope.changePassword = function () {
        if (!$scope.password || !$scope.confirmPassword) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Vui lòng điền đầy đủ thông tin.',
            });
            return;
        }

        if ($scope.password !== $scope.confirmPassword) {
            Swal.fire({
                icon: 'warning',
                title: 'Cảnh báo',
                text: 'Mật khẩu không khớp.',
            });
            return;
        }

        var username = getParameterByName("username");
        var token = getParameterByName("token");

        if (username && cookieUsername == null) {
            var url = `${host}/${username}`;
            $http.patch(url, $scope.accountData).then(resp => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: `Đã đổi mật khẩu ` + username + ` thành công.`,
                });
            }).catch(Error => {
                console.log(Error.data);
            })
        } else if (token && cookieUsername == null) {
            var password = $scope.password;
            var confirmPassword = $scope.confirmPassword;
            var data = {
                password: password,
                confirmPassword: confirmPassword,
            }
            var url = `${host}?token=${token}`;
            $http({
                method: 'POST',
                url: url,
                data: data
            }).then(function (response) {
                if (response.data.status == "success") {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: response.data.message,
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Thất bại',
                        text: response.data.message,
                    });
                }
            }).catch(function (error) {
                console.error("Lỗi khi gọi API:", error);
            });
        } else {
            var url = `${host}/${cookieUsername}`;
            $http.patch(url, $scope.accountData).then(resp => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: `Đã đổi mật khẩu ` + cookieUsername + ` thành công.`,
                });
            }).catch(Error => {
                console.log(Error.data);
            })
        }
    }

    $scope.getDetail = function (key) {
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
            console.log(resp.data);
            $scope.accountData = resp.data;
        }).catch(Error => {
            console.log("Error", Error);
        })
    }

    if (cookieUsername) {
        $scope.getDetail(cookieUsername);
    }

})