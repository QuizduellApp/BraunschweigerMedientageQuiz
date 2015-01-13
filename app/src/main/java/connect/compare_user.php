<?php
    // Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    $query = sprintf("SELECT benutzername FROM Benutzer WHERE benutzername = '%s'",
                mysql_real_escape_string($_REQUEST['user'],$con));

    file_put_contents("log.txt", $query);

	$result = mysql_query($query, $con);

	if($row = mysql_num_rows($result) > 0 ) {
		echo "true";
    }
    else {
        echo "false";
    }
    $db->close();
?>