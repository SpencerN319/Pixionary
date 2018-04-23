<?php
    include 'dbConnect.php';
    $response = array();
    $response["success"] = false;
    $response["hosts"] = array();

    $end = 5;

    $sql0 = mysqli_query($con, "SELECT * FROM User WHERE user_type = 'host_req'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $counter = -1;
        $alldata = array();
        while($row = mysqli_fetch_array($sql0, MYSQLI_ASSOC)){
            $counter++;
            if($counter < $end){
                $data["username"] = $row['username'];
                $alldata[] = $data;
            } else {
                break;
            }
        }
        $response["hosts"] = $alldata;
        $response["success"] = TRUE;
    }

    echo json_encode($response);

    include 'dbClose.php'
  ?>
