<?php

// include db connect class
require_once('/home/a4717469/public_html/db_connect.php');

// connecting to db
$db = new DB_CONNECT();

$user =$_REQUEST['user'];
$passwort =$_REQUEST['passwort'];

$result = mysql_query("SELECT benutzer_id FROM Benutzer WHERE benutzername='$user' AND passwort='$passwort'");
$row = mysql_fetch_object($result);
echo "$row->benutzer_id";

?>