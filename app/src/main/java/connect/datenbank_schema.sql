CREATE TABLE Benutzer
(
Benutzer_ID int NOT NULL AUTO_INCREMENT,
Benutzername varchar(50) NOT NULL UNIQUE,
Passwort varchar(60) NOT NULL,
Passwort_Salt varchar(50) NOT NULL,
Vorname varchar(50) NOT NULL,
Nachname varchar(50) NOT NULL,
Email varchar(50) NOT NULL UNIQUE,
Highscore int,
PRIMARY KEY (Benutzer_ID)
);

CREATE TABLE Spiel
(
Spiel_ID int NOT NULL AUTO_INCREMENT,
Benutzer_ID_1 int NOT NULL,
Benutzer_ID_2 int NOT NULL,
NextToPlay int NOT NULL,
PRIMARY KEY (Spiel_ID),
FOREIGN KEY (Benutzer_ID_1) REFERENCES Benutzer(Benutzer_ID),
FOREIGN KEY (NextToPlay) REFERENCES Benutzer(Benutzer_ID),
FOREIGN KEY (Benutzer_ID_2) REFERENCES Benutzer(Benutzer_ID)
);

CREATE TABLE Kategorie
(
Kategorie_ID int NOT NULL AUTO_INCREMENT,
Name varchar(80),
PRIMARY KEY (Kategorie_ID)
);

CREATE TABLE Frage
(
Frage_ID int NOT NULL AUTO_INCREMENT,
Kategorie_ID int NOT NULL,
Frage varchar,
Antwort_1 varchar,
Antwort_2 varchar,
Antwort_3 varchar,
Antwort_4 varchar,
Richtige_Antwort varchar,
PRIMARY KEY (Frage_ID),
FOREIGN KEY (Kategorie_ID) REFERENCES Kategorie(Kategorie_ID)
);

CREATE TABLE Runde
(
Runde_ID int NOT NULL AUTO_INCREMENT,
Spiel_ID int NOT NULL,
Benutzer_ID int NOT NULL,
Runde int,
Kategorie_ID int NOT NULL,
Frage_ID_1 int NOT NULL,
Frage_ID_2 int NOT NULL,
Frage_ID_3 int NOT NULL,
PRIMARY KEY (Runde_ID),
FOREIGN KEY (Spiel_ID) REFERENCES Spiel(Spiel_ID),
FOREIGN KEY (Benutzer_ID) REFERENCES Benutzer(Benutzer_ID),
FOREIGN KEY (Kategorie_ID) REFERENCES Kategorie(Kategorie_ID),
FOREIGN KEY (Frage_ID_1) REFERENCES Frage(Frage_ID),
FOREIGN KEY (Frage_ID_2) REFERENCES Frage(Frage_ID),
FOREIGN KEY (Frage_ID_3) REFERENCES Frage(Frage_ID)
);

CREATE TABLE Antworten
(
Runde_ID int NOT NULL,
Benutzer_ID int NOT NULL,
Antwort_1 int,
Antwort_2 int,
Antwort_3 int,
CONSTRAINT pk_Antworten PRIMARY KEY (Runde_ID,Benutzer_ID),
FOREIGN KEY (Runde_ID) REFERENCES Runde(Runde_ID),
FOREIGN KEY (Benutzer_ID) REFERENCES Benutzer(Benutzer_ID)
);

Create Table Freundesliste (
Freunde_id int NOT NULL auto_increment,
Bid_1 int NOT NULL,
Bid_2 int NOT NULL,
PRIMARY KEY (Freunde_id),
FOREIGN KEY (Bid_1) REFERENCES Benutzer(Benutzer_ID),
FOREIGN KEY (Bid_2) REFERENCES Benutzer(Benutzer_ID)
);