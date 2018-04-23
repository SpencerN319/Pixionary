<<?php
    include "dbConnect.php";

    $sql = "SELECT username, password, user_id, user_type FROM User";
    $result = $con->query($sql);

    if($result->num_rows > 0){
        while($row=$result->fetch_assoc()){
            echo "Username: " .$row["username"]. " Password: " .$row["password"].
                 "User_id: " .$row["user_id"]. " User_type: " .$row["user_type"]. "<br>";
        }
    } else {
        echo "0 results";
    }

    include "dbClose.php";

 ?>
