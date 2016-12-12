	<?php
	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	$host="mysql2772int.cp.blacknight.com";
	$uname="u1429550_sweng";
	$pwd="sw3ngproject?";
	$db="db1429550_swengproject";
	//
	// $OBJECT_NOT_FOUND = "0";
	// $SUCCESS_RESPONSE = "1";
	// $OBJECT_FOUND = "2";
	// $LIST_PROJECTS = "3";
	// $LIST_PERSON = "4";
	// $DUPLICATE=  "5";


	$con = mysqli_connect($host,$uname,$pwd,$db) or die(mysqli_error($con));
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
		case 3: search_object($con);
		break;
		case 4: add_object($con);
		break;
		case 5: get_list($con);
		break;
		case 6: attach_project_person($con);
		break;
		case 7:
		break;
		case 8:
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
		$sql = "INSERT INTO Project (ProjectName, EndDate) VALUES ('$projectName', '$endDate') ";
		$ret = mysqli_query($con,$sql)  or die(mysqli_error($con));

	  //Attach individuals to each projects
		for($i=0;$i<$num;$i++){
		  check_person_exists($con,$indvs[i]);
			$sql = "INSERT INTO ProjectGroup (ProjectName,PersonName) VALUES ('$projectName','$indvs[$i]')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
		echo "1" . "#";
		mysqli_close($con);

	}

	function add_individuals($con){
		$name = $_POST["NAME"];
		$num = $_POST["TEAM_NUM"];
		$projects = array();
		for($i=0;$i<$num;$i++){
			$projects[$i] = $_POST["TEAM".$i];
		}

		$sql = "INSERT INTO Person (PersonName) VALUES ('$name')";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));

		for($i=0;$i<$num;$i++){
			check_project_exists($con,$projects[i]);
			$sql = "INSERT INTO ProjectGroup (ProjectName,PersonName) VALUES ('$projects[$i]','$name')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
		echo "1" . "#";
		mysqli_close($con);
	}


	function check_person_exists($con,$person_name){
		$sql = "SELECT * FROM Person WHERE PersonName = '$person_name'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			$sql = "INSERT INTO Person (PersonName) VALUES ('$person_name')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
	}
	function check_project_exists($con,$project_name){
		$sql = "SELECT * FROM Project WHERE ProjectName = '$project_name'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			$sql = "INSERT INTO Project (ProjectName) VALUES ('$project_name')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
	}

	function attach_project_person($con){
		$project_name = $_POST["PROJECT_NAME"];
		$person_name = $_POST["PERSON_NAME"];

		$sql = "SELECT * FROM Project WHERE ProjectName = '$project_name'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		$sql = "SELECT * FROM Person WHERE PersonName = '$person_name'";
		$ret2 = mysqli_query($con,$sql) or die(mysqli_error($con));

		if (mysqli_num_rows($ret) == 0 || mysqli_num_rows($ret2) == 0) {
			echo "0" . "#" . "Person or Project does not exist in system";
		} else {
			$sql = "INSERT INTO ProjectGroup (ProjectName, PersonName) VALUES ('$project_name','$person_name')";
			mysqli_query($con,$sql) or die(mysqli_error($con));
			echo "1" . "#";
		}
	}

	function search_object($con){
		$barcode = $_POST['BARCODE_INFO'];
		$sql = "SELECT * FROM Object WHERE Barcode = ('$barcode')";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));

		if (mysqli_num_rows($ret) == 0) {
			echo "3" . "#" . $barcode;
		} else {
			echo "2" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[0] . "#" . $row[1] . "#" . $row[2] . "#" .
				$row[3] . "#" . $row[4] . "#" . $row[5] . "#"  . $row[6] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}


	function add_object($con){
		$date = $_POST['DATE'];//not used
		$object_name = $_POST['OBJECT_NAME']; //not used
		$barcode = $_POST['BARCODE'];

		$project_name = NULL;
		if (isset($_POST['PROJECT_NAME'])) {
			$project_name = $_POST['PROJECT_NAME'];
			check_project_exists($con,$project_name);
		}

		$person_name = NULL;
		if (isset($_POST['PERSON_NAME'])) {
			$person_name = $_POST['PERSON_NAME'];
			check_person_exists($con,$person_name);
		}

		$broken = $_POST['BROKEN'];

		$sql = "INSERT INTO Object (Barcode, PersonName, ProjectName, ObjectName, Broken, EndDate)
												VALUES ('$barcode', '$person_name', '$project_name' , '$object_name', '$broken','$date')";
		$ret = mysqli_query($con,$sql)  or die(mysqli_error($con));
		echo "1" . "#";
		mysqli_close($con);
	}

	function get_list($con){
		$list_type = $_POST["LIST_TYPE"];
		if($list_type == "BROKEN"){
			broken_objects_list($con);
		}
		else if($list_type == "ATTACHED"){
			attached_objects_list($con);
		}
		else if($list_type == "RECLAIMED"){
			reclaimed_objects_list($con);
		}
		else{
			echo "0" . "#" . "Unavailable List type selected";
		}
	}

	function reclaimed_objects_list($con){
		$date = $_POST["DATE"];
		$sql = "SELECT * FROM Object o LEFT OUTER JOIN Project p ON o.Project = p.Name WHERE p.EndDate < '$date' OR o.Project IS NULL";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "0" . "#" . "No Objects to be returned by this date.";
		} else {
			echo "2" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[0] . "#" . $row[1] . "#" . $row[2] . "#" .
				$row[3] . "#" . $row[4] . "#" . $row[5] . "#"  . $row[6] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}

	function broken_objects_list($con){
		$sql = "SELECT * FROM Object o WHERE o.Broken = 1";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "3" . "#" . "No broken Objects found that must be returned by specified date.";
		} else {
			echo "2" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[0] . "#" . $row[1] . "#" . $row[2] . "#" .
				$row[3] . "#" . $row[4] . "#" . $row[5] . "#"  . $row[6] . "#";
			}
		echo "!" . "#";
		}
		mysqli_close($con);
	}

	function attached_objects_list($con) {
		$date = $_POST["DATE"];
		$sql = "SELECT * FROM Object WHERE Object.ProjectName IN (SELECT ProjectName FROM Project WHERE Project.EndDate > '$date');";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "3" . "#" . "No Objects attahced to this project/person that must be returned by the specified date.";
		} else {
			echo "2" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[0] . "#" . $row[1] . "#" . $row[2] . "#" .
				$row[3] . "#" . $row[4] . "#" . $row[5] . "#"  . $row[6] . "#";

			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}

?>
