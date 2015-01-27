<?php

// TODO Umändern, dass die Kategorie gesetzt wird. Im Moment Kopie von Set_Neues_Spiel
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    $query = sprintf("SELECT Spiel_ID FROM Spiel WHERE Spiel_ID='%s'",
                mysql_real_escape_string($_REQUEST['spiel_id'], $con));

    $result = mysql_query($query);

    $data = mysql_fetch_array($result);

    //file_put_contents("set_cat_log.txt",$data);

    if ($data){
        $query = sprintf("INSERT INTO Runde VALUES(0,'%s',0,'%s',1,1,1)", // TODO IDs der Fragen müssen richtig eingetragen werden
                mysql_real_escape_string($_REQUEST['spiel_id'], $con),
                mysql_real_escape_string($_REQUEST['kategorie_id'], $con)
                );
        $result = mysql_query($query);

        if (!empty($result)) {
            file_put_contents("set_cat_log.txt",$query);
            echo "true";
        } else {
            //file_put_contents("set_cat_log.txt","false");
            echo "false";
        }
    }
	$db->close();
?>