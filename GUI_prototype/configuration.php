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


// Define variables and initialize with empty values
$cooldown_time = "";
$max_inj_amt = "";
$max_inj_amt = "";
$max_dose_amt = "";

$cooldown_time_err = "";
$max_inj_amt_err = "";
$min_inj_amt_err = "";
$max_dose_amt_err = "";


// Processing form data when form is submitted
if($_SERVER["REQUEST_METHOD"] == "POST"){
 
    // Check if cooldown_time is empty
    if(empty(trim($_POST["cooldown_time"]))){
        $cooldown_time_err = "Please enter cooldown time.";
    } else{
        $cooldown_time = trim($_POST["cooldown_time"]);
    }
    
    // Check if password is empty
    if(empty(trim($_POST["max_inj_amt"]))){
        $max_inj_amt_err = "Please enter your Max Injection Amountd.";
    } else{
        $max_inj_amt = trim($_POST["max_inj_amt"]);
    }

        // Check if password is empty
    if(empty(trim($_POST["min_inj_amt"]))){
        $min_inj_amt_err = "Please enter your Min Injection Amount.";
    } else{
        $min_inj_amt = trim($_POST["min_inj_amt"]);
    }

        // Check if password is empty
    if(empty(trim($_POST["max_dose_amt"]))){
        $max_dose_amt_err = "Please enter your password.";
    } else{
        $max_dose_amt = trim($_POST["max_dose_amt"]);
    }


    // Check input errors before updating the database
    // if(empty($cooldown_time_err) && empty($max_inj_amt_err) && empty($min_inj_amt_err) empty($max_dose_err)){
    if(empty($cooldown_time_err) && empty($max_inj_amt_err) && empty($min_inj_amt_err) && empty($max_dose_amt_err)){
        // Prepare an update statement
        $sql = "UPDATE `configuration` SET `cooldown_time` = ?, `max_inj_amnt` = ?, `min_inj_amnt` = ?, `max_cumm_dose` = ? WHERE `users_user_id` = ?";
        // $sql = "UPDATE configuration SET cooldown_time = ?  WHERE users_user_id = ?";



        if($stmt = mysqli_prepare($link, $sql)){
            // Bind variables to the prepared statement as parameters
            mysqli_stmt_bind_param($stmt, "dddsi", $param_cooldown_time, $param_max_inj_amt, $param_min_inj_amt, $param_max_dose_amt , $param_id);
            
            // Set parameters
            $param_password = password_hash($new_password, PASSWORD_DEFAULT);
            $param_cooldown_time = $cooldown_time;
            $param_max_inj_amt = $max_inj_amt;
            $param_min_inj_amt = $min_inj_amt;
            $param_max_dose_amt = $max_dose_amt;
            $param_id = $_SESSION["user_id"];

            
            
            // Attempt to execute the prepared statement
            if(mysqli_stmt_execute($stmt)){
                // Password updated successfully. Destroy the session, and redirect to login page
                mysqli_stmt_store_result($stmt);
                echo "Correctly stored";
                
                header("location: configuration.php");
                exit();
            } else{
                echo "Oops! Something went wrong. Please try again later.";
            }

            // Close statement
            mysqli_stmt_close($stmt);
        }
    }

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

        <br>

        <div>
            <label for="patient_name">Patient Name:</label>
            <input type="text" id="patient_name" name="patient_name"><br><br>
        </div>


        <h2> Configuration </h2>
        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post" >
            <div class="form-group <?php echo (!empty($cooldown_time_err)) ? 'has-error' : ''; ?>">

                <label for="cooldown_time">Cooldown time:</label>
                <input type="text" id="cooldown_time" name="cooldown_time">
                <span class="help-block"><?php echo $cooldown_time_err; ?></span>
            </div>   

            <div class="form-group <?php echo (!empty($max_inj_amt_err)) ? 'has-error' : ''; ?>">
                <label for="max_inj_amt">Max Injection Amount:</label>
                <input type="text" id="max_inj_amt" name="max_inj_amt">
                <span class="help-block"><?php echo $max_inj_amt_err; ?></span>
                
            </div>   

            <div class="form-group <?php echo (!empty($min_inj_amt_err)) ? 'has-error' : ''; ?>">
                <label for="min_inj_amt">Min Injection Amount:</label>
                <input type="text" id="min_inj_amt" name="min_inj_amt">
                <span class="help-block"><?php echo $min_inj_amt_err; ?></span>
                
            </div>   
            
            <div class="form-group <?php echo (!empty($max_dose_amt_err)) ? 'has-error' : ''; ?>">
                <label for="max_dose_amt">Max Daily Doses Count:</label>
                <input type="text" id="max_dose_amt" name="max_dose_amt">
                <span class="help-block"><?php echo $min_inj_amt_err; ?></span>
            </div>               


            <input type="submit" value="Submit">
        </form>


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