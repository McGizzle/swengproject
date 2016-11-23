<?php

	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	$host="mysql2772int.cp.blacknight.com";
	$uname="u1429550_sweng";
	$pwd="sw3ngproject?";
	$db="db1429550_swengproject";

	$con = mysqli_connect($host,$uname,$pwd,$db) or die("connection failed");
	//
	// foreach ($_POST as $key => $value) {
	//
	// 	 echo "Key = " . $key . " ";
	//
	// 	 echo "Value = " . $value;
	//  }


	$type = $_POST["TYPE"];

	switch($type){
		case 1: add_project($con);
		break;
		case 2: add_individuals($con);
		break;
		case 3: search_object();
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

	function add_project($con){
		$projectName = $_POST["NAME"];
		$endDate = $_POST["END_DATE"];
		$num = $_POST["INDIVIDUALS_NUM"];
		$indvs = array();
		for($i=0;$i<$num;$i++){
			$indvs[$i] = $_POST["INDIVIDUALS" . $i];
		}

		// ADD the new project into the database
		$sql = "INSERT INTO Project (Name, EndDate) VALUES ('$projectName', '$endDate') ";
		$ret = mysqli_query($con,$sql)  or die(mysqli_error($con));

	  //Attach individuals to each projects
		for($i=0;$i<$num;$i++){
			$sql = "INSERT INTO ProjectGroup (ProjectName,MemberName) VALUES ('$projectName','$indvs[$i]')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
		echo '1';
		mysqli_close($con);

	}
	function add_individuals($con){

		$name = $_POST["NAME"];
		$num = $_POST["TEAM_NUM"];
		$projects = array();
		for($i=0;$i<$num;$i++){
			$projects[$i] = $_POST["TEAM".$i];
		}
		$sql = "INSERT INTO Person (Name) VALUES ('$name')";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));

		for($i=0;$i<$num;$i++){
			$sql = "INSERT INTO ProjectGroup (ProjectName,MemberName) VALUES ('$projects[$i]','$name')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
		echo '1';
		mysqli_close($con);
	}
	function search_object(){

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


?>
