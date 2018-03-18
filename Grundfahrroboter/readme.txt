Readme für Projekt verteilte Robotersteuerung.

Um den Server mit Oberfläche zu starten muss die Datei Gui.java im Paket server.gui gestartet werden.
Die Parameter mit welchen der Server gestartet wird sind in der Datei serverStart.properties in resources.config zu finden.

Der Server kann auch unabhängig von der GUI über die Konsole gestartet werden, da dann aber kein Zugriff auf die Steuerung möglich ist,
macht das nicht sonderlich viel Sinn.

Der Einstiegspunkt für die virtuellen Roboter ist die StartAutomatisation.java in test.virtualRobot. Diese legt einen Manger an und führt die im Code
angelegte Methodensequenz auf den Manger aus. So ist es möglich zum Testen Roboter hinzuzufügen, entfernen, zu verbinden oder zu trennen.