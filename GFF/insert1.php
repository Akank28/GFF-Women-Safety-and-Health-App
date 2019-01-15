<?php
	$username=$_GET['un'];
	$date1=$_GET['date1'];
	


	require 'connection.php';
	$conn    = Connect();
		$sql1="INSERT INTO `period_db` (`id`, `username`, `date`) VALUES (NULL, '$username', '$date1')";
		

		if(mysqli_query($conn, $sql1))
			echo "added date1";
		else
			echo "error in adding date1";
		
	
	
	mysqli_close($conn);
?>