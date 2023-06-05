<?php

require_once '../includes/DbOperations.php';

$response = array();

// get a all budget items
if($_SERVER['REQUEST_METHOD']=='GET'){

    $db = new DbOperations();

    $budgetItems = $db->getBudgetItems();
    
    $response = array(
        'error' => false,
        'message' => 'Request successfully completed',
        'budget items' => $budgetItems
    );
}else{
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}

header('Content-Type: application/json');
echo json_encode($response);
