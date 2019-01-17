<?php
	$email=$_GET['email'];
	$username=$_GET['un'];
	$password=$_GET['pw'];

	require 'connection.php';
	$conn    = Connect();
		$sql="Insert into customer values ('$email','$username','$password')";

		if(mysqli_query($conn, $sql))
		{
			echo "added";
	        
		}
		else
			echo "error";
	
	
	mysqli_close($conn);
?>