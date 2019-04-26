// 定义购物车的控制器
app.controller('cartController', function ($scope, $controller, baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});

    $scope.arrStr = [];
    var sellerCartsData = [];
    // 查询购物车
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;
            // 定义json对象封装统计的数据
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};
            $scope.payData();
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


    // $scope.selectOne = function ($event,itemId,num,totalFee) {
    //     var data = {itemId : "", num : "", totalFee : ""};
    //     if($event.target.checked){
    //         data.itemId = itemId;
    //         data.num = num;
    //         data.totalFee = totalFee;
    //         arrStr.push(data);
    //         alert(JSON.stringify(arrStr));
    //         $scope.totalEntity.totalNum += num;
    //         $scope.totalEntity.totalMoney += totalFee;
    //     }else {
    //         var idx = arrStr.indexOf(itemId);
    //         arrStr.splice(idx,1);
    //         $scope.payData();
    //         alert(JSON.stringify(arrStr));
    //     }
    // };

    $scope.payData = function () {
        $scope.totalEntity.totalNum = 0;
        $scope.totalEntity.totalMoney = 0;
        for(var i = 0 ; i < $scope.arrStr.length ; i++){
            $scope.totalEntity.totalNum += $scope.arrStr[i].num;
            $scope.totalEntity.totalMoney += $scope.arrStr[i].totalFee;
        }
    };

    $scope.selectCart = function ($event,sellerName) {

    };

//checkbox全选单选
    $scope.selected = [] ;//定义一个数组
    //全选方法，并将所有的id一并传入selected数组中
    $scope.all = function($event,data){
        var checkbox = $event.target ;
        var checked = checkbox.checked ;
        if(checked){
            $scope.x = true;
            $scope.y = true;
            for(var key in data){
                if($scope.selected.indexOf(data[key].sellerName)>=0){//判断数组中是否重复存在
                    continue;
                }else{
                    $scope.selected.push(data[key].sellerName);
                }
            }
            console.log($scope.selected);
            console.log($scope.arrStr);
        }
        else{
            $scope.x = false;
            $scope.y = false;
            $scope.selected=[];
        }
    };

    $scope.updateSelection = function($event,sellerName){ //单选更新selected
        var checkbox = $event.target;
        var checked = checkbox.checked ;
        if(checked){
            $scope.selected.push(sellerName);
            $scope.y = true;
        }else{
                var idx = $scope.selected.indexOf(sellerName) ;
                $scope.selected.splice(idx,1);
            $scope.y = false;
            }
        console.log($scope.selected);
        };



    $scope.oneSelection = function ($event,itemId,num,totalFee) {
        var checkbox = $event.target;
        var checked = checkbox.checked;
        if($scope.y == true){
            checked = checked;
        }
        if (checked) {
            var data = {itemId: "", num: "", totalFee: ""};
            data.itemId = itemId;
            data.num = num;
            data.totalFee = totalFee;
            $scope.arrStr.push(data);
            alert(JSON.stringify($scope.arrStr));
            $scope.totalEntity.totalNum += num;
            $scope.totalEntity.totalMoney += totalFee;
        } else {
            var idx = $scope.arrStr.indexOf(itemId);
            $scope.arrStr.splice(idx, 1);
            $scope.payData();
            alert(JSON.stringify($scope.arrStr));
        }
    };

    //批量删除
    $scope.seletedDelete = function(){
        if($scope.arrStr.length == 0){
            alert("请选择要删除的商品")
        }else {
            if(confirm("确认删除？")){
                for(var i = 0 ; i < $scope.arrStr.length ; i++){
                    var num = -($scope.arrStr[i].num);
                    $scope.addCart($scope.arrStr[i].itemId,num);
                }
            }
        }
    }
});