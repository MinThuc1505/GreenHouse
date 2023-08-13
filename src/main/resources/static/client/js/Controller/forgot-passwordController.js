appClient.controller('forgot-passwordController', ['$scope', '$http', 'urlForgotPasswordClient', function ($scope, $http, urlForgotPasswordClient) {
    var host = `${urlForgotPasswordClient}`;

    $scope.openModalOTP = function () {
        $('#otpModal').modal('show');
    };

    $scope.sendEmail = function (username) {
        showLoading();
        var url = `${host}/sendmail`;
        $http({
            method: 'POST',
            url: url,
            data: { username: username }
        }).then(function (response) {
            console.log(response.data);
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

    var countdown;
    var seconds = 60;
    $scope.startCountdown = function () {
        var resendButton = document.getElementById("resendButton");
        resendButton.disabled = true;

        countdown = setInterval(function () {
            seconds--;
            if (seconds <= 0) {
                clearInterval(countdown);
                resendButton.innerHTML = "Gửi lại mã";
                resendButton.disabled = false;
            } else {
                resendButton.innerHTML = "Gửi lại mã (" + seconds + "s)";
            }
        }, 1000);

        seconds = 60;
    };

    $scope.sendOTP = function (username) {
        showLoading();
        var url = `${host}/send-otp`;
        $http({
            method: 'POST',
            url: url,
            data: { username: username }
        }).then(function (response) {
            hideLoading();
            if (response.data.status == "success") {
                Swal.fire({
                    icon: "success",
                    title: 'Xác minh',
                    text: response.data.message
                });
                $scope.openModalOTP();
                $scope.startCountdown();
            } else {
                Swal.fire({
                    icon: "error",
                    title: 'Lỗi',
                    text: response.data.message
                });
            }
        }).catch(function (error) {
            console.error(error);
        });
    }

    $scope.reSendOTP = function () {
        var username = $scope.username;
        console.log(username);
        $scope.sendOTP(username);
        $scope.startCountdown();

    }

    $scope.validateOTP = function () {
        var username = $scope.username;
        var otp = $scope.otpCode;
        console.log(otp);
        var url = `${host}/validateOTP`;
        $http({
            method: 'POST',
            url: url,
            data: {
                username: username,
                otp: otp
            }
        }).then(function (response) {
            console.log("==============" + response.data.status);
            if (response.data.status == "success") {
                window.location.href = `/client/change-password?username=${username}`;
            }
        }).catch(function (error) {
            console.error(error);
        });
    }

    $scope.validateForm = function () {
        var username = $scope.username;
        if (!username) {
            Swal.fire({
                title: 'Lỗi',
                text: 'Hãy nhập đủ thông tin',
                icon: 'error',
            });
            return;
        }

        var url = `${host}/validateUsername`;
        $http({
            method: 'POST',
            url: url,
            data: { username: username }
        }).then(function (response) {
            if (response.data.status == "success") {
                var username = response.data.username;
                Swal.fire({
                    title: 'Xác Minh',
                    text: 'Hãy chọn cách xác minh tài khoản',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Gửi mail',
                    cancelButtonText: 'OTP',
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Xử lí khi nhấn nút "Gửi Mail"
                        $scope.sendEmail(username);
                    } else if (result.dismiss === Swal.DismissReason.cancel) {
                        // Xử lí khi nhấn nút "OTP"
                        $scope.sendOTP(username);
                    }
                });
            } else {
                Swal.fire({
                    title: 'Lỗi',
                    text: response.data.message,
                    icon: 'error',
                });
            }
        }).catch(function (error) {
            console.error(error);
        });
    }

}]);