appClient.controller('menuController', function ($scope, $http, urlMenuClient) {
    let host = urlMenuClient;

    $scope.setCategory = function (category) {
        localStorage.setItem('category', category);
        window.location.href = "/client/shop"; // reload page to update menu items based on selected category
    }

    $scope.init = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.categories = resp.data.categories;
            var numItemsPerColumn = 8; // Số lượng phần tử trong mỗi cột
            var numColumns = Math.ceil($scope.categories.length / numItemsPerColumn);
    
            // Tạo menuItems và chia mảng categories vào các menuItems tương ứng
            $scope.menuItems = [];
            for (var i = 0; i < numColumns; i++) {
                var startIndex = i * numItemsPerColumn;
                var endIndex = startIndex + numItemsPerColumn;
                $scope.menuItems.push($scope.categories.slice(startIndex, endIndex));
            }
    
        }).catch(Error => {
            console.log("Error: ", Error);
        })
    }

    $scope.init();
})
