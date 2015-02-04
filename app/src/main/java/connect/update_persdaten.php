<?php

	/*
	* Persönliche Daten des Spielers werden gespewichert
	*/
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    // Variablen müssen vorhanden sein
    if(empty($_REQUEST['bid']) || empty($_REQUEST['benutzername']) || empty($_REQUEST['email'])
			 || empty($_REQUEST['passwort'])) {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
	
		// echoing JSON response
		echo json_encode($response);
        exit();
    }

	$query = sprintf("UPDATE Benutzer SET benutzername = '%s', email = '%s', passwort = '%s' WHERE benutzer_id = %d",
			mysql_real_escape_string($_REQUEST['benutzername'], $con),
			mysql_real_escape_string($_REQUEST['email'], $con),
			mysql_real_escape_string($_REQUEST['passwort'], $con),
			mysql_real_escape_string($_REQUEST['bid'], $con)
			);
			
	$result = mysql_query($query);

	if (!empty($result)) {
		file_put_contents("update_persdaten_log.txt",$query);
		
		// successfully updated
        $response["success"] = 1;
        $response["message"] = "Spielerdaten erfolgreich geaendert.";

        // echoing JSON response
        echo json_encode($response);
	} else {
		echo "false";
    }
	$db->close();
?>