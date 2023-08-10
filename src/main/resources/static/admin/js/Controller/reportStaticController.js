app.controller('reportStaticController', function ($scope, $http, urlReportStatic) {
    let host = urlReportStatic;
    $scope.form = {};
    $scope.items = [];

    $scope.searchData = function () {
        var startDate = formatDateToISOString($scope.form.startDate);
        var endDate = formatDateToISOString($scope.form.endDate);

        if (startDate && endDate) {
            var url = `${host}?startDate=${startDate}&endDate=${endDate}`;
            $http.get(url).then(resp => {
                $scope.items = resp.data;
                console.log("Success", $scope.items);
            }).catch(error => {
                console.log("Error", error);
            });
        } else {
            loadAllData();
        }
    }

    $scope.loadAllData = function() {
        var url = `${host}/all`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
            console.log("Success", $scope.items);
        }).catch(error => {
            console.log("Error", error);
        });
    };

    $scope.getRevenueData1 = function () {
        var url = `${host}/monthly`;
        $http.get(url).then(function (response) {
            // Lấy dữ liệu từ API response
            var data = response.data;
            var years = [];
            var revenues = [];
            for (var i = 0; i < data.length; i++) {
                years.push(data[i][0]);
                revenues.push(data[i][1]);
            }
            console.log(response);
            // Cấu hình biểu đồ line chart
            var ctx = document.getElementById('barStatic').getContext('2d');
            var chart = new Chart(ctx, {
                type: 'line', // Thay đổi type thành 'line'
                data: {
                    labels: years,
                    datasets: [{
                        label: 'VNĐ',
                        data: revenues,
                        borderColor: '#FF0000',
                        borderWidth: 1,
                        fill: false // Thiết lập fill: false để không tô màu dưới đường line
                    }]
                },
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: 'Bảng 1.2 BIỂU ĐỒ DOANH THU THÁNG', // Đặt tiêu đề ở đây
                            position: 'bottom',
                            fontSize: 16
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
            console.log(response.data);
        }).catch(function (error) {
            console.log(error);
        });
    };
    
    
    $scope.getRevenueData1();
    // Gọi hàm searchData khi người dùng nhấn nút Tìm kiếm
    $scope.searchData();
    // Hiển thị dữ liệu
    $scope.loadAllData();
});