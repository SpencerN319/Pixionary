<?php
    include 'dbConnect.php';

    $word = $_POST['word'];
    $category = $_POST['category'];

    $response["success"] = false;

    $sql0 = mysqli_query($con, "SELECT * FROM Images WHERE category = '$category'");
    $affected = mysqli_num_rows($sql0);
    if($affected > 0){

        $sql1 = mysqli_query($con, "DELETE FROM Images WHERE word = '$word'");
        $affected_deletes = mysqli_num_rows($sql1);
        if($affected_deletes > 0){
            $response["success"] = true;
        }
    }
    echo json_encode($response);

    include 'dbClose.php';
 ?>
