<?php
    include "dbConnect.php";

    $start = $_POST["page"] * 10;
    $end = $start + 10;

    $response = array();
    $response["success"] = false;

    $response["users"] = array();
    $sql0 = mysqli_query($con,"SELECT username FROM User ORDER BY username");
    $affected = mysqli_num_rows($sql0);
    $response["total"] = $affected;
    if($affected > 0){
        $counter = -1;
        $alldata = array();
        while($row = mysqli_fetch_array($sql0, MYSQLI_ASSOC)){
            $counter++;
            if($counter < $end) {
                if($counter >= $start){
                    $data["username"] = $row['username'];
                    $alldata[] = $data;
                }

            }else {
                break;
            }
        }
        $response["users"] = $alldata;
        $response["success"] = true;
    }

    echo json_encode($response);

    include "dbClose.php";
 ?>
