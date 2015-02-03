<?php
/*
* Neue Runde wird mit der Kategorie und zufällig generierten Fragen erstellt
*/
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    // Variablen müssen vorhanden sein
    if(empty($_REQUEST['runde_id']) || empty($_REQUEST['benutzer_id']) || empty($_REQUEST['antwort_1'])
			 || empty($_REQUEST['antwort_2']) || empty($_REQUEST['antwort_3'])) {
        echo "false";
        exit();
    }
    
	$query = sprintf("INSERT INTO Antworten VALUES(%d,'%d','%d','%d','%d')",
			mysql_real_escape_string($_REQUEST['runde_id'], $con),
			mysql_real_escape_string($_REQUEST['benutzer_id'], $con),
			mysql_real_escape_string($_REQUEST['antwort_1'], $con),
			mysql_real_escape_string($_REQUEST['antwort_2'], $con),
			mysql_real_escape_string($_REQUEST['antwort_3'], $con)
			);
	$result = mysql_query($query);

	if (!empty($result)) {
		file_put_contents("set_antworten_query_log.txt",$query);
		echo "true";
	} else {
		echo "false";
    }
	$db->close();
?>