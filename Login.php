<?php
/**
 * Created by PhpStorm.
 * User: Garrett
 * Date: 4/15/2017
 * Time: 8:45 PM
 */
    $con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");

    $username = $_POST["username"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "SELECT * FROM User_Table WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $username, $password0, $role);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["username"] = $username;
        $response["password"] = $password;
        $response["role"] = $role;
        $response["id"] = $id;
    }

    echo json_encode($response);
    $con->close();
?>