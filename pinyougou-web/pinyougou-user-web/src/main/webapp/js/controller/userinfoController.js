app.controller('userinfoController', function($scope,$controller,baseService,$filter){

    $controller('indexController',{$scope:$scope});

    //初始化值
    $scope.userInfo = { };

    //选择职业
    $scope.profession = {
        id:0,
        opt:[{id:1,name:'程序员'},{id:2,name:'产品经理'},{id:3,name:'UI设计师'}]
    };

    //选择所在地
    $scope.Local1 = {id:0,opt:[]};
    $scope.Local2 = {d:0,opt:[]};
    $scope.Local3 = {id:0,opt:[]};

    //回显所在地的ID
    $scope.LocalJson = {
        province :0,
        city:0,
        area:0
    };

    //获取同户信息
    $scope.getUserInfo = function () {
        baseService.sendGet("http://user.pinyougou.com/user/info/get?username="+$scope.loginName)
            .then(function (resp) {
                if (resp.data){
                    //alert(JSON.stringify(resp.data));
                    $scope.userInfo = resp.data;
                    // 格式化日期
                    $scope.userInfo.birthday =  $filter('date')($scope.userInfo.birthday, "yyyy-MM-dd");
                }
            });
    };

    $scope.$watch('userInfo',function (newVal, oldVal) {
        //回显所在地
        $scope.LocalJson = JSON.parse(newVal.address);
        $scope.Local1.id = $scope.LocalJson.province;
        $scope.Local2.id = $scope.LocalJson.city;
        $scope.Local3.id = $scope.LocalJson.area;
        //回显职业
        $scope.userInfo.profession = JSON.parse(newVal.profession);
    });

    //加载省份选项
    $scope.getProvince = function () {
       baseService.sendGet("http://user.pinyougou.com/user/info/getProvince")
           .then(function (resp) {
               if (resp.data) {
                   $scope.Local1.opt = resp.data;
               }
           }) ;
    };

    $scope.$watch('Local1.id',function (newVal, oldVal) {
        $scope.LocalJson.province = newVal;
        baseService.sendGet("http://user.pinyougou.com/user/info/getCitys?provinceId="+newVal)
            .then(function (resp) {
                if (resp.data) {
                    $scope.Local2.opt = resp.data;
                }
            });
    });

    $scope.$watch('Local2.id',function (newVal, oldVal) {
        $scope.LocalJson.city = newVal;
        baseService.sendGet("http://user.pinyougou.com/user/info/getAreas?cityId="+newVal)
            .then(function (resp) {
                if (resp.data) {
                    $scope.Local3.opt = resp.data;
                }
            });
    });

    $scope.$watch('Local3.id',function (newVal, oldVal) {
        $scope.LocalJson.area = newVal;
    });

});