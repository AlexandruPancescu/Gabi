<?php

require_once '../includes/DbOperations.php';

$response = array();

// create budget if the request method is POST

if($_SERVER['REQUEST_METHOD']=='POST'){
    // check the parameters required for this request
    if(isset($_POST['PK_BudgetID']) and
        isset($_POST['startDate']) and
        isset($_POST['endDate']) and
        isset($_POST['FK_UserID'])){
            // operate the data further
            $db = new DbOperations();
            // call the function to create the budget
            $result = $db->createBudget($_POST['PK_BudgetID'],
                                        $_POST['startDate'],
                                        $_POST['endDate'],
                                        $_POST['FK_UserID']
                                    );
            // check if the budget was created successfully
            if($result == 1){
                // if the budget was created successfully
                $response['error'] = false;
                $response['message'] = "Budget created successfully";
            }elseif($result == 2){
                // if the budget was not created successfully
                $response['error'] = true;
                $response['message'] = "Some error occurred, please try again";
            }elseif($result == 0){
                // if the budget was not created successfully
                $response['error'] = true;
                $response['message'] = "It seems you have already created a budget";
            }
        }else{
            // if the required parameters are not available
            $response['error'] = true;
            $response['message'] = "Required parameters are missing";
        }


}else{
    // if the request method is not POST
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
        echo json_encode($response);