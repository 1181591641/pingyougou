 //品牌控制层
    app.controller("brandController",function($scope,$http,brandService,$controller){
    
    	$controller('baseController',{$scope:$scope});//伪继承

    	// 查询品牌列表
    	$scope.findBrandAll=function(){
    		brandService.findBrandAll().success(result=>{
    			$scope.brandList=result;
    		});
    	};
    		
    	// 分页
    	$scope.findPage=function(page,size){
    		brandService.findPage(page,size).success(result=>{
    			// 分页查询数据
    			$scope.brandList=result.rows;
    			// 更新总记录数
    			$scope.paginationConf.totalItems=result.total;
    		});		
    	};
    	
  
    	
      //判断用户名是否重复
        $scope.Names=function(){	
        	brandService.Names($scope.entity).success(
        			function(response){
        				if(response.success){
        					 alert(response.message);
        					 $('#myButton').prop('disabled', true); // 按钮灰掉，且不可点击。
        				}else{
        					 $('#myButton').prop('disabled', false); // 按钮灰掉，且不可点击。
        				}			
        			}		
        		);
        	}
 
	//新增
	$scope.save=function(){	
		var Object=null
		if($scope.entity.id!=null)
			{
			Object=brandService.update($scope.entity);
			}
		else
			{
			Object=brandService.add($scope.entity);
			}
	
		Object.success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新
			
				}else{
					alert(response.message);
				}				
			}		
		);
	}
    
	//根据id查询
	$scope.findOne=function(id){
		brandService.findOne(id).success(
				function(response)
				{
					$scope.entity=response;
				}
		);
	}
    

	
	
	$scope.dele=function(){
		if(confirm('确定要删除吗？')){
		 brandService.dele(	$scope.selectIds).success(
     			function(response){
     				if(response.success){
     					$scope.reloadList();//刷新
     				}else{
     					alert(response.message);
     				}			
     			}		
     		);
		}
	}
	
	

	$scope.searchEntity={};
	//条件查询 
	$scope.search=function(page,size){
		brandService.search(page,size, $scope.searchEntity).success(
			function(response){
				$scope.brandList=response.rows;//显示当前页数据 	
				$scope.paginationConf.totalItems=response.total;//更新总记录数 
			}		
		);	
		
	}
	$scope.jsonToString=function(jsonString,key){
		
		var json= JSON.parse(jsonString);
		var value="";
		
		for(var i=0;i<json.length;i++){
			if(i>0){
				value+=",";
			}			
			value +=json[i][key];			
		}
				
		return value;
	}
	
    });