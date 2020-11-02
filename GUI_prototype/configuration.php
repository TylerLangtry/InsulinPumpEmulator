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
$min_inj_amt = "";
$max_dose_amt = "";

$cooldown_time_err = "";
$max_inj_amt_err = "";
$min_inj_amt_err = "";
$max_dose_amt_err = "";

$patient_id = "";
$patient_id_err = "";


// Processing form data when form is submitted
if($_SERVER["REQUEST_METHOD"] == "POST"){

    if (isset($_POST['btn_load'])) {

        // Check if password is empty
        if(empty(trim($_POST["patient_id"]))){
            $patient_id_err = "Please enter your user id.";
        } else{
            $patient_id = trim($_POST["patient_id"]);
        }

        // $sql_load = "SELECT `cooldown_time`,`max_inj_amnt`,`min_inj_amnt`,`max_cumm_dose` FROM `configuration` WHERE `configuration_id` = ? " ; 
        $sql_load = "SELECT `cooldown_time`,`max_inj_amnt`,`min_inj_amnt`,`max_cumm_dose`  FROM `configuration` WHERE `users_user_id` = ? " ; 

        if(empty($patient_id_err)){
            if($stmt = mysqli_prepare($link, $sql_load)){
                // Bind variables to the prepared statement as parameters
                mysqli_stmt_bind_param($stmt, "i", $param_patient_id );
                
                // Set parameters
                $param_patient_id = $patient_id;

            
                
                // Attempt to execute the prepared statement
                if(mysqli_stmt_execute($stmt)){
                    // Store result
                    mysqli_stmt_store_result($stmt);
                    
                    // Check
                    // $count = mysqli_stmt_num_rows($stmt);
                    // print("Number of rows in the table: ".$count."\n");
                    
                    // Check if user id exists
                    if(mysqli_stmt_num_rows($stmt) == 1){                    
                        // Bind result variables
                        mysqli_stmt_bind_result($stmt, $result_cooldown_time, $result_max_inj_amnt, $result_min_inj_amnt, $result_max_cumm_dose);
                        if(mysqli_stmt_fetch($stmt)){
                        // Save MYSQL values to php values
                            

                            print("Cooldown: ".$result_cooldown_time."\n");

                            $cooldown_time =  $result_cooldown_time;
                            $max_inj_amt = $result_max_inj_amnt;
                            $min_inj_amt = $result_min_inj_amnt;
                            $max_dose_amt = $result_max_cumm_dose; 
                        } else{
                            // Display an error message if password is not valid
                            echo "Fetch went wrong";
                        }
                    } else{
                        // Display an error message if username doesn't exist
                        $patient_id_err = "No configuration with that user id.";
                    }
                } else {
                    echo "Oops! Something went wrong. Please try again later.";
                }

                // Close statement
                mysqli_stmt_close($stmt);
            }
        }
    }

    if (isset($_POST['btn_submit'])) {
 
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

        // Check if password is empty
        if(empty(trim($_POST["patient_id"]))){
            $patient_id_err = "Please enter patient user id";
        } else{
            $patient_id = trim($_POST["patient_id"]);
        }



        // Check input errors before updating the database
        if(empty($cooldown_time_err) && empty($max_inj_amt_err) && empty($min_inj_amt_err) && empty($max_dose_amt_err) && empty($patient_id_err)){
            // Prepare an update statement
            // $sql = "UPDATE `configuration` SET `cooldown_time` = ?, `max_inj_amnt` = ?, `min_inj_amnt` = ?, `max_cumm_dose` = ? WHERE `users_user_id` = ?";
            $sql = "UPDATE `configuration` SET `cooldown_time` = ?, `max_inj_amnt` = ?, `min_inj_amnt` = ?, `max_cumm_dose` = ? WHERE (`users_user_id` = ?)";
            
            // $sql = "UPDATE configuration SET cooldown_time = ?  WHERE users_user_id = ?";

            printf("Hello world");

            if($stmt = mysqli_prepare($link, $sql)){
                // Bind variables to the prepared statement as parameters
                mysqli_stmt_bind_param($stmt, "dddsi", $param_cooldown_time, $param_max_inj_amt, $param_min_inj_amt, $param_max_dose_amt , $param_id);
                printf("Hello world 2");

                // Set parameters
                $param_cooldown_time = $cooldown_time;
                $param_max_inj_amt = $max_inj_amt;
                $param_min_inj_amt = $min_inj_amt;
                $param_max_dose_amt = $max_dose_amt;
                // $param_id = $_SESSION["user_id"];            // TODO change line to patient select
                $param_id = $patient_id;

                
                
                // Attempt to execute the prepared statement
                if(mysqli_stmt_execute($stmt)){
                    printf("Hello world 3");

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

        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post" >
            <div>
            
            <label for="patient_id">Patient Select:</label>
                <input type="text" id="patient_id" name="patient_id" value = <?php echo $patient_id; ?>>
                <span class="help-block"><?php echo $patient_id_err; ?></span>

            </div>

            <input type="submit" name="btn_load" value="Load">
        <!-- </form> -->


        <h2> Configuration </h2>
        <!-- <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post" > -->
            <div class="form-group <?php echo (!empty($cooldown_time_err)) ? 'has-error' : ''; ?>">

                <label for="cooldown_time">Cooldown time:</label>
                <input type="text" id="cooldown_time" name="cooldown_time" value = <?php echo $cooldown_time; ?> > 
                <span class="help-block"><?php echo $cooldown_time_err; ?></span>
            </div>   

            <div class="form-group <?php echo (!empty($max_inj_amt_err)) ? 'has-error' : ''; ?>">
                <label for="max_inj_amt">Max Injection Amount:</label>
                <input type="text" id="max_inj_amt" name="max_inj_amt" value = <?php echo $max_inj_amt; ?> > 
                <span class="help-block"><?php echo $max_inj_amt_err; ?></span>
                
                
            </div>   

            <div class="form-group <?php echo (!empty($min_inj_amt_err)) ? 'has-error' : ''; ?>">
                <label for="min_inj_amt">Min Injection Amount:</label>
                <input type="text" id="min_inj_amt" name="min_inj_amt" value = <?php echo $min_inj_amt; ?> > 
                <span class="help-block"><?php echo $min_inj_amt_err; ?></span>
                
            </div>   
            
            <div class="form-group <?php echo (!empty($max_dose_amt_err)) ? 'has-error' : ''; ?>">
                <label for="max_dose_amt">Max Daily Doses Count:</label>
                <input type="text" id="max_dose_amt" name="max_dose_amt" value = <?php echo $max_dose_amt; ?> > 
                <span class="help-block"><?php echo $min_inj_amt_err; ?></span>
            </div>               


            <input type="submit" name="btn_submit" value="Submit">
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