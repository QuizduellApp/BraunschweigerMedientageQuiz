<?php
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    $query = sprintf("SELECT benutzer_id FROM Benutzer WHERE benutzer_id='%s' OR benutzer_id='%s'",
                mysql_real_escape_string($_REQUEST['spieler1'], $con),
                mysql_real_escape_string($_REQUEST['spieler2'], $con));

    $result = mysql_query($query);

    $spielerCount = 0;
    while ($data = mysql_fetch_array($result)) {
        $spielerCount++;
    }
    //file_put_contents("set_neues_spiel_log1.txt",$spielerCount);

    if ($spielerCount == 2){
        $query = sprintf("INSERT INTO Spiel VALUES(0,'%s','%s','%s')",
                mysql_real_escape_string($_REQUEST['spieler1'], $con),
                mysql_real_escape_string($_REQUEST['spieler2'], $con),
                mysql_real_escape_string($_REQUEST['next_to_play'], $con)
                );
        $result = mysql_query($query);

        // TODO evtl. erneut verkehrt herum eintragen? Wenn benötigt...

        if (!empty($result)) {
            file_put_contents("set_neues_spiel_log2.txt",$query);
            echo "true";
        } else {
            file_put_contents("set_neues_spiel_log2.txt","false");
            echo "false";
        }
    }
	$db->close();
?>