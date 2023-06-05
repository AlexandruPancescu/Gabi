<?php
// create a bank account


require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] == "POST"){
    // the atrributes of the bank account: id, bankName, balance, currency, number

    if(
        isset($_POST['id']) and
            isset($_POST['bankName']) and
                isset($_POST['balance']) and
                    isset($_POST['currency']) and
                        isset($_POST['additionalInfo'])
    ){
        //operate the data further

    $db = new DbOperations();

    if( $db->createAccount(
          $_POST['id'],
          $_POST['bankName'],
          $_POST['balance'],
          $_POST['currency'],
          $_POST['additionalInfo']
     )){
    $response['error'] = false;
    $response['message'] = "Account created successfully";
     }else{
    $response['error'] = true;
    $response['message'] = "Some error occurred please try again";
     }


}
else{
    $response['error'] = true;
    $response['message'] = "Required fields are missing";

}


}else{
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
echo json_encode($response);