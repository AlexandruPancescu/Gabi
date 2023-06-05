<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    if(
    isset($_POST['id']) and
        isset($_POST['value']) and
            isset($_POST['type']) and
                isset($_POST['category']) and
                    isset($_POST['date']) and
                        isset($_POST['description']) and
                            isset($_POST['parentCategory'])
    ){
        //operate the data further

    $db = new DbOperations();
   if( $db->createTransaction(
        $_POST['id'],
        $_POST['value'],
        $_POST['type'],
        $_POST['category'],
        $_POST['date'],
        $_POST['description'],
        $_POST['parentCategory']
    )){
   $response['error'] = false;
    $response['message'] = "Transaction created successfully";
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