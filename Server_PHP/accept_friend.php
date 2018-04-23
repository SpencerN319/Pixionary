<?php
    include 'dbConnect.php';

    $username = $_POST["username"];
    $friend = $_POST['friend'];

    $sql0 = mysqli_query($con, "SELECT * FROM Friends WHERE username = '$username'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $update = ("UPDATE Friends
                    SET friend_name = '$friend', request_name = null
                    WHERE username = '$username' and request_name = '$friend'");
        if($con->query($update) === TRUE){
            echo "success";
        }
    } else {
        echo "$username";
    }

    include 'dbClose.php';

  ?>
