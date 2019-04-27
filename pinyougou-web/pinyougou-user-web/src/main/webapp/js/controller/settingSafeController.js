//用户安全管理控制器
app.controller('settingSafeController', function($scope,$controller,baseService) {

    $controller('indexController',{$scope:$scope});

    //重定向
    $scope.redirectUrl = window.encodeURIComponent(location.href);
    //定义用户账户密码json对象
    $scope.user = {username : '',password:'',phone : ''};
    //定义两个验证码
    $scope.codes = {picCode:'',smsCode:''};

    $scope.getName = function () {
        $scope.user.username = $scope.loginName;
    };

    //修改密码
    $scope.updatePwd = function () {
        $scope.user = JSON.stringify($scope.user);
        alert($scope.user);
        baseService.sendPost("/user/setting/updatePwd",$scope.user).then(function (response) {
            if (response.data){
                alert("修改成功！");
                location.href = "http://sso.pinyougou.com/logout?service=" + $scope.redirectUrl ;
            }else {
                alert("修改失败！");
                location.reload(true);
            }
        });
    };

    //定义发送短信验证码方法
    $scope.getSmsCode = function () {
        baseService.sendGet("/user/sendSmsCode?phone="
            + $scope.user.phone)
            .then(function(response){
                alert(response.data ? "发送成功！" : "发送失败！");
            });
    };

    var picResult ="";
    //下一步按钮
    $scope.checkCodes = function(){
        // alert("----------");
        alert("======1111========");
        $scope.checkPicCode();
        alert("======2222========");
    };
    //图片验证码判断
    $scope.checkPicCode = function () {
        if ($scope.codes.picCode != "") {
            baseService.sendGet("/user/setting/checkPicCode?picCode=" + $scope.codes.picCode)
                .then(function (response) {
                    if (!response.data.result) {
                        picResult = JSON.stringify(response.data.result);

                        return picResult;
                    } else {
                        picResult = JSON.stringify(response.data.result);
                        return picResult;
                    }
                });
        }else {
            // alert("<font color='red'>验证码不能为空！</font>");
            // $scope.msgInfo.html("<font color='red'>验证码不能为空！</font>");
            return picResult = false;
        }
    };

    //验证码判断
    // $scope.checkSmsCode = function () {
    //     //获取用户输入的短信验证码
    //     if ($scope.codes.smsCode != "") {
    //         // alert($scope.user.username);
    //         baseService.sendPost("/user/setting/checkSmsCode?smsCode=" + smsCode ,$scope.user)
    //             .then(function (response) {
    //                 if (response.data) {
    //                     location.href(href = "home-setting-address-phone.html");
    //                     smsCode = '';
    //                     return true;
    //                 } else {
    //                     alert("验证码不正确！");
    //                     location.reload(true);
    //                     return false;
    //                 }
    //             })
    //     }else {
    //         $scope.msgInfo.html("<font color='red'>短信验证码不能为空！</font>");
    //     }
    // };

    //获取同户信息
    $scope.getUserInfo = function () {
        baseService.sendGet("http://user.pinyougou.com/user/info/get?username="+$scope.loginName)
            .then(function (resp) {
                if (resp.data){
                    //alert(JSON.stringify(resp.data));
                    $scope.user.phone = resp.data.phone;
                }
            });
    };


});