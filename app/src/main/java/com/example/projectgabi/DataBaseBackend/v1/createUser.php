<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    if(
    isset($_POST['id']) and
        isset($_POST['firstName']) and
            isset($_POST['lastName']) and
                isset($_POST['email']) and
                    isset($_POST['phoneNumber']) and
                        isset($_POST['password'])
    ){
        //operate the data further

    $db = new DbOperations();

    if( $db->createUser(
          $_POST['id'],
          $_POST['firstName'],
          $_POST['lastName'],
          $_POST['email'],
          $_POST['phoneNumber'],
          $_POST['password']
     )){
    $response['error'] = false;
    $response['message'] = "User created successfully";
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