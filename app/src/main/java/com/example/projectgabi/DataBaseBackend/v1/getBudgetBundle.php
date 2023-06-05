<?php

require_once '../includes/DbOperations.php';

$response = array();
// get the budget from the database based on the start and end date

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // check the parameters required for this request
    if (
        isset($_GET['startDate']) and
        isset($_GET['endDate'])
    ) {
        // operate the data further
        $db = new DbOperations();
        // call the function to get the budget
        if (
            $db->getBudgetWithBudgetItems(
                $_GET['startDate'],
                $_GET['endDate']
            )
        ) {
            $response["bundle"] = $db->getBudgetWithBudgetItems(
                $_GET['startDate'],
                $_GET['endDate']
            );
            $response['error'] = false;
            $response['message'] = "Budget retrieved successfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Some error occurred please try again";
        }



    } else {
        // if the required parameters are not available
        $response['error'] = true;
        $response['message'] = "Error occured";
    }

} else {
    // if the request method is not GET
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}


header('Content-Type: application/json');
echo json_encode($response);