create_product.php
<?php
	$host='quizapp.cqndrgh2munj.us-west-2.rds.amazonaws.com:3306';
	$uname='QuizApp';
	$pwd='Android1415';
	$db="QuizDB";

	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");

	$benutzername =$_REQUEST['benutzername'];
	$vorname=$_REQUEST['vorname'];
        $nachname =$_REQUEST['nachname'];
        $passwort =$_REQUEST['passwort'];
        $email =$_REQUEST['email'];


	$flag['code']=0;

	if($r=mysql_query("insert into Benutzer values('$benutzername','$vorname','$nachname','$passwort','$email') ",$con))
	{
		$flag['code']=1;
		echo"hi";
	}

	print(json_encode($flag));
	mysql_close($con);
?>