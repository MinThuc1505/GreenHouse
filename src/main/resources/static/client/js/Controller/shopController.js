appClient.controller('shopController', function ($scope, $http, urlShopClient, $cookies, UserService, CartService) {
    let host = urlShopClient;
    var username = UserService.getCookiesUsername();


    // Function Init
    $scope.init = function () {
        $scope.sessionUsername = $cookies.get('username');
        var data = localStorage.getItem('category');

        if (data) {
            var url = `${host}/filter?category=${data}`;
        } else {
            var url = `${host}`;
        }

        $http.get(url).then(resp => {
            $scope.products = resp.data.products;
            $scope.categories = resp.data.categories;
            console.log(url + "=============" + $scope.products);
        }).catch(Error => {
            console.log("Error: ", Error);
        })
    }

    $scope.loadAllProducts = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.products = resp.data.products;
            $scope.categories = resp.data.categories;
        }).catch(Error => {
            console.log("Error: ", Error);
        })
    }

    // Function submit filter
    $scope.submitFilter = function () {
        var selectedPrice = $scope.selectedPrice;
        var selectedCategory = $scope.selectedCategory;

        if ((!selectedPrice || selectedPrice === "") && (!selectedCategory || selectedCategory === "")) {
            $scope.loadAllProducts();
        } else {
            if (!selectedPrice || selectedPrice === "") {
                var url = `${host}/filter?category=${selectedCategory.id}`;
            } else if (!selectedCategory || selectedCategory === "") {
                var url = `${host}/filter?price=${selectedPrice}`;
            } else {
                var url = `${host}/filter?category=${selectedCategory.id}&price=${selectedPrice}`;
            }
            // Tiếp tục gọi API và xử lý dữ liệu khi đã chọn đủ giá trị
            $http.get(url).then(resp => {
                $scope.products = resp.data.products;
                console.log(JSON.stringify($scope.products) + " loaded");
            }).catch(Error => {
                console.log("Error: ", Error);
            });
        }
    };

    $scope.init();
})