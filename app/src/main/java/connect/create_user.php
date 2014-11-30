<?php
	$host='quizapp.cqndrgh2munj.us-west-2.rds.amazonaws.com:3306';
	$uname='QuizApp';
	$pwd='Android1415';
	$db="QuizDB";

	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");
	 
$benutzer_id =$_REQUEST['benutzer_id'];
	$benutzername =$_REQUEST['benutzername'];
	$vorname=$_REQUEST['vorname'];
        $nachname =$_REQUEST['nachname'];
        $passwort =$_REQUEST['passwort'];
        $email =$_REQUEST['email'];
        $highscore =$_REQUEST['highscore'];


	$flag['code']=0;

	if($r=mysql_query("insert into Benutzer values('benutzer_id','$benutzername','$vorname','$nachname','$passwort','$email','$highscore') ",$con))
	{
		$flag['code']=1;
		echo"Ready";
	}

	print(json_encode($flag));
	mysql_close($con);
?>		