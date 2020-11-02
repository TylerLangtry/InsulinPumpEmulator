<?php

// define('DB_SERVER', 'databaseinsulinpump632.ca80da60sphk.us-east-2.rds.amazonaws.com');
// define('DB_USERNAME', 'master');
// define('DB_PASSWORD', 'Master1234');
// define('DB_NAME', 'main');

// /* Database credentials. Assuming you are running MySQL server with default setting (user 'root' with no password) */
// define('DB_SERVER', 'localhost');
// define('DB_USERNAME', 'root');
// define('DB_PASSWORD', '');
// define('DB_NAME', 'insulin_pump_final');


/* Database credentials. Assuming you are running MySQL server with default setting (user 'root' with no password) */
 define('DB_SERVER', 'insulin-pump-db.ccywbop2kswa.ap-southeast-2.rds.amazonaws.com:3306/');
 define('DB_USERNAME', 'master');
 define('DB_PASSWORD', 'Master1234');
 define('DB_NAME', 'insulinpumpdb');

//define('DB_SERVER', 'localhost');
//define('DB_USERNAME', 'root');
//define('DB_PASSWORD', '');
//define('DB_NAME', 'insulinpumpdb');




// define('DB_SERVER', 'databaseinsulinpump632.ca80da60sphk.us-east-2.rds.amazonaws.com');
// define('DB_USERNAME', 'master');
// define('DB_PASSWORD', 'Master1234');
// define('DB_NAME', 'main');




/* Attempt to connect to MySQL database */
$link = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_NAME);

// Check connection
if($link === false){
    die("ERROR: Could not connect. " . mysqli_connect_error());
}
?>