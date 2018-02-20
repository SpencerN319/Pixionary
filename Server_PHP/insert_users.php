<<?php
    include "dbConnect.php";

    $count = 0;
    $front1 = "sample";
    $front2 = "password";
    $user_id = uniqid();
    $user_type = "general";



    while($count < 30) {
        $name = $front1.$count;
        $password = $front2.$count;
        $score = mt_rand(0,50);
        $games_played = mt_rand(5,20);
        while($con->query("SELECT user_id FROM User WHERE user_id = '$user_id'")->num_rows > 0){
            $user_id = uniqid();
        }
        $sql0 = mysqli_query($con,"INSERT INTO User (username, password, user_id, user_type, score, games_played)
                 VALUES ('$name', '$password', '$user_id', '$user_type', '$score', '$games_played')");
        $count++;
    }

    include "dbClose.php";

 ?>
