<?php
    $con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");

    $user_id = $_POST["user_id"];

    $query = "DELETE FROM User_Table WHERE id = '$user_id'";

    mysqli_query($con, $query);


    $response = array();
    $response["success"] = true;

    echo json_encode($response);
    $con->close();
?>