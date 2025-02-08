# Local Command References
- `javac -cp "lib/ESMSlibV4.0.jar:lib/jakarta.ws.rs-api-3.1.0.jar:lib/jakarta.enterprise.cdi-api-4.1.0.jar:lib/jakarta.inject-api-2.0.1.jar:lib/jakarta.mail-api-2.1.3.jar:lib/jackson-annotations-2.18.2.jar:lib/jackson-databind-2.18.2.jar" -d out/WEB-INF/classes src/java/org/carecode/messenger/**/*.java` - Compile
- `cp config.properties out/WEB-INF/classes` - Copy config.properties with testing email server details
- `mkdir out/WEB-INF/lib` - Create out/WEB-INF/lib/
- `cp lib/ESMSlibV4.0.jar out/WEB-INF/lib/` - Copy ESMSlibV4.0.jar to out/WEB-INF/lib/
- `jar -cvf out/messenger.war -C out/ .` - Create messenger.war

## Payara Server Command References
- `~/Documents/Payara/payara5/bin/asadmin start-domain` - Start Payara Server
- `~/Documents/Payara/payara5/bin/asadmin deploy out/messenger.war` - Deploy messenger.war
- `~/Documents/Payara/payara5/bin/asadmin undeploy messenger` - Undeploy messenger
- `~/Documents/Payara/payara5/bin/asadmin stop-domain` - Stop Payara Server

## Payara Micro Command References
- `java -jar payara-micro-6.2025.1.jar --deploy out/messenger.war --port 8081` - Start Payara Micro and deploy messenger.war
