<?php

require_once '../includes/DbOperations.php';

$response = array();

// if operation is called with DELETE method delete budget and budget items
if($_SERVER['REQUEST_METHOD']=='DELETE'){

    if(isset($_GET['id'])){
        //operate the data further

    $db = new DbOperations();

    if( $db->deleteBudget(
          $_GET['id']
     )){
    $response['error'] = false;
    $response['message'] = "Budget deleted successfully";
    }else{
    $response['error'] = true;
    $response['message'] = "Budget id not found";
    }
    }else{
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    
    }
    
    }
    
    else{
       
        $response['error'] = true;
        $response['message'] = "invalid Request";
    }
    echo json_encode($response);