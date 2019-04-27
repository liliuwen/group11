/** 定义控制器层 */
app.controller('indexController', function ($scope, baseService,$controller,$location,$interval,$http) {

   // $controller('baseController', {$scope : $scope});
    //$controller('specificationController', {$scope : $scope});

    $scope.uname = "";
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
                $scope.getUserInfo();
                $scope.showHeadAndNickname();
                $scope.getName();
                $scope.uname = $scope.loginName;
            });
    };


    // 分页查询品牌
    $scope.search = function (page, rows) {
        // 发送异步请求
        baseService.findByPage("/orderItem/findByPage", page, rows, $scope.searchEntity)
            .then(function(response){
                // 获取响应数据
                // response.data: {total: 1000, rows : [{},{}]}
                // 获取品牌数据
                $scope.dataList = response.data.rows;
                // 获取总记录数
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    $scope.showHeadAndNickname = function () {
        baseService.sendGet("/user/info/getHead?username="+$scope.loginName)
            .then(function (resp) {
                if (resp.data) {
                    $scope.headPic = resp.data.headPic;
                    $scope.nickName = resp.data.nickName;
                }
            });
    };

    $scope.changId=function (id) {
        return BigInt(id);
    };

    // 秒杀下单
    $scope.submitOrder = function () {
        alert($scope.orderId);
        // 判断用户是否登录
        if ($scope.loginName){ // 登录用户
          /*  baseService.sendGet("/order/submitOrder?id="
                + $scope.entity.id).then(function(response){
                // 获取响应数据
                if (response.data){
                    // 秒杀下单成功
                    location.href = "/order/pay.html";
                }else{
                    alert("秒杀下单失败！");
                }
            });*/
            location.href = "/order/pay.html";
        }else{
            // 未登录，跳转到单点登录系统
            location.href = "http://sso.pinyougou.com/login?service=" + $scope.redirectUrl;
        }
    };

    /** 获取登录用户名 */
    $scope.loadUsername = function(){
        /** 定义重定向URL */
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        /** 获取用户登录信息 */
        $http.get("/user/showName").then(function(response){
            $scope.loginName = response.data.loginName;
        });
    };
});