app.controller('reportStaticController', function ($scope, $http, urlReportStatic) {
    let host = urlReportStatic;
    $scope.form = {};
    $scope.items = [];

    $scope.searchData = function () {
        var startDate = formatDateToISOString($scope.form.startDate);
        var endDate = formatDateToISOString($scope.form.endDate);
    
        if (startDate && endDate && startDate <= endDate) {
            $scope.startDateError = false; // Reset lỗi nếu thỏa mãn điều kiện
            var url = `${host}?startDate=${startDate}&endDate=${endDate}`;
            $http.get(url).then(resp => {
                $scope.items = resp.data;
                console.log("Success", $scope.items);
            }).catch(error => {
                console.log("Error", error);
            });
        } else {
            $scope.startDateError = true; // Hiển thị lỗi ngày bắt đầu không hợp lệ
            $scope.items = []; // Reset danh sách khi có lỗi ngày
        }
    }
    
    

    $scope.loadAllData = function() {
        var url = `${host}/all`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
            console.log("Success", $scope.items);
            $scope.getRevenueData1(); // Gọi lại hàm getRevenueData1() sau khi tải lại dữ liệu
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
                type: 'bar', // Thay đổi type thành 'line'
                data: {
                    labels: years,
                    datasets: [{
                        label: 'VNĐ',
                        data: revenues,
                        backgroundColor: '#00D9FF', // Màu nền cột
                        borderColor: '#ff00cc',    // Màu viền cột
                        borderWidth: 1,
                        fill: false // Thiết lập fill: false để không tô màu dưới đường line
                    }]
                },
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: 'BIỂU ĐỒ DOANH THU THÁNG', // Đặt tiêu đề ở đây
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
    $scope.getTotalQuantity = function() {
        let total = 0;
        for (let i = 0; i < $scope.items.length; i++) {
            total += $scope.items[i][3];
        }
        return total;
    };

    $scope.getTotalRevenue = function() {
        let total = 0;
        for (let i = 0; i < $scope.items.length; i++) {
            total += $scope.items[i][4];
        }
        return total;
    };
    
    $scope.getRevenueData1();
    // Gọi hàm searchData khi người dùng nhấn nút Tìm kiếm
    $scope.searchData();
    // Hiển thị dữ liệu
    $scope.loadAllData();
});
//linebar
// $scope.getRevenueData1 = function () {
//     var url = `${host}/monthly`;
//     $http.get(url).then(function (response) {
//         // Lấy dữ liệu từ API response
//         var data = response.data;
//         var years = [];
//         var revenues = [];
//         for (var i = 0; i < data.length; i++) {
//             years.push(data[i][0]);
//             revenues.push(data[i][1]);
//         }

//         // Cấu hình biểu đồ line chart và bar chart
//         var ctx = document.getElementById('barStatic').getContext('2d');
//         var chart = new Chart(ctx, {
//             type: 'bar', // Sử dụng loại biểu đồ bar
//             data: {
//                 labels: years,
//                 datasets: [
//                     {
//                         type: 'line', // Sử dụng loại biểu đồ line
//                         label: 'Doanh thu (VNĐ)',
//                         data: revenues,
//                         borderColor: '#ff00cc',
//                         borderWidth: 2,
//                         fill: false
//                     },
//                     {
//                         type: 'bar', // Sử dụng loại biểu đồ bar
//                         label: 'Doanh thu (VNĐ)',
//                         data: revenues,
//                         backgroundColor: 'rgba(75, 192, 192, 0.2)', // Màu nền cột
//                         borderColor: 'rgba(75, 192, 192, 1)', // Màu viền cột
//                         borderWidth: 1
//                     }
//                 ]
//             },
//             options: {
//                 plugins: {
//                     title: {
//                         display: true,
//                         text: 'BIỂU ĐỒ DOANH THU THÁNG',
//                         position: 'bottom',
//                         fontSize: 16
//                     }
//                 },
//                 scales: {
//                     y: {
//                         beginAtZero: true
//                     }
//                 }
//             }
//         });
//     }).catch(function (error) {
//         console.log(error);
//     });
// };
