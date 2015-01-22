<?php

/*
 * Frage anhand einer Kategorie ID auswählen
 */

// array for JSON response
$response = array();

// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

// check for post data
if (isset($_GET["kategorie_id"])) {
    $query = sprintf("SELECT Frage_ID,Kategorie_ID,Frage,Antwort_1,Antwort_2,Antwort_3,Antwort_4,Richtige_Antwort
                      FROM Frage WHERE Kategorie_ID=%d ORDER BY RAND() LIMIT 1",
                      mysql_real_escape_string($_GET['kategorie_id'], $con)
                      );

    $result = mysql_query($query);

    file_put_contents("get_frage_log.txt",$query." - ".$_GET['kategorie_id']);

    if (!empty($result)) {
        // check for empty result
        if ($response = mysql_fetch_assoc($result)) {
            // success
            $response["success"] = 1;

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no benutzer found
            $response["success"] = 0;
            $response["message"] = "Keine Frage gefunden!";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no benutzer found
        $response["success"] = 0;
        $response["message"] = "Keine Frage gefunden!";

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
