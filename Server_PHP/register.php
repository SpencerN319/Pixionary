<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $password = $_POST["password"];
    $user_type = $_POST["user_type"];
    $user_id = uniqid();
	    
    $username = mysqli_real_escape_string($con,$username);
    $password = mysqli_real_escape_string($con,$password);
    $user_type = mysqli_real_escape_string($con,$user_type);

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
	$salt = substr(md5(rand()), 24);
	$prehash = $password.$salt;
	$hash = hash('sha256', $prehash);
        $sql0 = "INSERT INTO User (username, salt, hash, user_id, user_type)
                 VALUES ('$username','$salt''$hash','$user_id','$user_type')";

        if($con->query($sql0)){
            echo "success";
        } else {

            echo ("failure" . mysqli_error($con));
        }
    }
    include 'dbClose.php';
 ?>
