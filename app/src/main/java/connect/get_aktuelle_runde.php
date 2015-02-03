<?php

/*
 * ID der aktuellen Runde zurück geben.
 * Benötigt spiel_id
 */

// JSON Array
$response = array();

// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

// POST data überprüfen
if (!empty($_REQUEST["spiel_id"])) {
    $query = sprintf("SELECT Runde_ID
                      FROM Runde WHERE Spiel_ID=%d ORDER BY Runde_ID DESC LIMIT 1",
                      mysql_real_escape_string($_REQUEST['spiel_id'], $con)
                      );

    $result = mysql_query($query,$con);

    file_put_contents("get_aktuelle_runde_log.txt",$query);

    if (!empty($result)) {
        // Wenn das Ergebnis nicht leer ist
        if ($response = mysql_fetch_assoc($result)) {
			$db->close();
            $response["success"] = 1;
			
			// Array in UTF-8 encodieren da JSON nur damit funktioniert!!! Ganz wichtig!
			array_walk_recursive($response, 'encode_items');

            // JSON Response zurück liefern
            echo json_encode($response);
        } else {
            // Keine Ergebnis gefunden
            $response["success"] = 0;
            $response["message"] = "Keine Runde gefunden!";

            // JSON Response zurück liefern
            echo json_encode($response);
        }
    } else {
        // Kein Ergebnis
        $response["success"] = 0;
        $response["message"] = "Keine Runde gefunden!";

        // JSON Response zurück liefern
        echo json_encode($response);
    }
} else {
    // Das benötigte Feld fehlt
    $response["success"] = 0;
    $response["message"] = "Ein benötigtes Feld fehlt!";

    // JSON Response zurück liefern
    echo json_encode($response);
}
?>

