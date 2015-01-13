<?php
// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

$query = sprintf("SELECT benutzer_id FROM Benutzer WHERE benutzername='%s' AND passwort='%s'",
			mysql_real_escape_string($_REQUEST['user'], $con),
			mysql_real_escape_string($_REQUEST['passwort'], $con));

$result = mysql_query($query);

$row = mysql_fetch_object($result);
echo "$row->benutzer_id";

$db->close();
?>