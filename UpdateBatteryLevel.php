<?php
$con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");


$id = $_POST["id"];
$battery_level = $_POST["battery_level"];

$query = "UPDATE Device_Table SET battery_level = '$battery_level' WHERE id = '$id'";

mysqli_query($con, $query);


$response = array();
$response["success"] = true;

echo json_encode($response);
$con->close();
?>