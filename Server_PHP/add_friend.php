<?php
    include 'dbConnect.php';

    $username = $_POST['username'];
    $friend = $_POST['friend'];

    $sql0 = mysqli_query($con, "SELECT * FROM User WHERE username = '$friend'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $sql1 = mysqli_query($con, "SELECT * FROM Friends WHERE id = '$username' AND friend_name = '$friend' OR request_name = '$friend'");
        $exists = mysqli_num_rows($sql1);
        if($exists == 0){
            $sql2 = mysqli_query($con, "INSERT INTO Friends(id, request_name)
                                 VALUES ('$username', '$friend')");
            echo "success";
        } else {
            echo "exists";
        }
    } else {
        echo "error";
    }

    include 'dbClose.php';
  ?>
