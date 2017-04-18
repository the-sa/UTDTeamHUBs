<?php
$con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");

if (isset($_POST["username"]))
{
  $username = $_POST["username"];
  echo $username;
  echo " is your username";
}
else
{
  $username = null;
  echo "no username supplied";
}
$password = $_POST["password"];

$statement = mysqli_prepare($con, "INSERT INTO User_Table (username, password) VALUES (?, ?)");
mysqli_stmt_bind_param($statement, "ss", $username, $password);
mysqli_stmt_execute($statement);


$response = array();
$response["success"] = true;

echo json_encode($response);
$con->close();
?>