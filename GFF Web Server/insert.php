<?php
	$username=$_GET['un'];
	$date1=$_GET['date1'];
	$date2=$_GET['date2'];
	$date3=$_GET['date3'];


	require 'connection.php';
	$conn    = Connect();
		$sql1="INSERT INTO `period_db` (`id`, `username`, `date`) VALUES (NULL, '$username', '$date1')";
		$sql2="INSERT INTO `period_db` (`id`, `username`, `date`) VALUES (NULL, '$username', '$date2')";
		$sql3="INSERT INTO `period_db` (`id`, `username`, `date`) VALUES (NULL, '$username', '$date3')";

		if(mysqli_query($conn, $sql1) and mysqli_query($conn, $sql2) and mysqli_query($conn, $sql3))
			echo "added";
		else
			echo "error in adding";

	
	
	mysqli_close($conn);
?>