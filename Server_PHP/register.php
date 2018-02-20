<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $password = $_POST["password"];
    $user_type = $_POST["user_type"];
    $user_id = uniqid();

    //check if user exists
    $check = mysqli_query($con, "SELECT * FROM User WHERE username = '$username'");
    $count = mysqli_num_rows($check);
    if($count > 0){
        echo "username already exists";
    } else {
        while($con->query("SELECT user_id FROM User WHERE user_id = '$user_id'")->num_rows > 0){
            $user_id = uniqid();
        }
        //insert user data
        $sql0 = "INSERT INTO User (username, password, user_id, user_type)
                 VALUES ('$username','$password','$user_id','$user_type')";

        if($con->query($sql0)){
            echo "success";
        } else {

            echo ("failure" . mysqli_error($con));
        }
    }
    include "dbClose.php";
 ?>
