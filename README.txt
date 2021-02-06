Name: WGU Scheduling Application - C195
Purpose: Solve multi-location appointment scheduling needs of growing businesses
Author: Luis J. Gimenez
Email: lgimen3@wgu.edu
Version: 1.0.0
Date: 12-13-2020
IDE: IntelliJ Community Edition 2020.03
JDK: Java SE 11.0.9
JavaFX: JavaFX-SDK-15

How to Launch:

Using IntelliJ, go to Run/Debug Configuration and write in VM options: `--module-path [PATH_TO_FX] --add-modules javafx.controls,javafx.fxml`.
You have to install the following libraries: 

- JavaFX-SDK-15
- MySQL Connector (mysql-connector-java-8.0.21). 

After installation of those libraries, the application should run.

This program has been designed for simple visibility and is very intuitive. You're going to be able to arrange appointments and handle the appointments in a gorgeous interface. You will also have access to reports for bookkeeping. As a developer, I preffered to concentrate on locations filtering on the Reporting page as this program is likely to end up in multi-location companies. Other functions, such as automatic translation to French when an user from that location logs in are part of the package.

In Reports: I decided todo the categories of appointments by their location. This populated all appointments that have the same location.

