<?php
    include 'dbConnect.php';

    $start = $_POST["page"] * 4;
    $end = $start + 4;
    $category = $_POST["category"];

    $response array();
    $response["success"] = false;
    $response["urls"] = array();

    $sql0 = mysqli_query($con, "SELECT location FROM Images");
    $affected = mysqli_num_rows($sql0);
    $response["total"] = $affected;
    if($affected > 0){
        $counter = -1;
        $data = array();
        while($row = mysqli_fetch_array($sql0, MYSQLI_ASSOC)){
            $counter++;
            if($counter < $end){
                if($counter >= $start){
                    $url["url"] = $row['location'];
                    $data[] = $url;
                }
            } else { break; }
        }
        $response["urls"] = $data;
        $response["success"] = true;
    }

    echo json_encode($response);
    
    include 'dbClose.php'
  ?>
