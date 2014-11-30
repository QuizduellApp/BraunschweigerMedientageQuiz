<?php

	$host='quizapp.cqndrgh2munj.us-west-2.rds.amazonaws.com:3306';
	$uname='QuizApp';
	$pwd='Android1415';
	$db="QuizDB";

	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");

$benutzername =$_REQUEST['benutzername'];


	$result = mysql_query("SELECT benutzername FROM Benutzer WHERE benutzername ='$benutzername'");

	if($row = mysql_fetch_row($result)>0)
		{echo "Benutzer existiert";
}else
{
echo "Benutzer nicht vorhanden";
}
mysql_close($con);
?>