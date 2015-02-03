<?php
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();
	
	// Variablen müssen vorhanden sein
    if(empty($_REQUEST['benutzer_id']) || empty($_REQUEST['highscore_add'])) {
        echo "false";
        exit();
    }
	
	if ($_REQUEST['highscore_add'] > 3) {
		$_REQUEST['highscore_add'] = 0;
	}

    $query = sprintf("UPDATE Benutzer SET Highscore = Highscore + %d WHERE Benutzer_ID=%d",
                mysql_real_escape_string($_REQUEST['highscore_add'], $con),
                mysql_real_escape_string($_REQUEST['benutzer_id'], $con));

    $result = mysql_query($query);
	
	file_put_contents("set_next_to_play_log.txt",$query);

	if (!empty($result)) {
		echo "true";
	} else {
		echo "false";
    }
	$db->close();
?>