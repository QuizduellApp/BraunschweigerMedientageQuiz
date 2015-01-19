<?php

/*
 * Following code will get personal data of one user
 * A user is identified by user id (bid)
 */

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

   
    $query = sprintf("SELECT Name FROM Kategorie ORDER BY RAND() LIMIT 2“);
    $result = mysql_query($query);

file_put_contents("log.txt",$query."    --     ". $_GET['bid']);

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $benutzer = array();
            $benutzer[„cat1“] = $result[„cat1“];
            $benutzer[„cat2“] = $result[„cat2“];

            // success
            $response["success"] = 1;

            // user node
            $response["benutzer"] = array();

            array_push($response["benutzer"], $benutzer);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no benutzer found
            $response["success"] = 0;
            $response["message"] = "No benutzer found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no benutzer found
        $response["success"] = 0;
        $response["message"] = "No benutzer found";

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
?>

