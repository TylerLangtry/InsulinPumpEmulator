
This prototype uses local host of Apache and MySQL servers via XAMPP Contol Panel v3.2.4

## Change Document Root ##
The local directory was changed for the GUI prototype and for Github push functionality.
https://stackoverflow.com/questions/1408/make-xampp-apache-serve-file-outside-of-htdocs-folder
Edit ~line 176 in C:\xampp\apache\conf\httpd.conf; change #DocumentRoot "C:/xampp/htdocs" to #DocumentRoot "C:/Projects" (or ...\IdeaProjects\InsulinPumpEmulator).
Edit ~line 203 to match your new location (in this case C:/Projects).

## Create MySQL database **
The MySQL "users" database was generated using local.phpadmin with the following query:
CREATE TABLE users (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


