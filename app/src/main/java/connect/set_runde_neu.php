<?php
/*
* Neue Runde wird mit der Kategorie und zufällig generierten Fragen erstellt
*/
	// Datenbankverbindungsklasse einbinden
    require_once(dirname(__FILE__).'/db_connect.php');

    // Klasseninstanz und -verbindung herstellen
    $db = new DB_CONNECT();
    $con = $db->connect();

    // Variablen müssen vorhanden sein
    if(empty($_REQUEST['spiel_id']) || empty($_REQUEST['benutzer_id']) || empty($_REQUEST['kategorie_id'])) {
        echo "false";
        exit();
    }

    $query = sprintf("SELECT Spiel_ID FROM Spiel WHERE Spiel_ID='%s'",
                mysql_real_escape_string($_REQUEST['spiel_id'], $con));
    $result = mysql_query($query);
    $data = mysql_fetch_array($result);

    $query = sprintf("SELECT Frage_ID FROM Frage
                        WHERE Kategorie_ID='%s'
                        ORDER BY RAND()
                        LIMIT 3",
                mysql_real_escape_string($_REQUEST['kategorie_id'], $con));
    $result = mysql_query($query);

    $i = 0;
    while ($returnData = mysql_fetch_array($result)) {
        $frage[$i] = $returnData['Frage_ID'];
        $i++;
    }

    file_put_contents("set_cat_log.txt",$data." - ".var_dump($frage));

    if ($data && $frage){
        $query = sprintf("INSERT INTO Runde VALUES(0,'%s','%s',0,'%s','%s','%s','%s')",
                mysql_real_escape_string($_REQUEST['spiel_id'], $con),
                mysql_real_escape_string($_REQUEST['benutzer_id'], $con),
                mysql_real_escape_string($_REQUEST['kategorie_id'], $con),
                mysql_real_escape_string($frage[0], $con),
                mysql_real_escape_string($frage[1], $con),
                mysql_real_escape_string($frage[2], $con)
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