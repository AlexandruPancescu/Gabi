<?php

require_once "../includes/DbOperations.php";

if($_SERVER["REQUEST_METHOD"] == "GET"){
    $db = new DbOperations();
    $transactions = $db->getAccounts();
    $response = array(
        "error" => false,
        "message" => "Request successfully completed",
        "accounts" => $transactions
    ); 
}else{
    $response = array(
        "error" => true,
        "message" => "Invalid Request"
    );
}

header("Content-Type: application/json");
echo json_encode($response);