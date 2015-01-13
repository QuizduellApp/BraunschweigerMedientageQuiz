<?php
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();
	 
    $query = sprintf("INSERT INTO Benutzer VALUES(0,'%s','%s','%s','%s','%s',0)",
	        mysql_real_escape_string($_REQUEST['benutzername'],$con),
	        mysql_real_escape_string($_REQUEST['vorname'],$con),
            mysql_real_escape_string($_REQUEST['nachname'],$con),
            mysql_real_escape_string($_REQUEST['passwort'],$con),
            mysql_real_escape_string($_REQUEST['email'],$con));

	file_put_contents("log.txt", $query); // Log Query TODO: Remove later

    $flag['code']=0;
	if($r=mysql_query($query,$con))
	{
		$flag['code']=1;
		echo"Ready";
	}

	print(json_encode($flag));
	$db->close();
?>	