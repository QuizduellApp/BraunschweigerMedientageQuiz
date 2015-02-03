<?php

/*
 * Frage abrufen
 * Benötigt frage_id
 */

// Richtigen UTF-8 header setzen
header('Content-Type: application/json; charset=utf-8');

// array for JSON response
$response = array();

// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

// check for post data
if (!empty($_REQUEST["frage_id"])) {
    $query = sprintf("SELECT Frage_ID,Kategorie_ID,Frage,Antwort_1,Antwort_2,Antwort_3,Antwort_4,Richtige_Antwort
                      FROM Frage WHERE Frage_ID=%d",
                      mysql_real_escape_string($_REQUEST['frage_id'], $con)
                      );

    $result = mysql_query($query,$con);

    file_put_contents("get_frage_log.txt",$query);

    if (!empty($result)) {
        // check for empty result
        if ($response = mysql_fetch_assoc($result)) {
			file_put_contents("get_frage_response_log.txt",serialize($response));
			$db->close();
            // success
            $response["success"] = 1;
			
			// Array in UTF-8 encodieren da JSON nur damit funktioniert!!! Ganz wichtig!
			array_walk_recursive($response, 'encode_items');
			
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

