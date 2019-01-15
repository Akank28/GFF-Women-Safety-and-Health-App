<?php
	$u=$_GET['uid'];
	$p=$_GET['upw'];
	require 'connection.php';
	$conn    = Connect();

	$sql="Select password from customer where username='".$u."';";

	$result = mysqli_query($conn, $sql);

	if (mysqli_num_rows($result) ==1) 
	{
    // output data of each row
	    while($row = mysqli_fetch_assoc($result)) 
	    {
	    	//echo $row['password']."<br>".$row['username']." ".md5($p)." ".$p;
        	if($row['password']==$p)
        	{
        		
        		echo "success";
        	}
        	else 
	{
		echo "failure";
	}
    	}
	} 
	else 
	{
		echo "failure";
	}

	mysqli_close($conn);
?>