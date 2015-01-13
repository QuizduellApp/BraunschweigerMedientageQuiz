<?php
// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

$query = sprintf("SELECT benutzername, passwort FROM Benutzer WHERE benutzername='%s' AND passwort='%s'",
	mysql_real_escape_string($_REQUEST['user'], $con),
	mysql_real_escape_string($_REQUEST['passwort'],$con));

$result = mysql_query($query);

if($row = mysql_num_rows($result) > 0) {
	echo "true";
} 
else {
	echo "false";
}
$db->close();
?>