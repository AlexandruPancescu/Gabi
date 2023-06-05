<?php

require_once '../includes/DbOperations.php';

$response = array();

// create a cateogry with atributtes: PK_CategoryID, Name, Amount, ParentCategory
if($_SERVER["REQUEST_METHOD"] == "POST"){

    if(isset($_POST['PK_CategoryID']) and
        isset($_POST['Name']) and
        isset($_POST['Amount']) and
        isset($_POST['ParentCategory'])){
            // operate the data further
            $db = new DbOperations();
            // call the function to create the category
            $result = $db->createCategory($_POST['PK_CategoryID'],
                                        $_POST['Name'],
                                        $_POST['Amount'],
                                        $_POST['ParentCategory']
                                    );
            // check if the category was created successfully
            if($result == 1){
                // if the category was created successfully
                $response['error'] = false;
                $response['message'] = "Category created successfully";
            }elseif($result == 2){
                // if the category was not created successfully
                $response['error'] = true;
                $response['message'] = "Some error occurred, please try again";
            }elseif($result == 0){
                // if the category was not created successfully
                $response['error'] = true;
                $response['message'] = "It seems you have already created a category";
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