<?php
// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

$query = sprintf("SELECT benutzername, passwort FROM Benutzer WHERE benutzername='%s'",
	mysql_real_escape_string($_REQUEST['user'], $con));

$result = mysql_query($query);
$row = mysql_fetch_object($result);
//$db->close();

/*var_dump($__REQUEST);
var_dump($_GET);
if ($row->benutzername != "") {echo "BENUTZER";}
if (password_verify($_GET['passwort'], $row->passwort)) {echo "PASSWORT";}

echo $_GET['passwort'];

echo "\nROW:".$row->passwort;

*/
if($row->benutzername != "" && password_verify($_REQUEST['passwort'], $row->passwort)) {
	echo "true";
} 
else {
	echo "false";
}

?>