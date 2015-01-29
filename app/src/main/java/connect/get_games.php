<?php

// array for JSON response
$response = array();

// Datenbankverbindungsklasse einbinden
require_once(dirname(__FILE__).'/db_connect.php');

// Klasseninstanz und -verbindung herstellen
$db = new DB_CONNECT();
$con = $db->connect();

// check for post data
if (isset($_GET["bid"])) {
    $bid = intval($_GET['bid']);

$query = sprintf("SELECT Benutzer.benutzername
                  FROM Benutzer
                  WHERE Benutzer_ID IN
                  (SELECT Benutzer_ID_1
                  FROM Spiel WHERE NextToPlay ='%d'
                  AND Benutzer_ID_2 ='%d')
                  OR
                  Benutzer_ID IN
                  (SELECT Benutzer_ID_2
                  FROM Spiel WHERE NextToPlay ='%d'
                  AND Benutzer_ID_1 ='%d')",
               mysql_real_escape_string($bid, $con),
               mysql_real_escape_string($bid, $con),
               mysql_real_escape_string($bid, $con),
               mysql_real_escape_string($bid, $con)
               );
file_put_contents("get_games_log.txt",$query);
$result = mysql_query($query);

if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

$benutzer = array();    
$response["benutzer"] = array();
while($benutzer = mysql_fetch_array($result)) {
    $response["success"] = 1;
    array_push($response["benutzer"], $benutzer);
}
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no benutzer found
            $response["success"] = 0;
            $response["message"] = "No games found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no benutzer found
        $response["success"] = 0;
        $response["message"] = "No games found";

        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}

$db->close();
?>