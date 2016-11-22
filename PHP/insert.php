<?php

	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	$host="mysql2772int.cp.blacknight.com";
	$uname="u1429550_sweng";
	$pwd="sw3ngproject?";
	$db="db1429550_swengproject";

	$con = mysqli_connect($host,$uname,$pwd,$db) or die("connection failed");

	$type = $_POST["type"];

	switch(type){
		case 1: add_project();
		break;
		case 2: add_individuals();
		break;
		case 3: attach_individuals();
		break;
		case 4: insert_object();
		break;
		case 5: assign_object();
		break;
		case 6: date_objects_list();
		break;
		case 7: other_objects_list();
		break;
		case 8: find_object();
		break;
	}

	function add_project(){
		$projectName = $_POST["NAME"];															//Abstract Data from POST
		$endDate = $_POST["END_DATE"]
		$num = $_POST["INDIVIDUALS_NUM"]
		$indvs = array();
		for($i=0;$i<$num;$i++){
			$indivs[$k] = $_POST["INDIVIDUALS" . $num];
		}
		//Create new table under project name to store individuals
		$sql = "CREATE TABLE $projectName (Member VARCHAR(60) NOT NULL)";
		$ret = mysqli_query($con,$sql);
		if($ret == false){
			echo 'Error';
		}
	  //Attach individuals to each project
		for($i=0;$i<$num;$i++){
			$sql = "INSERT INTO $projectName Member VALUES '$indivs[$i]'";
			$ret = mysqli_query($con,$sql);
			if($ret == false){
				echo 'Error';
			}
			//
		}
		// ADD the new project into the database
		$sql = "INSERT INTO $projects_database (Name, EndDate) VALUES ('$projectName', '$endDate') ";
		$ret = mysqli_query($con,$sql);
		if($ret == false){
			echo 'Error';
		}



	}
	function add_individuals(){

	}
	function attach_individuals(){

	}
	function insert_object(){

	}
	function assign_object(){

	}
	function date_objects_list(){

	}
	function other_objects_list(){

	}
	function find_object(){

	}


	mysql_close($con);
?>
