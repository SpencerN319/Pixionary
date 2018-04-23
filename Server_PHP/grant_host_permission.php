<?php
    include 'dbConnect';

    $username = $_POST;

    $sql0 = mysqli_query($con, "SELECT * FROM User WHERE username = '$username'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $update = ("UPDATE User
                    SET user_type = 'host'
                    WHERE username = '$username''");
        if($con->query($update) === TRUE){
            echo "success";
        }
    } else {
        echo "error";
    }

    include 'dbClose';

  ?>
