//用户安全管理控制器
app.controller('settingSafeController', function($scope,$controller,baseService) {

    $controller('indexController',{$scope:$scope});

    $scope.user = {username:'',password:''};

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