<?php

	$host='quizapp.cqndrgh2munj.us-west-2.rds.amazonaws.com:3306';
	$uname='QuizApp';
	$pwd='Android1415';
	$db="QuizDB";

	$conn = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$conn) or die("db selection failed");

$user =$_REQUEST['user'];
$passwort =$_REQUEST['passwort'];


	$result = mysql_query("SELECT benutzername, passwort FROM Benutzer WHERE benutzername ='$user' AND passwort= '$passwort‘“);

	if($row = mysql_num_rows($result)>0)
		{echo "true";
}else
{
echo "false";
}
mysql_close($conn);
?>