<?php
    include 'dbConnect.php'

    $word = $_POST['word'];

    $old_path = getcwd();
    chdir('./');
    $output = shell_exec('./imgtaker.sh $word');
    chdir($old_path);

    sleep(20);

    


    include 'dbClose.php'
?>
