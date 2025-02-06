## Local Command References
- `javac -cp "lib/ESMSlibV4.0.jar:lib/jakarta.ws.rs-api-3.1.0.jar:lib/jakarta.enterprise.cdi-api-4.1.0.jar:lib/jakarta.inject-api-2.0.1.jar:lib/jakarta.mail-api-2.1.3.jar:lib/jackson-annotations-2.18.2.jar:lib/jackson-databind-2.18.2.jar" -d out/WEB-INF/classes src/java/org/carecode/messenger/**/*.java` - Compile
- `cp config.properties out/WEB-INF/classes` - Copy config.properties with testing email server details
- `jar cvf out/WEB-INF/lib/ESMSlibV4.0.jar -C lib .` - Create ESMSlibV4.0.jar
- `~/Documents/Payara/payara5/bin/asadmin start-domain` - Start Payara Server
- `~/Documents/Payara/payara5/bin/asadmin deploy out/messenger.war` - Deploy messenger.war
- `~/Documents/Payara/payara5/bin/asadmin undeploy messenger` - Undeploy messenger
- `~/Documents/Payara/payara5/bin/asadmin stop-domain` - Stop Payara Server
