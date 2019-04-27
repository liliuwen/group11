// 定义购物车的控制器
app.controller('cartController', function ($scope,$controller, baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});

    $scope.selected = [] ;//定义一个数组
    $scope.opc = {};
    // 查询购物车
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;
            // 定义json对象封装统计的数据
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};
            $scope.selected = [];
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

    $scope.payData = function () {
        $scope.totalEntity.totalNum = 0;
        $scope.totalEntity.totalMoney = 0;
        for(var i = 0 ; i < $scope.selected.length ; i++){
            $scope.totalEntity.totalNum += $scope.selected[i].num;
            $scope.totalEntity.totalMoney += $scope.selected[i].totalFee;
        }
    };


    //全选方法，并将所有的id一并传入selected数组中
    $scope.all = function($event,data){
        var checkbox = $event.target ;
        var checked = checkbox.checked ;
        if(checked){
            $scope.seller = true;
            for(var i = 0;i < data.length ; i++){
                $scope.opc[data[i].sellerId] = true ;
                for(var j = 0;j < data[i].orderItems.length; j++){
                    if($scope.selected.indexOf(data[i].orderItems[j])>=0){//判断数组中是否重复存在
                        continue;
                    }else{
                        $scope.selected.push(data[i].orderItems[j]);
                    }
                }
            }
        }
        else{
            for(var i = 0;i < data.length ; i++){
                $scope.opc[data[i].sellerId] = false ;
            }
            $scope.seller = false;
            $scope.selected=[];
        }
        $scope.payData();
        console.log($scope.selected);
    };


    //商家更新selected
    $scope.updateSelection = function($event,cart){
        var checkbox = $event.target;
        var checked = checkbox.checked ;
        if(checked){
            $scope.opc[cart.sellerId] = true ;
            for(var i = 0; i < cart.orderItems.length ; i++){
                if($scope.selected.indexOf(cart.orderItems[i]) >= 0){
                    continue;
                }else {
                    $scope.selected.push(cart.orderItems[i]);
                }
            }
        }else{
            $scope.opc[cart.sellerId] = false ;
            for(var i = 0; i < cart.orderItems.length; i++){
                    var idx = $scope.selected.indexOf(cart.orderItems[i]);
                    $scope.selected.splice(idx,1);
                }
            }
        $scope.payData();
        console.log($scope.selected);
        console.log($scope.opc);
    };

    //单选更新selected
    $scope.oneSelection = function ($event,orderItem) {
        var checkbox = $event.target;
        var checked = checkbox.checked;
        if (checked) {
            if($scope.selected.indexOf(orderItem) < 0){
                $scope.selected.push(orderItem);
            }else {
                return;
            }
        } else {
            var idx = $scope.selected.indexOf(orderItem);
            $scope.selected.splice(idx, 1);
        }
        $scope.payData();
        console.log($scope.selected);
    };

    //批量删除
    $scope.seletedDelete = function(){
        if($scope.selected.length == 0){
            alert("请选择要删除的商品")
        }else {
            if(confirm("确认删除？")){
                for(var i = 0 ; i < $scope.selected.length ; i++){
                    var num = -($scope.selected[i].num);
                    $scope.addCart($scope.selected[i].itemId,num);
                }
            }
        }
    };

    $scope.payHtml = function () {
        if($scope.selected.length == 0){
            alert("请钩选要购买的商品");
        }else {
            location.href = "/order/getOrderInfo.html";
        }
    }

    $scope.findSeleted = function () {
        baseService.send("/cart/findSeleted",$scope.selected).then(function (response) {
            $scope.carts = response.data;
        });
    };
});