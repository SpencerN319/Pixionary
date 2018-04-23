<?php
    include "dbConnect.php";

    $username = $_POST["username"];
    $password = $_POST["password"];
    $category_name = $_POST["name"];

    $sql0 = mysqli_query($con, "SELECT * FROM User WHERE username = '$username' AND password = '$password'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){
        $sql1 = mysqli_query($con, "SELECT * FROM Images WHERE category = '$category_name'");
        $taken = mysqli_num_rows($sql1);
        if($taken > 0){
            echo "error";
        } else {
            $sql2 = mysqli_query($con, "INSERT INTO Images(location, word, category)
                                 Values ('blank', 'blank', '$category_name')");
            echo "success";
        }
    } echo "error";

    include "dbClose.php";
 ?>
