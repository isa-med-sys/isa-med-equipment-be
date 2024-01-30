# Medical Equipment

## Description
The implemented web application serves as a information system for managing medical equipment procurement. Private hospitals can use the system to reserve and acquire the necessary medical equipment.
The system efficiently handles a significant number of registered companies, providing a comprehensive record of employees, registered companies, equipment reservations, pickup schedules, users, and their profiles. Its primary objective is to streamline the management of information related to personnel, companies, equipment reservations, and user profiles.

Application contains 5 projects:
- Main Application Backend
- [Main Application Frontend](https://github.com/nina-bu/isa-med-equipment-fe)
- [Location Simulator](https://github.com/anastasija1m/isa-med-equipment-location-simulator)
- [Extern Application Backend](https://github.com/nina-bu/isa-hospital-be)
- [Extern Application Frontend](https://github.com/nina-bu/isa-hospital-fe)

The Location Simulator mimics real-time vehicle movements, generating and sending coordinates to the central system for tracking. Meanwhile, the External Application facilitates the monthly delivery of specific equipment to subscribed private hospitals, allowing the creation and management of delivery contracts. The seamless communication between these applications is achieved through asynchronous message queues.

SQL [script](https://github.com/nina-bu/isa-med-equipment-be/blob/main/med_equipment/src/main/resources/data.sql) with test data.

## Installation and Usage
* Download and Install [RabbitMQ.](https://www.rabbitmq.com/download.html)
* Start RabbitMQ Server.
* Open IntelliJ IDEA.
* Click on File -> Open and select your project folder. IntelliJ IDEA should automatically recognize it as a Maven project.
* Install Dependencies from pom.xml file.
* Add necesary queues in [RabbitMQ Management Interface.](http://localhost:15672/)
* Run the application.
