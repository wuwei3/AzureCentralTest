# AzureCentralTest
This is for IOT central test, the usage is like:

1, Change the values of properties in sensor-config.properties for your account in Azure
2, Go to root folder where it has pom.xml in command line, run command "mvn -clean package"
3, After build success, upload the "lib", "IOTAzureCentralTest-0.0.1-SNAPSHOT.jar" to the server under the same folder(if you have your own server, you also can run in your local machi)
4, Start 
  Linux with command "nohup  java -jar IOTAzureCentralTest-0.0.1-SNAPSHOT.jar >log.out &"
