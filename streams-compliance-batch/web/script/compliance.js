var lithium = angular.module('lithium',['ngRoute','ngProgress','ngGrid']);

lithium.config(function ($routeProvider, ngProgressProvider){
	// Default color is firebrick
       ngProgressProvider.setColor('firebrick');
    // Default height is 2px
       ngProgressProvider.setHeight('2px');

	$routeProvider
		.when('/client',
			{
				controller: 'SimpleController',
				templateUrl: 'html/client.html'
			})
		.when('/events',
			{
				controller: 'SimpleController',
				templateUrl: 'html/events.html'
			})
		.otherwise(
			{
				redirectTo: '/'
			}
		);
});

lithium.controller ('SimpleController' , function($scope, $http, $timeout, ngProgress){

	$timeout(function(){
            ngProgress.complete();
            $scope.show = true;
    }, 2000);

	$scope.restCall = function(){
		ngProgress.start();
	   $http({
	        url: 'http://localhost:7070/compliance/v1/id',
	        method: 'GET',
	        params: {
	            start: 1,
	            end: 1000
	        }
	    }).success(function (data, status, headers, config) {
	    	ngProgress.complete();
	        $scope.events = data;
	        $scope.numberOfEvents = data.length;
	       	$scope.iter = 0;
	        console.log("Success: Status="+status );

	    }).error(function(data, status, headers, config) {
	    	ngProgress.reset();
	        $scope.exception = data;
	        $scope.numberOfEvents = -1;
	        console.log("Exception: "+data);
	    });
	};

	$scope.gridOptions = {
        data: 'events',
        useExternalSorting: true
    };

	$scope.customers = [
		{name:'Vijay', city:'Sunnyvale', Drive: 'Jeep'},
		{name:'Mohan', city:'Boston', Drive: 'Civic'},
		{name:'Rao', city:'Seattle', Drive: 'Toyota'},
		{name:'Doc', city:'San Ramon', Drive: 'Merc'},
		{name:'Pal', city:'San Clemente', Drive: 'Honda'}
	];
	$scope.addCustomer = function(){
		$scope.customers.push(
			{
				name: $scope.newCustomer.name, city: $scope.newCustomer.city
			});
	};



});
