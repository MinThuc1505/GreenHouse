appClient.controller('change-passwordController', function ($scope, $http, urlChangePasswordClient, $cookies) {
    let host = urlChangePasswordClient;
    var cookieUsername = $cookies.get('username');
    $scope.accountData = {};
   
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

        var url = `${host}/${cookieUsername}`;
        $http.patch(url, $scope.accountData).then(resp => {
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đã đổi mật khẩu` + cookieUsername +  ` thành công.` ,
            });
        }).catch(Error => {
            console.log(Error.data);
        })
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

    $scope.getDetail(cookieUsername);

})