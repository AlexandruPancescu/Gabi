<?php

require_once '../includes/DbOperations.php';
$response = array();
// create a budget item if the request method is POST
// budget item has atributtes: PK_BudgetID, PK_CategoryID, BudgetAmount
if($_SERVER["REQUEST_METHOD"] == "POST"){

    if(isset($_POST['PK_BudgetID']) and
        isset($_POST['PK_CategoryID']) and
        isset($_POST['BudgetAmount'])){
            // operate the data further
            $db = new DbOperations();
            // call the function to create the budget item
            $result = $db->createBudgetItem($_POST['PK_BudgetID'],
                                        $_POST['PK_CategoryID'],
                                        $_POST['BudgetAmount']
                                    );
            // check if the budget item was created successfully
            if($result == 1){
                // if the budget item was created successfully
                $response['error'] = false;
                $response['message'] = "Budget item created successfully";
            }elseif($result == 2){
                // if the budget item was not created successfully
                $response['error'] = true;
                $response['message'] = "Some error occurred, please try again";
            }elseif($result == 0){
                // if the budget item was not created successfully
                $response['error'] = true;
                $response['message'] = "It seems you have already created a budget item";
            }
        }else{
            // if the required parameters are not available
            $response['error'] = true;
            $response['message'] = "Required parameters are missing";
        }

}else{
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
echo json_encode($response);