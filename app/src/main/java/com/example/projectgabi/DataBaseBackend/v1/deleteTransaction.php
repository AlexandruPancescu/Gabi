<?php


require_once '../includes/DbOperations.php';
$response = array();




// delete a transaction based on id using the DELETE method

if($_SERVER['REQUEST_METHOD']=="DELETE"){
 
    


    if(isset($_GET['id'])){
        //operate the data further

    $db = new DbOperations();

    if( $db->deleteTransaction(
          $_GET['id']
     )){
    $response['error'] = false;
    $response['message'] = "Transaction deleted successfully";
    }else{
    $response['error'] = true;
    $response['message'] = "Some error occurred please try again";
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
