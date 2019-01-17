<?php
require 'connection.php';
$conn    = Connect();
$un=$_GET['un'];
$query="select date from period_db where username='$un' order by id desc limit 3 ";
$numrows = mysqli_query($conn,$query);
$row=mysqli_fetch_assoc($numrows);
$s1= $row['date'];
$row=mysqli_fetch_assoc($numrows);
$s2= $row['date'];
$row=mysqli_fetch_assoc($numrows);
$s3= $row['date'];
$start1=date_create($s1);
$start2=date_create($s2);
$start3=date_create($s3);
$diff=date_diff($start1,$start2);
$d1=$diff->format("%a");
$diff=date_diff($start2,$start3);
$d2=$diff->format("%a");
$dv=round(($d1+$d2)/2);
$predict = date('Y-m-d', strtotime($s1. ' + '.$dv.'days'));
echo $predict;
//$sql="Insert into predicted values ('$un','$predict')";
//if(mysqli_query($conn, $sql))
//echo "added";
?>