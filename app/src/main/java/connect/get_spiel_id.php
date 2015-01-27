<?php

/*
 * Spiel ID ermitteln
 */

// Array der JSON Antwort
$response = array();

// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

// check for post data
if (isset($_GET["spieler1"]) && isset($_GET["spieler2"])) {
    $query = sprintf("SELECT Spiel_ID
                      FROM Spiel
                      WHERE (Benutzer_ID_1='%s' AND Benutzer_ID_2='%s')
                         OR (Benutzer_ID_1='%s' AND Benutzer_ID_2='%s')",
                      mysql_real_escape_string($_GET['spieler1'], $con),
                      mysql_real_escape_string($_GET['spieler2'], $con),
                      mysql_real_escape_string($_GET['spieler2'], $con),
                      mysql_real_escape_string($_GET['spieler1'], $con)
                      );

    $result = mysql_query($query);

    file_put_contents("get_spiel_id.txt",$query);

    if (!empty($result)) {
        // check for empty result
        if ($response = mysql_fetch_assoc($result)) {
            // success
            $response["success"] = 1;

            // TODO Es wird nur eine Spiel ID zurück gegeben,
            // wenn wir mehrere Spiele zwischen zwei gleichen Spielern erlauben wollen,
            // dann müssen wir mehrere IDs zurück liefern

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no benutzer found
            $response["success"] = 0;
            $response["message"] = "Kein Spiel gefunden!";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no benutzer found
        $response["success"] = 0;
        $response["message"] = "Kein Spiel gefunden!";

        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Ein benötigtes Feld fehlt!";

    // echoing JSON response
    echo json_encode($response);
}
?>

