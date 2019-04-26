//用户安全管理控制器
app.controller('settingSafeController', function($scope,$controller,baseService) {

    $scope.user = {username:'',password:''};

    /** 定义获取登录用户名方法 */
    $scope.showName = function(){

        //定义重定向地址
        $scope.redirectUrl = window.encodeURIComponent(location.href);
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };

    //修改密码
    $scope.updatePwd = function () {
        $scope.user.username = $scope.loginName;
        $scope.user = JSON.stringify($scope.user);
        alert($scope.user);
        baseService.sendPost("/user/setting/updatePwd",$scope.user).then(function (response) {
            if (response.data){
                alert("修改成功！");
                location.href = "http://sso.pinyougou.com/logout?service=" + $scope.redirectUrl;
            }else {
                alert("修改失败！");
                location.reload(true);
            }
        });
    };

});