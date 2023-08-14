app.controller('indexController', function ($scope, $http, indexController) {
    let host = indexController;
    $scope.form = {};
    $scope.items = {};
    $scope.revenue = null;
    $scope.totalDiscounts = null;
    $scope.totalUsers = null;
    $scope.totalProducts = null;

    $scope.getRevenue = function () {
        var url = `${host}/revenue`;
        $http.get(url).then(function (resp) {
            var revenue = parseFloat(resp.data);
            if (!isNaN(revenue)) {
                $scope.revenue = revenue;
            } else {
                console.log("Invalid revenue data:", resp.data);
            }
            console.log("Success - Revenue", $scope.revenue);
        }).catch(function (error) {
            console.log("Error - Revenue", error);
        });
    };

    $scope.getTotalDiscounts = function () {
        var url = `${host}/totalDiscounts`;
        $http.get(url).then(function (resp) {
            var totalDiscounts = parseFloat(resp.data);
            if (!isNaN(totalDiscounts)) {
                $scope.totalDiscounts = totalDiscounts;
            } else {
                console.log("Invalid total discounts data:", resp.data);
            }
            console.log("Success - Total Discounts", $scope.totalDiscounts);
        }).catch(function (error) {
            console.log("Error - Total Discounts", error);
        });
    };

    $scope.getTotalUsers = function () {
        var url = `${host}/totalUsers`;
        $http.get(url).then(function (resp) {
            var totalUsers = parseFloat(resp.data);
            if (!isNaN(totalUsers)) {
                $scope.totalUsers = totalUsers;
            } else {
                console.log("Invalid total users data:", resp.data);
            }
            console.log("Success - Total Users", $scope.totalUsers);
        }).catch(function (error) {
            console.log("Error - Total Users", error);
        });
    };

    $scope.getTotalProducts = function () {
        var url = `${host}/totalProducts`;
        $http.get(url).then(function (resp) {
            var totalProducts = parseFloat(resp.data);
            if (!isNaN(totalProducts)) {
                $scope.totalProducts = totalProducts;
            } else {
                console.log("Invalid total products data:", resp.data);
            }
            console.log("Success - Total Products", $scope.totalProducts);
        }).catch(function (error) {
            console.log("Error - Total Products", error);
        });
    };

    $scope.loadAllData = function () {
        var url = `${host}/topProducts`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
            console.log("Success", $scope.items);
        }).catch(error => {
            console.log("Error", error);
        });
    };

    $scope.getRevenueData2 = function () {
        var url = `${host}/year`;
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
            var ctx = document.getElementById('barIndex').getContext('2d');
            var chart = new Chart(ctx, {
                type: 'line', // Thay đổi type thành 'line'
                data: {
                    labels: years,
                    datasets: [{
                        label: 'VNĐ',
                        data: revenues,
                        borderColor: '#ff00cc',
                        borderWidth: 1,
                        fill: false // Thiết lập fill: false để không tô màu dưới đường line
                    }]
                },
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: 'BIỂU ĐỒ DOANH THU NĂM', // Đặt tiêu đề ở đây
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
    $scope.getRevenueData1 = function () {
        var url = `${host}/role`;
        $http.get(url).then(function (response) {
            // Lấy dữ liệu từ API response
            var data = response.data;
            var roles = [];
            var counts = [];
            for (var i = 0; i < data.length; i++) {
                var role = data[i][0] === true ? 'Admin' : 'User'; // Xử lý giá trị role
                roles.push(role);
                counts.push(data[i][1]);
            }


            // Tạo biểu đồ tròn
            var ctx = document.getElementById('barRoleChart').getContext('2d');
            var chart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: roles,
                    datasets: [{
                        data: counts,
                        backgroundColor: [
                            '#FF6384',
                            '#36A2EB',
                            // ... thêm các màu khác tương ứng với số lượng vai trò
                        ]
                    }]
                },
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: 'BIỂU ĐỒ VAI TRÒ NGƯỜI DÙNG',
                            position: 'bottom',
                            fontSize: 16
                        }
                    },
                    maintainAspectRatio: true, // Tắt tỷ lệ giữa chiều rộng và chiều cao
                    aspectRatio: 1.6// Tỷ lệ giữa chiều cao và chiều rộng (ở đây ví dụ là 3:2)
                }
            });
        }).catch(function (error) {
            console.log(error);
        });
    };

    $scope.formatCurrency = function(amount) {
        // Thực hiện chuyển đổi số thành chuỗi định dạng tiền tệ
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    };



    $scope.getRevenue();
    $scope.getTotalDiscounts();
    $scope.getTotalUsers();
    $scope.getTotalProducts();
    $scope.loadAllData();
    $scope.getRevenueData1();
    $scope.getRevenueData2();
});
const today = moment();
let currentMonth = today.clone();

function renderMonthlyCalendar() {
    const monthlyCalendarBody = document.getElementById('monthly-calendar-body');
    monthlyCalendarBody.innerHTML = '';

    const firstDayOfMonth = currentMonth.clone().startOf('month').startOf('isoWeek');

    for (let week = 0; week < 6; week++) {
        const weekRow = document.createElement('tr');
        for (let dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++) {
            const day = firstDayOfMonth.clone().add(week * 7 + dayOfWeek, 'days');
            const dayCell = document.createElement('td');
            dayCell.textContent = day.format('D');
            dayCell.classList.add('day-cell');
            if (day.isSame(today, 'day')) {
                dayCell.classList.add('current-day');
            }
            if (day.month() !== currentMonth.month()) {
                dayCell.classList.add('other-month-day');
            } else if (!day.isSame(today, 'day')) {
                dayCell.classList.add('current-month-day');
            }
            weekRow.appendChild(dayCell);
        }
        monthlyCalendarBody.appendChild(weekRow);
    }
}

function updateCurrentMonthText() {
    document.getElementById('current-month').textContent = currentMonth.format('dddd, D MMMM YYYY');
}

document.getElementById('prev-month').addEventListener('click', () => {
    currentMonth.subtract(1, 'month');
    renderMonthlyCalendar();
    updateCurrentMonthText();
});

document.getElementById('next-month').addEventListener('click', () => {
    currentMonth.add(1, 'month');
    renderMonthlyCalendar();
    updateCurrentMonthText();
});

renderMonthlyCalendar();
updateCurrentMonthText();