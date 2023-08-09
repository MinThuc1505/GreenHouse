appClient.controller('forgot-passwordController', ['$scope', '$http', 'urlForgotPasswordClient', function ($scope, $http, urlForgotPasswordClient, $rootScope) {
    var host = `${urlForgotPasswordClient}`;

    $scope.sendEmail = function () {
        showLoading();
        var email = $('#email').val();
        console.log(email);
        if (!email) {
            $scope.emailError = "Vui lòng nhập email cần đặt lại mật khẩu";
            return;
        }
        $scope.emailError = '';
        var url = `${host}/sendmail`;
        $http({
            method: 'POST',
            url: url,
            data: { email: email }
        }).then(function (response) {
            hideLoading();
            if (response.data.status == "success") {
                Swal.fire({
                    icon: 'info',
                    title: 'Thông tin',
                    text: response.data.message
                });
            } else {
                Swal.fire({
                    icon: 'info',
                    title: 'Thông tin',
                    text: response.data.message
                });
            }
        }).catch(function (error) {
            console.error(error);
        });
    };


}]);