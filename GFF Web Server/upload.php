<?php
	$input = $_GET['data'];
	$fileName="./data.csv";
	$DelFilePath = $fileName;
 
# delete file if exists
if (file_exists($DelFilePath)) { unlink ($DelFilePath); }
	$file_open = fopen("data.csv", "w");
	$no_rows = count(file("data.csv"));
	if($no_rows > 1)
	{
		$no_rows = ($no_rows - 1) + 1;
	}
	$form_data = explode('.', $input);

	fputcsv($file_open, $form_data);
?>
ok