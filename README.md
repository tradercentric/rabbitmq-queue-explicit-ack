# rabbitmq-queue-explicit-ack

Learn SpringBoot RabbitMQ server, Non-SpringBoot RabbitMQ client, direct messaging with explicit acknowledgement </br>

# Prerequisite

Need jdk 1.8 installed </br>
Need Maven 3.x installed </br>
Need to installed and start RabbitMQ server </br>

# Build at directory where pom.xml located

mvn clean install </br>

# Start Server at directory where pom.xml located

java -jar target/acme-rabbitmq-services-1.0.jar </br>

# Start Client at directory where pom.xml located

java -cp target/acme-rabbitmq-services-1.0.jar -Dloader.main=com.acme.rabbitmq.MessageSender org.springframework.boot.loader.PropertiesLauncher </br>
