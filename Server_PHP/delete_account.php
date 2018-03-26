<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $password = $_POST["password"];

    $sql0 = mysqli_query($con, "SELECT * FROM User WHERE username = '$username' AND password = '$password'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $delete = ("DELETE FROM User WHERE username = '$username'");
        if ($con->query($delete) === TRUE) {
            echo "success";
        }
    } else {
        echo "User not found";
    }

    include "dbClose.php";
 ?>
