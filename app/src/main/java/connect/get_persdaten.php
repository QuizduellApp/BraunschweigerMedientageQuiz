<?php

/*
 * Following code will get personal data of one person
 * A person is identified by user id (bid)
 */

// array for JSON response
$response = array();

// include db connect class
require_once('/home/a4717469/public_html/db_connect.php');

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["bid"])) {
    $bid = $_GET['bid'];

    // get a benutzer from benutzer table
    $result = mysql_query("SELECT benutzername, email FROM Benutzer WHERE benutzer_id = $bid");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $benutzer = array();
            $benutzer["benutzername"] = $result["benutzername"];
            $benutzer["email"] = $result["email"];

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