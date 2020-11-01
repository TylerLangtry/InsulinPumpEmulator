<?php
// Initialize the session
session_start();
 
// Check if the user is logged in, if not then redirect him to login page
if(!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true){
    header("location: login.php");
    exit;
}

// Include config file
require_once "config.php";
?>


 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script src="line_chart.js"></script>

    <style type="text/css">
        body{ font: 14px sans-serif; text-align: center; }
    </style>

</head>
<body>
    <div class="w3-sidebar w3-bar-block w3-card w3-animate-left" style="display:none" id="mySidebar">
        <button class="w3-bar-item w3-button w3-large"
        onclick="sidebar_close()">Close &times;</button>
        <a href="dashboard.php" class="w3-bar-item w3-button">Dashboard</a>
        <a href="configuration.php" class="w3-bar-item w3-button">Configuration</a>
        <a href="reset-password.php" class="w3-bar-item w3-button">Reset Your Password</a>
        <a href="logout.php" class="w3-bar-item w3-button">Sign Out of Your Account</a>
    </div>
    <div id="main">

        <div class="w3-teal" >
            <div class=w3-container align="left">
                <button id="openNav" class="w3-button w3-teal w3-xlarge" onclick="sidebar_open()">&#9776;</button>
            </div>

            <div class="w3-container">
                <h1>Hi, <b><?php echo htmlspecialchars($_SESSION["username"]); ?></b>. Welcome to the Insulin Pump Emulator</h1>
            </div>
        </div>



        <label for="battery_progress">Battery level:</label>
        <progress id="battery_progress" value="32" max="100"> 32% </progress>
        <br>
        <label for="insulin_progress">Insulin Reserve:</label>
        <progress id="insulin_progress" value=40 max="100"> 40% </progress>

        <br>

        <div id="curve_chart" style="width: 900px; height: 500px"></div>

        <br>

        <h1> Past Alerts </h1>

        <div >
            <p id="alarm_box">text here </p>
        </div>
    </div>


    <script>
    function sidebar_open() {
    document.getElementById("main").style.marginLeft = "25%";
    document.getElementById("mySidebar").style.width = "25%";
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("openNav").style.display = 'none';
    }
    function sidebar_close() {
    document.getElementById("main").style.marginLeft = "0%";
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("openNav").style.display = "inline-block";
    }
    </script>
</body>
</html>