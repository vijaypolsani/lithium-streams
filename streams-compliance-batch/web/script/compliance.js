var lithium = angular.module('lithium',['ngRoute']);

lithium.controller ('SimpleController' , function($scope){
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


lithium.config(function ($routeProvider){
	$routeProvider
		.when('/index',
			{
				controller: 'SimpleController',
				templateUrl: 'home/index.html'
			})
		.when('/client',
			{
				controller: 'SimpleController',
				templateUrl: 'client/client.html'
			})
		.otherwise(
			{
				redirectTo: '/'
			}
		);
});