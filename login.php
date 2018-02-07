<html>
<body>
 
 
<?php
//this needs to not be localhost
$con = @mysqli_connect("localhost","root","","accounts");
if (!$con)
  {
	  die('Could not connect to mysql server ' . mysqli_error());
  }

$username =$_GET['username'];  
$password =$_GET['password'];

$result = (mysqli_query($con, "SELECT * FROM logins WHERE username = '$username' AND password = '$password'"));
$count =mysqli_num_rows($result);

if(!$count)
{
	die('Login attempt failed ');
	       #	. mysqli_error());

  }

echo "Success! do something here other than just echo";
mysqli_close($con)
?>
</body>
</html>
