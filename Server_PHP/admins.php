<?php
    /**
    * @author Spencer Nicol <stnicol>
    *   Hard Code all admin information
    * username, password, user_id, user_type
    */
    include "dbConnect.php";
    $sql0 = "INSERT INTO User (username, password, user_id, user_type)
             VALUES ('Spencer', 'stnicol', '0', 'admin')";
    if(!$con->query($sql0)){
        echo "fail sql0";
    }
    $sql1 = "INSERT INTO User (username, password, user_id, user_type)
             VALUES ('Levi', 'lclark', '1', 'admin')";
    if(!$con->query($sql1)){
        echo "fail sql1";
    }
    $sql2 = "INSERT INTO User (username, password, user_id, user_type)
             VALUES ('Steven', 'srein', '2', 'admin')";
    if(!$con->query($sql2)){
        echo "fail sql2";
    }
    $sql3 = "INSERT INTO User (username, password, user_id, user_type)
             VALUES ('Kole', 'kswesey', '3', 'admin')";
    if(!$con->query($sql3)){
        echo "fail sql3";
    }

    include "dbClose.php";

 ?>
