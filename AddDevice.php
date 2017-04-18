<?php
    $con = mysqli_connect("localhost","id1379182_database","vdci2017", "id1379182_bluetoothmanagementdevice");
    
    $device = $_POST["device"];
    $macAddress = $_POST["macAddress"];
    
    $statement = mysqli_prepare($con, "INSERT INTO Device_Table (device_name, mac_address) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ss", $device, $macAddress);
    mysqli_stmt_execute($statement);
    

    
    $response = array();
    $response["success"] = false;
    
    while(mysqli_stmt_fetch($statement))
    {
        $response["success"] = true;  
        $response["Device"] = device;
        $response["MACAddress"] = MACAddress;
    }
    
    echo json_encode($response);
?>