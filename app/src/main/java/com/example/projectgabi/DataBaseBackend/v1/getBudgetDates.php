<?php 

require_once '../includes/DbOperations.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='GET'){
    $db = new DbOperations();
    $budgetDates = $db->getBudgetDates();
    $response = array(
        'error' => false,
        'message' => 'Request successfully completed',
        'budget dates' => $budgetDates
    );
}else{
    $response = array(
        'error' => true,
        'message' => 'Invalid Request'
    );
}

header('Content-Type: application/json');
echo json_encode($response);