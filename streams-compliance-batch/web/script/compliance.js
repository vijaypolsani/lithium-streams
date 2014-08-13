var lithium = angular.module('lithium',['ngRoute','ngProgress','ngGrid']);

lithium.factory("simpleFactory", function(){
		var factory = {};
		var customers = [
			{name:'JPMorgan', city:'Seattle', Drive: 'Stocks'},
			{name:'Goldman', city:'New York', Drive: 'Bonds'},
			{name:'Barclay', city:'San Francisco', Drive: 'Currencies'},
			{name:'Jeffries', city:'Chicago', Drive: 'Commodities'},
			{name:'StateStreet', city:'Boston', Drive: 'ETF'}
		];

		factory.getCustomers = function(){
			return customers;
		};
		return factory;
});

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
lithium.directive('myRepeatDirective', function() {
	  return function($scope, element, attrs) {
	    angular.element(element).css('color','black');	    
	    var eventJson = angular.fromJson($scope.event);
	    $scope.formatVersion = eventJson.formatVersion;
	    $scope.published = eventJson.published;
	    $scope.source = eventJson.generator.source;
	    $scope.eventId = eventJson.generator.eventId;
	    $scope.service = eventJson.provider.service;
	    $scope.version = eventJson.provider.version;
	    $scope.uid = eventJson.actor.uid;
	    $scope.login = eventJson.actor.login;
	    $scope.registrationStatus = eventJson.actor.registrationStatus;
	    $scope.email = eventJson.actor.email;
	    $scope.type = eventJson.actor.type;
	    $scope.registrationTime = eventJson.actor.registrationTime;
	    $scope.verb = eventJson.verb;
	    $scope.type = eventJson.target.type;
	    $scope.conversationType = eventJson.target.conversationType;
	    $scope.objectType = eventJson.streamObject.objectType;
	    $scope.id = eventJson.streamObject.id;
	    $scope.displayName = eventJson.streamObject.displayName;
	    $scope.content = eventJson.streamObject.content;
	    $scope.visibility = eventJson.streamObject.visibility;
	    $scope.added = eventJson.streamObject.added;
	    $scope.postTime = eventJson.streamObject.postTime;
	    $scope.isTopic = eventJson.streamObject.isTopic;
	  };
	})
lithium.directive('myJsonDirective', function() {
	  return function($scope, element, attrs) {
	    var parsedData = JSON.parse($scope.event);
	    $scope.eventJson = JSON.stringify(parsedData, null, 2);
	    console.log("Event in myJsonDirective JSON: "+ $scope.eventJson );
	  };
	})


lithium.controller ('SimpleController' , function($scope, $http, $timeout, simpleFactory, ngProgress){


	$timeout(function(){
            ngProgress.complete();
            $scope.show = true;
    }, 2000);

	$scope.restCall = function(){
		ngProgress.start();
		console.log("Rest call parameters Start: "+$scope.start)
		console.log("Rest call parameters End: "+$scope.end)
	   $http({
	        //url: 'http://localhost:7070/compliance/v1/all',
	        url: 'http://localhost:7070/compliance/v1/id',
	        method: 'GET',
	        params: {
	            start: $scope.start,
	            end: $scope.end
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

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formatData = function() {
    	console.log(angular.fromJson($scope.event));
    	console.log(angular.fromJson($scope.customers));
    	var eventJson = angular.fromJson($scope.customers);
    	for (i = 0; i < eventJson.length; i++) { 
    		console.log("Event Json format: "+eventJson[i].name);
		}
    	$scope.published = eventJson.name;

    }
    
    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    //Factory injected into customers at Runtime
	$scope.customers = simpleFactory.getCustomers;

	$scope.addCustomer = function(){
		$scope.customers.push(
			{
				name: $scope.newCustomer.name, city: $scope.newCustomer.city
			});
	};



});
