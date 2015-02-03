<?php
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();
	
	// Variablen müssen vorhanden sein
    if(empty($_REQUEST['spiel_id']) || empty($_REQUEST['next_to_play'])) {
        echo "false";
        exit();
    }

    $query = sprintf("UPDATE Spiel SET NextToPlay = %d WHERE Spiel_ID=%d",
                mysql_real_escape_string($_REQUEST['next_to_play'], $con),
                mysql_real_escape_string($_REQUEST['spiel_id'], $con));

    $result = mysql_query($query);
	
	file_put_contents("set_next_to_play_log.txt",$query);

	if (!empty($result)) {
		echo "true";
	} else {
		echo "false";
    }
	$db->close();
?>