<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $password = $_POST["password"];

    $response = array();
    $response["success"] = false;
    $response["status"] = "invalid";

    $sql0 = mysqli_query($con, "SELECT * FROM User WHERE username = '$username' AND password = '$password'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $response["success"] = true;
        $response["status"] = "valid";
        while($row = mysqli_fetch_array($sql0, MYSQLI_ASSOC)){
            $response["username"] = $row['username'];
            $response["password"] = $row['password'];
            $response["user_id"] = $row['user_id'];
            $response["user_type"] = $row['user_type'];
            $response["score"] = $row['score'];
            //$response["categories_created"] = $row['categories_created']; TODO Implement later.
            $response["category_count"] = $row['category_count'];
            $response["image_count"] = $row['image_count'];
            $response["games_played"] = $row['games_played'];
        }
    }

    echo json_encode($response);
    include "dbClose.php";

 ?>
