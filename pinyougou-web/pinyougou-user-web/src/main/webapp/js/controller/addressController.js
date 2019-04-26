app.controller('addressController',function ($scope,$controller,baseService) {
    //继承indexController
    $controller('indexController',{$scope:$scope});

    /**
     * 查询用户地址信息
     */
    $scope.findAddressByUser = function () {
        baseService.sendGet("/user/findAddressByUser",$scope.loginName)
            .then(function (response) {
                $scope.addressList = response.data;
            });
    };

    /**
     * 查询省份
     */
    $scope.findProvinces = function () {
        baseService.sendGet("/user/findProvinces")
            .then(function (response) {
                $scope.provinceList = response.data;
            })
    };


    /**
     * 监控省份ID是否发生变化
     */
    $scope.$watch('entity.provinceId',function (newValue,oldValue) {
        if(newValue) {
            //如果provinceId改变则根据这个新值查询市
            $scope.findCitiesByProvinceId(newValue);
        }else {
            $scope.cityList = [];
        }
    });

    /**
     * 监控地级市ID是否发生变化
     */
    $scope.$watch('entity.cityId',function (newValue,oldValue) {
        if(newValue) {
            $scope.findAreasByCityId(newValue);
        }else {
            $scope.areaList = [];
        }
    });

    /**
     *根据市查询区
     */
    $scope.findAreasByCityId = function (cityId) {
        baseService.sendGet("/user/findAreasByCityId?cityId="+cityId)
            .then(function (response) {
                $scope.areaList = response.data;
            });
    };

    /**
     *根据省份ID查询市
     */
    $scope.findCitiesByProvinceId = function (provinceId) {
        baseService.sendGet("/user/findCitiesByProvinceId?provinceId="+provinceId)
            .then(function (response) {
                $scope.cityList = response.data;
            });
    };

    /**
     * 地址编辑按钮点击事件方法
     */
    $scope.show = function (entity) {
        // 把entity转化成json字符串
        var jsonStr = JSON.stringify(entity);
        // 把json字符串转化成json对象
        $scope.entity = JSON.parse(jsonStr);
        //查询省份信息
        $scope.findProvinces();
    };
    /**
     * 新增或修改地址
     */
    $scope.entity = {};
    $scope.saveOrUpdate = function () {
        var url = "saveAddress";
        if($scope.entity.id) {
            url = "updateAddress"
        }
        baseService.sendPost("/user/"+url,$scope.entity)
            .then(function (response) {
                if(response.data) {
                    alert("操作成功!");
                    //重新查询地址信息
                    $scope.findAddressByUser();
                }else {
                    alert("地址添加失败!")
                }
            })
    };

    /**
     * 删除地址
     */
    $scope.deleteAddress = function (id,isDefault) {
        //判断是否为默认地址
        if(isDefault == "1") {
            alert("默认地址不能删除!");
        }else {//不是默认地址
            baseService.sendGet("/user/deleteAddress?id=" + id)
                .then(function (response) {
                    if(response.data) {
                        alert("地址删除成功!")
                        //重新查询地址列表
                        $scope.findAddressByUser();
                    }else {
                        alert("地址删除失败!")
                    }
                });
        }
    }
});