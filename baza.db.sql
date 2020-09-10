BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "grad" (
	"id"	INTEGER,
	"naziv"	TEXT,
	"broj_stanovnika"	INTEGER,
	"drzava"	INTEGER,
	"nadmorska_visina"  INTEGER,
	"zagadjenost"  INTEGER,
	"tip"  INTEGER,
	"slika"  TEXT,
	FOREIGN KEY("drzava") REFERENCES "drzava",
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "drzava" (
	"id"	INTEGER,
	"naziv"	TEXT,
	"glavni_grad"	INTEGER,
	"najveci_grad"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "tipGrada" (
	"id"	INTEGER,
	"naziv"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "pobratimi" (
	"g1"	INTEGER,
	"g2"	INTEGER,
	PRIMARY KEY("g1","g2"),
	FOREIGN KEY("g1") REFERENCES "grad",
	FOREIGN KEY("g2") REFERENCES "grad"
);
INSERT INTO "grad" VALUES (1,'Pariz',2206488,1,35,2,2,'C:\\Users\\Belmin\\IdeaProjects\\rpr19-2parc-1t\\resources\\pictures\\La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques,_Paris_août_2014_(2).jpg');
INSERT INTO "grad" VALUES (2,'London',8825000,2,11,1,1,'C:\\Users\\Belmin\\IdeaProjects\\rpr19-2parc-1t\\resources\\pictures\\1200px-City_of_London,_seen_from_Tower_Bridge.jpg');
INSERT INTO "grad" VALUES (3,'Beč',1899055,3,542,3,2,'C:\\Users\\Belmin\\IdeaProjects\\rpr19-2parc-1t\\resources\\pictures\\8688.jpg');
INSERT INTO "grad" VALUES (4,'Manchester',545500,2,38,2,3,'C:\\Users\\Belmin\\IdeaProjects\\rpr19-2parc-1t\\resources\\pictures\\manchester-cityscape0.jpg');
INSERT INTO "grad" VALUES (5,'Graz',280200,3,353,3,3,'C:\\Users\\Belmin\\IdeaProjects\\rpr19-2parc-1t\\resources\\pictures\\1200px-GrazerRathaus-edit.jpg');
INSERT INTO "drzava" VALUES (1,'Francuska',1,1);
INSERT INTO "drzava" VALUES (2,'Velika Britanija',2,2);
INSERT INTO "drzava" VALUES (3,'Austrija',3,3);
INSERT INTO "tipGrada" VALUES (1,'Razvijeni grad');
INSERT INTO "tipGrada" VALUES (2,'Srednje razvijeni grad');
INSERT INTO "tipGrada" VALUES (3,'Nerazvijeni grad');
COMMIT;
