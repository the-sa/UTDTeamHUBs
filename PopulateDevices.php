<?php
    $con = mysqli_connect("localhost", "id1379182_database", "vdci2017", "id1379182_bluetoothmanagementdevice");
    
    $statement = mysqli_query($con, "SELECT * FROM Device_Table");
    
    $response = mysqli_fetch_all($statement, MYSQLI_ASSOC);
    
    echo json_encode($response);
    
    mysqli_free_result($statement);
    
    $con->close();
    
    ?>