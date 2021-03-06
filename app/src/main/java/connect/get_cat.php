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

    $query = sprintf("SELECT Kategorie_ID,Name FROM Kategorie ORDER BY RAND() LIMIT 2");
    $result = mysql_query($query);

    file_put_contents("get_cat_log.txt",$query." - ".$_GET['bid']);

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $i = 1;
            while ($row = mysql_fetch_array($result)) {
                $response['cat'.$i] = $row['Name'];
                $response['cat'.$i."_id"] = $row['Kategorie_ID'];
                $i++;
            }

            // success
            $response["success"] = 1;

            // Array in UTF-8 encodieren da JSON nur damit funktioniert!!! Ganz wichtig!
			array_walk_recursive($response, 'encode_items');

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

