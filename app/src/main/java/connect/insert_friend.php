<?php
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

$query = sprintf("SELECT benutzer_id FROM Benutzer WHERE benutzername='%s'",
			mysql_real_escape_string($_REQUEST['name'], $con));

$result = mysql_query($query);


    $query = sprintf("INSERT INTO Benutzer VALUES(0,'%s','$result')",
	        mysql_real_escape_string($_REQUEST['id'],$con));

    $query = sprintf("INSERT INTO Freundesliste VALUES(0,'$result','%s')",
	        mysql_real_escape_string($_REQUEST['id'],$con));
	        

	$db->close();
?>	