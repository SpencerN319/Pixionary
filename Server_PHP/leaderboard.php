<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $start = $_POST["page"] * 10;
    $end = $start + 10; //Change to 100 eventually, set to 10 for testing.

    $response = array();
    $response["success"] = false;
    
    /* Get the userdata so he can compare to others */
    $sqlUser = mysqli_query($con,"SELECT username, score FROM User WHERE username = '$username'");
    $userExists = mysqli_num_rows($sqlUser);
    if($userExists > 0){
        while($userdata = mysqli_fetch_array($sqlUser, MYSQLI_ASSOC)){
            $dataUser["username"] = $userdata['username'];
            $dataUser["score"] = $userdata['score'];
            $response["thisuser"] = $dataUser;
        }

        /* Get 100 or fewer users depending on the page number */
        $response["data"] = array();
        $sql0 = mysqli_query($con,"SELECT username, score FROM User ORDER BY score DESC");
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
                        $data["score"] = $row['score'];
                        $alldata[] = $data;
                    }

                }else {
                    break; //This will end the data run through.
                }
            }
            $response["data"] = $alldata;
            $response["success"] = true;
        }
    }

    echo json_encode($response);

    include "dbClose.php";
 ?>