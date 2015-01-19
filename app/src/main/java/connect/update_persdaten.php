<?php

/*
 * Following code will update personal data of one user
 * A user is identified by user id (bid)
 */

// array for JSON response
$response = array();

if (isset($_POST['bid']) && isset($_POST['benutzername']) && isset($_POST['email']) && isset($_POST['passwort'])) {

    $bid = $_POST['bid'];
    $benutzername = $_POST['benutzername'];
    $email = $_POST['email'];
    $passwort = $_POST['passwort'];

    // include db connect class
    require_once('/home/a4717469/public_html/db_connect.php');

    // connecting to db
    $db = new DB_CONNECT();

    // mysql update row with matched bid
    $result = mysql_query("UPDATE Benutzer SET benutzername = '$benutzername', email = '$email', passwort = '$passwort' WHERE benutzer_id = $bid");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    } else {

    }
} else {
     // required field is missing
     $response["success"] = 0;
     $response["message"] = "Required field(s) is missing";

     // echoing JSON response
     echo json_encode($response);
}

?>