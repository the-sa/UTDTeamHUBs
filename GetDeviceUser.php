<?php
$con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");

$user_id = $_POST["user_id"];

$statement = mysqli_query($con, "SELECT * FROM Device_Table WHERE user_id = '$user_id'");

$response = mysqli_fetch_all($statement, MYSQLI_ASSOC);

echo json_encode($response);

mysqli_free_result($statement);

$con->close();

?>