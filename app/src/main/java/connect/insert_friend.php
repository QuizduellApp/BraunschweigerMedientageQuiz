<?php
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    file_put_contents("inser_friend_log_1.txt","Name: ".$_REQUEST['name']." - ID: ".$_REQUEST['id']);

    $query = sprintf("SELECT benutzer_id FROM Benutzer WHERE benutzername='%s'",
                mysql_real_escape_string($_REQUEST['name'], $con));

    $result = mysql_query($query);
    $data = mysql_fetch_array($result);

    if (!empty($data)){
        $query = sprintf("INSERT INTO Freundesliste VALUES(0,'%s','%s')",
                mysql_real_escape_string($_REQUEST['id'], $con),
                mysql_real_escape_string($data['benutzer_id'], $con));
        $result = mysql_query($query);

        $query2 = sprintf("INSERT INTO Freundesliste VALUES(0,'%s','%s')",
                mysql_real_escape_string($data['benutzer_id'], $con),
                mysql_real_escape_string($_REQUEST['id'], $con));
        $result2 = mysql_query($query2);

        if (!empty($result) && !empty($result)) {
            file_put_contents("inser_friend_log.txt",$query." - ".$query2);
            echo "true";
        } else {
            file_put_contents("inser_friend_log.txt",$query." - ".$query2);
            echo "false";
        }

    }
	$db->close();
?>	