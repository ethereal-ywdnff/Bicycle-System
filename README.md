# Build-A-Bicycle Assignment

### Running the System

##### *Window OS*

1. ``cd`` into the src folder.

2. Build Application (make sure to ``cd`` into the ``src`` folder):

```bash
javac BicycleSystem.java controllers/*.java model/*.java model/users/*.java security/*.java view/*.java view/CustomerSystem/*.java view/LoginSystem/*.java view/MainWindow/*.java view/StaffSystem/*.java view/StaffSystem/Stocks/*.java view/StaffSystem/Stocks/Popup/*.java
```

3. Run the Application:
```bash
java -classpath .;.\lib\mysql-connector-java-8.0.30.jar BicycleSystem
```

##### *UNIX/MAC OS*

1. ``cd`` into the src folder.

2. Build Application (make sure to ``cd`` into the ``src`` folder):

```zsh
javac BicycleSystem.java controllers/*.java model/*.java model/users/*.java security/*.java view/*.java view/CustomerSystem/*.java view/LoginSystem/*.java view/MainWindow/*.java view/StaffSystem/*.java view/StaffSystem/Stocks/*.java view/StaffSystem/Stocks/Popup/*.java
```

3. Run the Application:
```zsh
java -cp .:./lib/mysql-connector-java-8.0.30.jar BicycleSystem
```

### Staff Login Details

Username | Password
--- | --- 
admin | admin
kush | admin
jonathan | admin
kanghua | admin
zhonghie | admin

### Main File
The entry point is located at ``BicycleSystem.java`` 

### Database Backup
Using ``data.sql`` the database can be populated if it has been cleared.
