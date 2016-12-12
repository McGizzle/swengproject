	<?php
	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	$host="mysql2772int.cp.blacknight.com";
	$uname="u1429550_sweng";
	$pwd="sw3ngproject?";
	$db="db1429550_swengproject";
	$con = mysqli_connect($host,$uname,$pwd,$db) or die(mysqli_error($con));

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
		case 7: break_object($con);
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
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}


	function add_object($con){
		$object_name = $_POST['OBJECT_NAME'];
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

		check_person_in_project($con, $project_name, $person_name);
		$broken = $_POST['BROKEN'];

		$sql = "INSERT INTO Object (Barcode, PersonName, ProjectName, ObjectName, Broken)" .
												" VALUES ('$barcode', '$person_name', '$project_name' , '$object_name', '$broken')";
		$ret = mysqli_query($con,$sql)  or die(mysqli_error($con));
		echo "1" . "#";
		mysqli_close($con);
	}

	function break_object($con) {
		$object_id = $_POST['OBJECT_ID'];
		$sql = "UPDATE Object SET Broken = 1 WHERE ObjectID = '$object_id'";
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
		$sql = "SELECT * FROM Object WHERE Object.ProjectName NOT IN (SELECT ProjectName FROM Project WHERE Project.EndDate > '$date')";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "0" . "#" . "No Objects to be returned by this date.";
		} else {
			echo "4" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#";
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
			echo "4" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#";
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
			echo "4" . "#" . "No Objects attahced to this project/person that must be returned by the specified date.";
		} else {
			echo "2" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}

	function check_person_in_project($con, $project, $person) {
		$sql = "SELECT * FROM ProjectGroup WHERE PersonName = '$person' AND ProjectName = '$project'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "0" . "#" . "Persons/project are not attahced.";

		}
	}
?>
