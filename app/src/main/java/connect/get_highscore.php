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

$query = "SELECT Benutzername, Highscore
          FROM Benutzer
          GROUP BY Highscore DESC
          LIMIT 10";
file_put_contents("get_highscore_log.txt",$query);
$result = mysql_query($query);

if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $response["score"] = array();
            while($score = mysql_fetch_assoc($result)) {
                $response["success"] = 1;
                array_push($response["score"], $score);
            }
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no highscores found
            $response["success"] = 0;
            $response["message"] = "No highscores found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
      // no highscores found
                  $response["success"] = 0;
                  $response["message"] = "No highscores found";

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