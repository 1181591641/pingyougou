    app.controller("baseController",function($scope){
    	
    	// 引入分页组件配置
    	$scope.paginationConf = {
    		currentPage: 1,  // 当前页
    		totalItems: 10,  // 总记录数
    		itemsPerPage: 10, // 每页显示记录数
    		perPageOptions: [10, 20, 30, 40, 50], // 下列列表
    		onChange: function(){
    			// 下列列表变化事件
    					$scope.reloadList();//刷新
    		}
    	};
    	
  
      	// 刷新列表，因为很多地方都需要调用，所以将其提取出来

    	$scope.reloadList=function(){
    		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    	};
    	
    	
  
    	
    	
    	$scope.selectIds=[];//用户勾选的ID集合 
    	//用户勾选复选框 
    	$scope.updateSelection=function($event,id){
    		if($event.target.checked){
    			$scope.selectIds.push(id);//push向集合添加元素 					
    		}else{
    			var index= $scope.selectIds.indexOf(id);//查找值的 位置
    			$scope.selectIds.splice(index,1);//参数1：移除的位置 参数2：移除的个数  
    		}
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