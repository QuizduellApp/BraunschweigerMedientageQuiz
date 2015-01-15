<?php
// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

$query = sprintf("SELECT benutzer_id,passwort FROM Benutzer WHERE benutzername='%s'",
			mysql_real_escape_string($_REQUEST['user'], $con));

$result = mysql_query($query);

$row = mysql_fetch_object($result);
$db->close();

// Passwort überprüfen
if (password_verify($_REQUEST['passwort'], $row->passwort) {
    echo "$row->benutzer_id";
}
?>