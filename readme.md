läuft unter jdk 1.8
Zertifikat läuft regelmäßig ab und muss erneuert werden

# Keystore:
###Auflistung aller Einträge des Keystore
keytool -list -keystore keystore.p12 -storepass 123456

###Generiere Schlüsselpaar und füge es in self-signed certificate in keystore.p12
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

die Frage nach Vor- und Nachname muss mit 
localhost
beantwortet werden
###exportieren des erzeugten zertifikat als server.cer
keytool -export -alias tomcat -storepass 123456 -file server.cer -keystore keystore.p12

das exportierte selbstsignierte zertifikat musste früher im ausführenden jre zu cacerts hinzugefügt werden 
keytool -import -v -trustcacerts -alias server-alias -file server.cer -keystore <keystore_name.jks>

ein p12-Zertifikat kann gleichzeitig trustore sein siehe application.properties

In IntelliJ:
Preferences->Tools->Server Certificates importieren und Haken setzen bei
accept non-trusted certificates automtacally

###Integrationstests mit testcontainers und mysql statt h2

