<?php
// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

$query = sprintf("SELECT benutzername FROM Benutzer WHERE benutzer_ID='%d'",
	
	mysql_real_escape_string($_REQUEST['bid'],$con));

$result = mysql_query($query);

$row = mysql_fetch_row($result);

echo $row[0];

$db->close();
?>