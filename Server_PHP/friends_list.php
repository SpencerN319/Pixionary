<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $start = $_POST["page"] * 10;
    $end = $start + 10;

    $response = array();
    $response["success"] = false;
    $response["friends"] = array();

    $sql0 = mysqli_query($con,"SELECT * FROM Friends WHERE id = '$username' ORDER BY friend_name");
    $affected = mysqli_num_rows($sql0);
    $response["total"] = $affected;
    if($affected > 0){
        $counter = -1;
        $alldata = array();
        while($row = mysqli_fetch_array($sql0, MYSQLI_ASSOC)){
            $counter++;
            if($counter < $end) {
                if($counter >= $start){
                    $alldata = $row['friend_name'];
                }

            } else {
                break;
        }
    }
    $response["friends"] = $alldata;
    $response["success"] = true;
}

echo json_encode($response);

include "dbClose.php";

  ?>
