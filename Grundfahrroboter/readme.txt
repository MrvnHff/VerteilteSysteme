Readme f�r Projekt verteilte Robotersteuerung.

Um den Server mit Oberfl�che zu starten muss die Datei Gui.java im Paket server.gui gestartet werden.
Die Parameter mit welchen der Server gestartet wird sind in der Datei serverStart.properties in resources.config zu finden.

Der Server kann auch unabh�ngig von der GUI �ber die Konsole gestartet werden, da dann aber kein Zugriff auf die Steuerung m�glich ist,
macht das nicht sonderlich viel Sinn.

Der Einstiegspunkt f�r die virtuellen Roboter ist die StartAutomatisation.java in test.virtualRobot. Diese legt einen Manger an und f�hrt die im Code
angelegte Methodensequenz auf den Manger aus. So ist es m�glich zum Testen Roboter hinzuzuf�gen, entfernen, zu verbinden oder zu trennen.