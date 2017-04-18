<?php
    $con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");

    $device_id = $_POST["device_id"];

    $query = "UPDATE Device_Table SET user_id = '0' WHERE id = '$device_id'";

    mysqli_query($con, $query);


    $response = array();
    $response["success"] = true;

    echo json_encode($response);
    $con->close();
?>