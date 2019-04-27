/** 定义控制器层 */
app.controller('indexController', function ($scope, baseService,$controller,$location,$interval,$http) {

   // $controller('baseController', {$scope : $scope});
    $controller('specificationController', {$scope : $scope});

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

    $scope.showHeadAndNickname = function () {
        baseService.sendGet("/user/info/getHead?username="+$scope.loginName)
            .then(function (resp) {
                if (resp.data) {
                    $scope.headPic = resp.data.headPic;
                    $scope.nickName = resp.data.nickName;
                }
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


    /**
    * findAll
    * */
  /*  $scope.findAll = function () {
        baseService.sendGet("/orderItem/findAll").then(function (response) {
            $scope.findList = response.data;
        })
    }
*/

    // 生成微信支付二维码
    $scope.genPayCode = function () {
        baseService.sendGet("/orderItem/genPayCode").then(function(response){
            // 获取响应数据  response.data : {outTradeNo: '', money : 1, codeUrl : ''}
            // 获取交易订单号
            $scope.outTradeNo = response.data.outTradeNo;
            // 获取支付金额
            $scope.money = (response.data.totalFee / 100).toFixed(2);
            // 获取支付URL
            $scope.codeUrl = response.data.codeUrl;

            // 支付二维码
            document.getElementById("qrcode").src = "/barcode?url=" + $scope.codeUrl;

            // 开启定时器
            // 第一个参数：定时需要回调的函数
            // 第二个参数：间隔的时间毫秒数 3秒
            // 第三个参数：总调用次数 100
            var timer = $interval(function(){

                // 发送异步请求，获取支付状态
                baseService.sendGet("/order/queryPayStatus?outTradeNo="
                    + $scope.outTradeNo).then(function(response){
                    // 获取响应数据: response.data: {status : 1|2|3} 1:支付成功 2：未支付 3:支付失败
                    if (response.data.status == 1){// 支付成功
                        // 取消定时器
                        $interval.cancel(timer);
                        // 跳转到支付成功的页面
                        location.href = "/order/paysuccess.html?money=" + $scope.money;
                    }
                    if (response.data.status == 3){
                        // 取消定时器
                        $interval.cancel(timer);
                        // 跳转到支付失败的页面
                        location.href = "/order/payfail.html";
                    }
                });
            }, 3000, 100);

            // 总调用次数结束后，需要调用的函数
            timer.then(function(){
                // 关闭订单
                $scope.tip = "二维码已过期，刷新页面重新获取二维码。";
            });

        });
    };

    $scope.changId=function (id) {
        return BigInt(id);
    }

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