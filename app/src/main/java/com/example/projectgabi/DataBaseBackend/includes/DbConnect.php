<?php

class DbConnect {
    // Variable to store database link
    private $con;

    // Class constructor
    function __construct() {

    }

    // This method will connect to the database
    function connect() {

        // Including the constants.php file to get the database constants
        include_once dirname(__FILE__) . '/Constants.php';

        // Connecting to mysql database with mysqli extension and storing the connection in $con variable 
        $this->con = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        // Checking if any error occured while connecting
        if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL database: " . mysqli_connect_err();
        }

        return $this->con;
    }
}