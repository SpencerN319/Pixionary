<?php
    /*
    @Author Spencer Nicol
    03-22-18
    */
    include "dbConnect.php";

    $username = $_POST["username"];
    $start = $_POST["page"] * 10;
    $end = $start + 10;

    $response = array();
    $response["success"] = false;
    $response["data"] = array();

    $sql = "SELECT DISTINCT category FROM Images ORDER BY category ASC";
    $result = $con->query($sql);

    if($result->num_rows > 0){
        $alldata = array();
        $affected = $result->num_rows;
        $response["total"] = $affected;
        $counter = -1;

        while($row=$result->fetch_assoc()){
            $counter++;
            if($counter < $end) {
                if($counter >= $start){
                     $data["category"] = $row['category'];
                    $alldata[] = $data;
                }
            } else {
                break;
            }
        }
        $response["data"] = $alldata;
        $response["success"] = true;
    }

    echo json_encode($response);

    include "dbClose.php";
 ?>
