appClient.controller('profileController', function ($scope, $http, urlProfileClient, $cookies) {
    let host = urlProfileClient;

    var cookieUsername = $cookies.get('username');

    $scope.updateAccount = function () {
        $scope.errorMessages = {};
        var url = `${host}/${cookieUsername}`;
        $http.patch(url, $scope.accountData).then(resp => {
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Cập nhật ` + cookieUsername +  ` thành công.` ,
            });
        }).catch(Error => {
            console.log(Error.data);
            if (Error.data) {
                $scope.errorMessages = Error.data; 
                if (Error.data.AccountExists){
                    Swal.fire({
                        icon: 'info',
                        title: 'Thông tin',
                        text: $scope.errorMessages.AccountExists,
                    });
                }else if (Error.data.emailExists){
                    Swal.fire({
                        icon: 'info',
                        title: 'Thông tin',
                        text: $scope.errorMessages.emailExists,
                    });
                }else if (Error.data.phoneExists){
                    Swal.fire({
                        icon: 'info',
                        title: 'Thông tin',
                        text: $scope.errorMessages.phoneExists,
                    });
                }
            } 
        })
    }

    $scope.getDetail = function (key) {
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
           console.log(resp.data);
           $scope.accountData = resp.data.account;
           $scope.bills = resp.data.bills;
        }).catch(Error => { 
            console.log("Error", Error);
        })
    }

    $scope.showBillDetailsModal = function (billId) {
        var url = `${host}/getBillDetails?billId=${billId}`;
        $http.get(url)
            .then(function (response) {
                $scope.selectedBill = response.data.bill;
                $scope.selectedBillDetails = response.data.billDetails;
                $('#billDetailsModal').modal('show');
            })
            .catch(function (error) {
                console.log(error);
            });
    };

    if(cookieUsername){
        $scope.getDetail(cookieUsername);
    }else{
        window.location.href= "/client/signin";
    }

})