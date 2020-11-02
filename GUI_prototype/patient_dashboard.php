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


$battery_charge =  "";
$reserve_amnt = "";
$alert = "";


// $results = array();

// $data = array (
//     'cols' => array( 
//         array('id' => 'date', 'label' => 'date', 'type' => 'datetime'), 
//         array('id' => 'blood_sug_lvl_a', 'label' => 'Blood Sugar Lvl', 'type' => 'number'),
//         array('id' => 'inj_amnt_a', 'label' => 'Insulin Injection Amnt', 'type' => 'number')),
//     'rows' => array()
//     );
$dataBlood = array (
    'cols' => array( 
        array('id' => 'date', 'label' => 'date', 'type' => 'datetime'), 
        array('id' => 'blood_sug_lvl_a', 'label' => 'Blood Sugar Lvl', 'type' => 'number')),
    'rows' => array()
    );

$dataInjection = array (
    'cols' => array( 
        array('id' => 'date', 'label' => 'date', 'type' => 'datetime'), 
        array('id' => 'inj_amnt_a', 'label' => 'Insulin Injection Amnt', 'type' => 'number')),
    'rows' => array()
    );

$dataAlerts = array ();


$sql_load = "SELECT `battery_charge`, `reserve_amnt`, `alert`  FROM `status` WHERE `users_user_id` = ? " ;

if($stmt = mysqli_prepare($link, $sql_load)){
    // Bind variables to the prepared statement as parameters
    mysqli_stmt_bind_param($stmt, "i", $param_user_id );
    
    // Set parameters
    $param_user_id = $_SESSION["user_id"];
    
    // Attempt to execute the prepared statement
    if(mysqli_stmt_execute($stmt)){
        // Store result
        mysqli_stmt_store_result($stmt);
        
        // Check if user id exists
        if(mysqli_stmt_num_rows($stmt) == 1){                    
            // Bind result variables
            mysqli_stmt_bind_result($stmt, $param_battery_charge, $param_reserve_amnt, $param_alert);
            if(mysqli_stmt_fetch($stmt)){
                // Save MYSQL values to php local values
                $battery_charge = $param_battery_charge ;
                $reserve_amnt = $param_reserve_amnt;
                
                // Check If alert is not null
                if( $param_alert != NULL){
                    $alert = $param_alert;
                }

            } else{
                // Display an error message if password is not valid
                echo "Fetch went wrong";
            }
        }

    } else {
        echo "Oops! Something went wrong. Please try again later.";
    }

    // Close statement
    mysqli_stmt_close($stmt);
}

$sql_chart = "SELECT `blood_sug_lvl`,`inj_amnt`, `last_update` FROM data WHERE `users_user_id` = ? ORDER BY `last_update` DESC limit 3";

if($stmt = mysqli_prepare($link, $sql_chart)){
    // Bind variables to the prepared statement as parameters
    mysqli_stmt_bind_param($stmt, "i", $param_user_id );
    
    // Set parameters
    $param_user_id = $_SESSION["user_id"];
    
    // Attempt to execute the prepared statement
    if(mysqli_stmt_execute($stmt)){
        // Store result
        mysqli_stmt_store_result($stmt);
    
        // Check if user id exists
        if(mysqli_stmt_num_rows($stmt) > 0){                    
            // Bind result variables
            mysqli_stmt_bind_result($stmt, $param_blood_sug_lvl, $param_inj_amnt, $param_last_update);


            /* fetch values */

            while (mysqli_stmt_fetch($stmt)) {


                preg_match('/(\d{4})-(\d{2})-(\d{2})\s(\d{2}):(\d{2}):(\d{2})/', $param_last_update, $match);
                $year = (int) $match[1];
                $month = (int) $match[2] - 1; // convert to zero-index to match javascript's dates
                $day = (int) $match[3];
                $hours = (int) $match[4];
                $minutes = (int) $match[5];
                $seconds = (int) $match[6];

                array_push($dataBlood['rows'], array('c' => array(
                    array('v' => "Date($year, $month, $day, $hours, $minutes, $seconds)"), 
                    array('v' => $param_blood_sug_lvl)
                )));

                array_push($dataInjection['rows'], array('c' => array(
                    array('v' => "Date($year, $month, $day, $hours, $minutes, $seconds)"), 
                    array('v' => $param_inj_amnt)
                )));

                // Save to Injection History Plot
                $last_update= $param_last_update;
                $inj_amnt = $param_inj_amnt;
            }
            
        } 
    } else {
        echo "Oops! Something went wrong. Please try again later.";
    }
    // Close statement
    mysqli_stmt_close($stmt);
}

