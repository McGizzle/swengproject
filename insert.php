<?php

	ini_set('display_errors', 1); 
	error_reporting(E_ALL);

	$host="mysql2772int.cp.blacknight.com"; 
	$uname="u1429550_sweng";
	$pwd="sw3ngproject?";
	$db="db1429550_swengproject";

	$con = mysqli_connect($host,$uname,$pwd,$db) or die("connection failed");

	//Querys go here

	mysql_close($con);
?>