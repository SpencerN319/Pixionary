<?php
    /**
    * @author Spencer Nicol <stnicol>
    * Initializes the database connection
    */

    $host = "mysql.cs.iastate.edu";
    $user = "dbu309sb3";
    $password = "Fx3tvTaq";
    $dbname = "db309sb3";

    $con = @mysqli_connect($host, $user, $password, $dbname);

    if($con->connect_error){
        die("Connection Failed: " .$con->connect_error);
    }
?>
