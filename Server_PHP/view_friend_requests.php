<?php
    include 'dbConnect.php';

    $username = $_POST['username'];

    $response = array();
    $response["success"] = false;
    $response["requests"] = array();

    $end = 5;

    $sql0 = mysqli_query($con, "SELECT request_name FROM User WHERE id = '$username'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $counter = -1;
        $alldata = array();
        while($row = mysqli_fetch_array($sql0, MYSQLI_ASSOC)){
            $counter++;
            if($counter < $end){
                $alldata[] = $row['request_name'];
            } else {
                break;
            }
        }
        $response["requests"] = $alldata;
        $response["success"] = TRUE;
    }

    echo json_encode($response);

    include 'dbClose.php'
  ?>
