<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $password = $_POST["password"];
    $username = mysqli_real_escape_string($con,$username);
    $password = mysqli_real_escape_string($con,$password);
	

	$salt=mysqli_query($con, "SELECT salt FROM User WHERE username = '$uername'");

    if ($salt) {
        #USER DETAILS MATCH
	$prehash = $password.$salt;
	$thehash=hash('sha256',$prehash);
	$check2 = mysqli_query($con, "SELECT username, hash FROM User WHERE username= '$username' AND hash='$thehash'");
	$affected2 = mysqli_num_rows($check2);
	if ($check2 > 0)
{
        echo "success";
        /*
        $response["success"] = true;
        while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
            $response["name"] = $row['name'];
            $response["password"] = $row['password'];
            $response["user_id"] = $row['user_id'];
	}       
 } else {
	echo "invalid username or password";
	}
        */
    }
    else {
        echo "invalid username or password";
        /*
        $userCheck = mysqli_query($con,"SELECT * FROM User WHERE username = '$username'");
        $usersAffected = mysqli_num_rows($userCheck);
        if($usersAffected > 0){
            echo "invalid password";
        }*/
    }


    include 'dbClose.php';
?>