$sql_alerts = "SELECT `alert`,`last_update` FROM `status` WHERE `users_user_id` = ? and `alert` is NOT NULL ORDER BY `last_update` DESC limit 10" ;

if($stmt = mysqli_prepare($link, $sql_alerts)){
    // Bind variables to the prepared statement as parameters
    mysqli_stmt_bind_param($stmt, "i", $param_user_id );
    
    // Set parameters
    $param_user_id = $_SESSION["user_id"];
    
    // Attempt to execute the prepared statement
    if(mysqli_stmt_execute($stmt)){
        // Store result
        mysqli_stmt_store_result($stmt);
        
        // Check if user id exists
        if(mysqli_stmt_num_rows($stmt) > 0){                    
            // Bind result variables
            mysqli_stmt_bind_result($stmt,  $param_alert, $param_last_update);
            if(mysqli_stmt_fetch($stmt)){
                // Save MYSQL values to php local values

                array_push($dataAlerts,
                    array('alert' => $param_alert, 'dates' => $param_last_update)
                );

            } else{
                // Display an error message if password is not valid
                echo "Fetch went wrong";
            }
        } 
    } else {
        echo "Oops! Something went wrong. Please try again later.";
    }

    // Close statement
    mysqli_stmt_close($stmt);
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
    <!-- <script src="line_chart.js"></script> -->
    <script type="text/javascript">

        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawBloodChart);
        google.charts.setOnLoadCallback(drawInsulinChart);

        function drawBloodChart() {
        var data = new google.visualization.DataTable(<?php echo json_encode($dataBlood); ?>);    
 
        $i = 0;
        $numofloops = 3;

        var options = {
            title: 'Blood sugar Level',
            curveType: 'function',
            legend: { position: 'bottom' },
            colors: ['blue'],
            vAxis: {    
                    viewWindow: {
                        min: 40,
                        max: 300}},
        };

        var chart = new google.visualization.LineChart(document.getElementById('blood_chart'));

        chart.draw(data, options);
        }

        function drawInsulinChart() {
        var data = new google.visualization.DataTable(<?php echo json_encode($dataInjection); ?>);    
 
        $i = 0;
        $numofloops = 3;

        var options = {
            title: 'Insulin Injection Amount',
            curveType: 'function',
            colors: ['red'],
            legend: { position: 'bottom' },
            vAxis: {    
                    viewWindow: {
                        min: 0,
                        max: 10}},
        };

        var chart = new google.visualization.LineChart(document.getElementById('insulin_chart'));

        chart.draw(data, options);
        }
    </script>

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
        <progress id="battery_progress" value= <?php echo $battery_charge; ?>  max="100"> </progress>
        <br>
        <label for="insulin_progress">Insulin Reserve:</label>
        <progress id="insulin_progress" value=<?php echo $reserve_amnt; ?>  max="100"> max="100"> </progress>

        <br>

        <div id="blood_chart" style="display: inline-block; width: 600px; height: 300px"></div>
        <div id="insulin_chart" style="display: inline-block; width: 600px; height: 300px"></div>

        <br>

        <h1> Past Alerts </h1>

        <div >
            <p id="alarm_box" > <?php
                foreach($dataAlerts as $item) {
                    echo $item['dates'], "   "; 
                    echo $item['alert'], '<br>'; 
                }
            ?></p>
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