appClient.controller('singInController', function ($scope, $http, urlSignInClient, $cookies) {
    let host = urlSignInClient;

    $scope.account = {}; // Object to store the username and password
    $scope.loginMessage = ""; // Message to display if login fails
    $scope.errors = {}; // Object to store field errors
    $scope.remember = false; // Checkbox value for "Remember me"
    $scope.recaptchaKey = "{{ @environment.getProperty('recaptcha.key') }}"; // Recaptcha key

    // Function to submit the login form
    $scope.submitLogin = function () {
        $scope.loginMessage = "";
        $scope.errors = {};

        if (!$scope.account.username) {
            $scope.errors.username = "Hãy nhập tài khoản.";
        }
        if (!$scope.account.password) {
            $scope.errors.password = "Hãy nhập mật khẩu.";
        }

        if (Object.keys($scope.errors).length === 0) {
            var url = `${host}`;
            var data = $scope.account;
            $http.post(url, data).then(resp => {
                $cookies.put('username', resp.data.username);
                window.location.href = "/client/index";
            },
                function (error) {
                    // Handle login failure
                    $scope.loginMessage = "Đăng nhập thất bại. Hãy kiểm tra lại tài khoản và mật khẩu.";
                }
            );
        }
    };

})