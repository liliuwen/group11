// 定义购物车的控制器
app.controller('cartController', function ($scope, $controller, baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});

    // 查询购物车
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;
            // 定义json对象封装统计的数据
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};
            // 迭代用户的购物车集合
            for (var i = 0; i < $scope.carts.length; i++){
                // 获取商家的购物车
                var cart = $scope.carts[i];
                // 迭代商家购物车中的商品
                for (var j = 0; j < cart.orderItems.length; j++){
                    // 获取购买的商品
                    var orderItem = cart.orderItems[j];
                    // 统计购买数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 统计总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee;
                }
            }
        });
    };

    // 添加商品到购物车
    $scope.addCart = function (itemId, num) {
        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
            // 获取响应数据
            if (response.data){
                // 重新查询购物车
                $scope.findCart();
            }
        });
    };

    $scope.changeNum = function (itemId,num) {
        if(num > 1){
            $scope.addCart(itemId,num);
        }else {
            alert("最低购买数量不能小于1");
            $scope.findCart();
        }
    };

    $scope.m = [];
    $scope.checked=[];
    //默认选择框未选中
    $scope.selectAll = false;

    $scope.changeAll = function () {
        if(selectAll == true){
            $scope.selectCart = true;
            $scope.checked=[];
            angular.forEach($scope.carts, function (cart, index) {
                $scope.checked.push(cart.id);
                $scope.m[cart.id] = true;
            })
        }else {
            $scope.selectCart = false;
            $scope.checked = [];
            $scope.m = [];
        }
        console.log($scope.checked);
    };

    $scope.sellerCart = function (cart) {
        angular.forEach($scope.m, function (i, id) {
            var index = $scope.checked.indexOf(id);
            if(i && index === -1){
                $scope.checked.push(id);
            }else if(!i && index !== -1){
                $scope.checked.splice(index,1);
            };
        });
        if($scope.carts.length === $scope.checked.length){
            $scope.selectAll = true;
        }else {
            $scope.selectAll = false;
        }
        console.log($scope.checked);
    };
});