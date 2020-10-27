<?php
// Initialize the session
session_start();
 
// Check if the user is logged in, if not then redirect him to login page
if(!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true){
    header("location: login.php");
    exit;
}
?>
 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <style type="text/css">
        body{ font: 14px sans-serif; text-align: center; }
    </style>
</head>
<body>
    <div class="w3-sidebar w3-bar-block w3-card w3-animate-left" style="display:none" id="mySidebar">
        <button class="w3-bar-item w3-button w3-large"
        onclick="sidebar_close()">Close &times;</button>
        <a href="#" class="w3-bar-item w3-button">Link 1</a>
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

        <div class="w3-container">
            <p>In this example, the sidebar is hidden (style="display:none")</p>
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