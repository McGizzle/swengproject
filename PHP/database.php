	<?php
	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	$host="";
	$uname="";
	$pwd="";
	$db="";
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
		case 8: assign_object($con);
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
		  check_person_exists($con,$indvs[$i]);
		  if(check_project_group($con,$indvs[$i],$projectName)==0){
				$sql = "INSERT INTO ProjectGroup (ProjectName,PersonName) VALUES ('$projectName','$indvs[$i]')";
				$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
			}
		}
		echo "1" . "#" . "Project successfully added :)";
		mysqli_close($con);

	}

	function check_project_group($con,$person_name,$project_name){
			$sql = "SELECT * FROM ProjectGroup WHERE ProjectName = '$project_name' AND PersonName = '$person_name'";
			$ret = mysqli_query($con,$sql);
			if(mysqli_num_rows($ret) == 0){
				return 0;
			}
				return 1;
	}

	function add_individuals($con){
		$name = $_POST["NAME"];
		$num = $_POST["TEAM_NUM"];
		$projects = array();
		for($i=0;$i<$num;$i++){
			$projects[$i] = $_POST["TEAM".$i];
		}
		$sql = "SELECT * FROM Person WHERE PersonName = '$name'";
		$ret = mysqli_query($con,$sql);
		if (mysqli_num_rows($ret) != 0) {
			echo "0" . "#" . "A Person with the name ". $name ." already exists. Please choose another name.";
			die();
		}
		$sql = "INSERT INTO Person (PersonName) VALUES ('$name')";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));

		for($i=0;$i<$num;$i++){
			if(check_project_exists($con,$projects[$i])==0){
				echo "0" . "#" . "A project with the name ". $project_name ." does not exist :( Please create it first." .
				"Any Projects listed after it have not been attached to the individual due to this error.";
				die();
			}
			$sql = "INSERT INTO ProjectGroup (ProjectName,PersonName) VALUES ('$projects[$i]','$name')";
			$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		}
		echo "1" . "#" . "Person successfully added :)";
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
			return 0;
		}
		return 1;
	}

	function attach_project_person($con){
		$project_name = $_POST["PROJECT_NAME"];
		$person_name = $_POST["PERSON_NAME"];

		$sql = "SELECT * FROM Project WHERE ProjectName = '$project_name'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		$sql = "SELECT * FROM Person WHERE PersonName = '$person_name'";
		$ret2 = mysqli_query($con,$sql) or die(mysqli_error($con));

		if (mysqli_num_rows($ret) == 0 || mysqli_num_rows($ret2) == 0) {
			echo "0" . "#" . "Person or Project does not exist in system. Please add them first before attaching.";
		} else {
			$sql = "INSERT INTO ProjectGroup (ProjectName, PersonName) VALUES ('$project_name','$person_name')";
			mysqli_query($con,$sql) or die(mysqli_error($con));
			echo "1" . "#" . "Person and Project successfully attached :)";
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
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#" . $row[5] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}


	function add_object($con){
		$object_name = $_POST['OBJECT_NAME'];
		$barcode = $_POST['BARCODE'];

		$project_name = $_POST['PROJECT_NAME'];
		if($project_name == ""){
		}
		else{
			if (check_project_exists($con,$project_name) == 0){
				echo "0" . "#" . "A project with the name ". $project_name ." does not exist :( Please create it first." .
				"The Object has not been added";
				die();
			}
		}

		$person_name = $_POST['PERSON_NAME'];
		check_person_exists($con,$person_name);

		if($project_name != ""){
			if(check_person_in_project($con, $project_name, $person_name)==0){
					echo "0" . "#" . "Persons/project are not attahced.	The Object has not been added";
					die();
			}
		}
		$broken = $_POST['BROKEN'];

		$sql = "INSERT INTO Object (Barcode, PersonName, ProjectName, ObjectName, Broken)
												 VALUES ('$barcode', '$person_name', '$project_name' , '$object_name', '$broken')";
		$ret = mysqli_query($con,$sql)  or die(mysqli_error($con));
		echo "1" . "#" . "Object successfully added :) Barcode = " . $barcode;
		mysqli_close($con);
	}

	function break_object($con) {
		$object_id = $_POST['OBJECT_ID'];
		$sql = "UPDATE Object SET Broken = 1 WHERE ObjectID = '$object_id'";
		$ret = mysqli_query($con,$sql)  or die(mysqli_error($con));
		echo "1" . "#" . "The Object has been set as broken :(";
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
		$sql = "SELECT * FROM Object WHERE Object.ProjectName NOT IN (SELECT ProjectName FROM Project WHERE Project.EndDate > '$date') AND Object.Broken = 0";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "0" . "#" . "No Objects to be returned by this date.";
		} else {
			echo "4" . "#" . "RECLAIMED" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#" . $row[5] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}

	function broken_objects_list($con){
		$sql = "SELECT * FROM Object WHERE Object.Broken = 1";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			echo "0" . "#" . "No broken objects found.";
		} else {
			echo "4" . "#" . "BROKEN" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#" . $row[5] . "#";
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
			echo "0" . "#" . "No Objects attahced to this project/person that must be returned by the specified date.";
		} else {
			echo "4" . "#" . "ATTACHED" . "#";
			while ($row = mysqli_fetch_row($ret)){
				echo $row[2] . "#" . $row[3] . "#" . $row[4] . "#" . $row[1] . "#" . $row[0] . "#" . $row[5] . "#";
			}
			echo "!" . "#";
		}
		mysqli_close($con);
	}

	function check_assigned_person($con, $object_id) {
		$sql = "SELECT * FROM Object WHERE ObjectID = '$object_id' AND PersonName IS NOT NULL";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			return 0;
		}
		return 1;
	}

	function check_assigned_project($con, $object_id) {
		$sql = "SELECT * FROM Object WHERE ObjectID = '$object_id' AND ProjectName IS NOT NULL ";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			return 0;
		}
		return 1;
	}

	function get_person_from_object($con, $object_id) {
		$sql = "SELECT PersonName FROM Object WHERE ObjectID = '$object_id'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		$row = mysqli_fetch_row($ret);
		return $row[0];
	}

	function assign_object($con) {
		$object_id = $_POST['OBJECT_ID'];
		$assignedPerson = check_assigned_person($con, $object_id);
		$assignedProject = check_assigned_project($con, $object_id);


		if ($assignedPerson == 0) {

			$person = $_POST['PERSON_NAME'];
			check_person_exists($con, $person);

			$project = $_POST['PROJECT_NAME'];
			if ($project != "") {
				if (check_project_group($con, $person, $project) == 1) {
					$sql = "UPDATE Object SET PersonName = '$person', ProjectName = '$project' WHERE ObjectID = '$object_id'";
					$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
					echo "1" . "#" . "Object[" . $object_id . "] has been assigned to project '" . $project . "' and person '" . $person . "'";
				} else {
					echo "0" . "#'" . $person . "' is not assigned to project '" . $project . "'";
				}
			} else {
				$sql = "UPDATE Object SET PersonName = '$person' WHERE ObjectID = '$object_id'";
				$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
				echo "1" . "#" . "Object[" . $object_id . "] has been assigned to '" . $person . "'";
			}
		} else {
			$person = get_person_from_object($con, $object_id);
			if ($assignedProject == 1) {
				echo "0" . "#" . "Object[" . $object_id . "] is already assigned a person and project.";
			} else {
				if (check_project_group($con, $person, $project) == 1) {
					$sql = "UPDATE Object SET ProjectName = '$project' WHERE ObjectID = '$object_id'";
					$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
					echo "1" . "#" . "Object[" . $object_id . "] has been assigned to '" . $person . "'";
				} else {
					echo "0" . "#" . "Object[" . $object_id . "] has been assigned to '" . $person . "', who is not " .
						"assigned to project '" . $project . "'. Please assign '" . $person . "' to '" . $project . "' first.";
				}
			}
		}
		mysqli_close($con);
	}

	function check_person_in_project($con, $project, $person) {
		$sql = "SELECT * FROM ProjectGroup WHERE PersonName = '$person' AND ProjectName = '$project'";
		$ret = mysqli_query($con,$sql) or die(mysqli_error($con));
		if (mysqli_num_rows($ret) == 0) {
			return 0;
		}
		return 1;
	}
?>
