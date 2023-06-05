<?php

class DbOperations{
    // Variable to store database link
    private $con;

    // Class constructor
    function __construct(){
        require_once dirname(__FILE__).'/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }

    /* CRUD */

    // Creating new transaction
    public function createTransaction($id, $value, $type, $category, $date, $description, $parentCategory){
        $stmt = $this->con->prepare("INSERT INTO `transactions` (`PK_TransactionID` , `Value`, `Type`, `Category`, `Date`, `Description`, `ParentCategory`)
         VALUES ( ?, ?, ?, ?, ?, ?, ?)");
         // echo something to check if the query is correct
         //echo "INSERT INTO `transactions` (`PK_TransactionID` , `Value`, `Type`, `Category`, `Date`, `Description`)"; 
         // bind param means that the value of the variable is passed to the query
        $stmt->bind_param("sssssss", $id, $value, $type, $category, $date, $description, $parentCategory);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }
    }

    public function getTransactions(){
       // get the values from the query
        $stmt = $this->con->prepare("SELECT `PK_TransactionID`, `Value`, `Type`, `Category`, `Date`, `Description`, `ParentCategory` FROM `transactions`");
        $stmt->execute();
        $stmt->bind_result($id, $value, $type, $category, $date, $description, $parentCategory);
        $transactions = array();
        while($stmt->fetch()){
            $transaction = array();
            $transaction['id'] = $id;
            $transaction['value'] = $value;
            $transaction['type'] = $type;
            $transaction['category'] = $category;
            $transaction['date'] = $date;
            $transaction['description'] = $description;
            $transaction['parentCategory'] = $parentCategory;
            array_push($transactions, $transaction);
        }
        return $transactions;
        

    }


    //

    // define isUserExist function
    private function isUserExist($email){
        $stmt = $this->con->prepare("SELECT PK_UserID FROM users WHERE Email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        // if the user exists, return true
        return $stmt->num_rows > 0;
    }
    // create a new user with the parameters: first name, last name, email, phone number  and password
    public function createUser($id, $firstname, $lastname, $email, $phonenumber, $password){
        // check if the user already exists
      //   if(!$this->isUserExist($email)){
            // if the user does not exist, create a new user
            $stmt = $this->con->prepare("INSERT INTO `users` (`PK_UserID`, `FirstName`, `LastName`, `Email`, `PhoneNumber`, `Password`) VALUES (?, ?, ?, ?, ?, ?);");
            // encode the password
           $password = md5($password);
            $stmt->bind_param("ssssss", $id,  $firstname, $lastname, $email, $phonenumber, $password);
            if($stmt->execute()){
                return true;
            }else{
                return false;
            }
   //    }
       
    }

    public function createAccount($id, $bankName, $balance, $currency, $additionalInfo){
        $stmt = $this->con->prepare("INSERT INTO `accounts` (`PK_AccountID`, `BankName`, `Balance`, `Currency`, `AdditionalInfo`) VALUES (?, ?, ?, ?, ?);");
        $stmt->bind_param("sssss", $id, $bankName, $balance, $currency, $additionalInfo);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }
    }

    public function isTransactionExist($id){
        $stmt = $this->con->prepare("SELECT PK_TransactionID FROM transactions WHERE PK_TransactionID = ?");
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $stmt->store_result();
        // if the transaction exists, return true
        return $stmt->num_rows > 0;
    }

    //delete a transaction based on the id
    public function deleteTransaction($id){
        // if the id does not exist, return false
        if(!$this->isTransactionExist($id)){
            return false;
        }

        $stmt = $this->con->prepare("DELETE FROM `transactions` WHERE `PK_TransactionID` = ?");
        $stmt->bind_param("s", $id);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }
    }

    // get all the bank accounts from the db 
    public function  getAccounts(){
        $stmt = $this->con->prepare("SELECT `PK_AccountID`, `BankName`, `Balance`, `Currency`, `AdditionalInfo` FROM `accounts`");
        $stmt->execute();
        $stmt->bind_result($id, $bankName, $balance, $currency, $additionalInfo);
        $accounts = array();
        while($stmt->fetch()){
            $account = array();
            $account['id'] = $id;
            $account['bankName'] = $bankName;
            $account['balance'] = $balance;
            $account['currency'] = $currency;
            $account['additionalInfo'] = $additionalInfo;
            array_push($accounts, $account);
        }
        return $accounts;
    }

    // budget has id, startDate, endDate and a user foreign key
    public function createBudget($PK_BudgetID, $startDate, $endDate, $FK_UserID){

        $stmt = $this->con->prepare("INSERT INTO `budget` (`PK_BudgetID`, `StartDate`, `EndDate`, `FK_UserID`) VALUES (?, ?, ?, ?);");
        $stmt->bind_param("ssss", $PK_BudgetID, $startDate, $endDate, $FK_UserID);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }

    }
    // budgetItem has PK_BudgetID , PK_CategoryID, BudgetAmount
    public function createBudgetItem($PK_BudgetID, $PK_CategoryID, $BudgetAmount){
        $stmt = $this->con->prepare("INSERT INTO `budgetitem` (`PK_BudgetID`, `PK_CategoryID`, `BudgetAmount`) VALUES (?, ?, ?);");
        $stmt->bind_param("sss", $PK_BudgetID, $PK_CategoryID, $BudgetAmount);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }
    }

    // create cateogry from db, category atributtes: PK_CategoryID, Name, ParentCategory, Amount
    public function createCategory($id, $name,  $amount,$parentCategory){
        $stmt = $this->con->prepare("INSERT INTO `categories` (`PK_CategoryID`, `Name`, `ParentCategory`, `Amount`) VALUES (?, ?, ?, ?);");
        $stmt->bind_param("ssss", $id, $name, $parentCategory, $amount);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }
    }


    // get Budget based on the start date and end date
    public function getBudget($startDate, $endDate){
        $stmt = $this->con->prepare("SELECT `PK_BudgetID`, `StartDate`, `EndDate`, `FK_UserID` FROM `budget` WHERE `StartDate` = ? AND `EndDate` = ?");
        $stmt->bind_param("ss", $startDate, $endDate);
        $stmt->execute();
        $stmt->bind_result($PK_BudgetID, $StartDate, $EndDate, $FK_UserID);
        $budget = array();
        while($stmt->fetch()){
            $budget['PK_BudgetID'] = $PK_BudgetID;
            $budget['startDate'] = $StartDate;
            $budget['endDate'] = $EndDate;
            $budget['FK_UserID'] = $FK_UserID;
        }
        return $budget;
    }

    // get all the budget items from the db
    public function getBudgetItems(){
        $stmt = $this->con->prepare("SELECT `PK_BudgetID`, `PK_CategoryID`, `BudgetAmount` FROM `budgetitem`");
        $stmt->execute();
        $stmt->bind_result($PK_BudgetID, $PK_CategoryID, $BudgetAmount);
        $budgetItems = array();
        while($stmt->fetch()){
            $budgetItem = array();
            $budgetItem['PK_BudgetID'] = $PK_BudgetID;
            $budgetItem['PK_CategoryID'] = $PK_CategoryID;
            $budgetItem['BudgetAmount'] = $BudgetAmount;
            array_push($budgetItems, $budgetItem);
        }
        return $budgetItems;
    }
    

    // get all the categories from the db
    public function getCategories(){
        $stmt = $this->con->prepare("SELECT `PK_CategoryID`, `Name`, `ParentCategory`, `Amount` FROM `categories`");
        $stmt->execute();
        $stmt->bind_result($PK_CategoryID, $Name, $ParentCategory, $Amount);
        $categories = array();
        while($stmt->fetch()){
            $category = array();
            $category['PK_CategoryID'] = $PK_CategoryID;
            $category['Name'] = $Name;
            $category['ParentCategory'] = $ParentCategory;
            $category['Amount'] = $Amount;
            array_push($categories, $category);
        }
        return $categories;
    }



    // get category by id
    public function getCategory($id){
        $stmt = $this->con->prepare("SELECT `PK_CategoryID`, `Name`, `ParentCategory`, `Amount` FROM `categories` WHERE `PK_CategoryID` = ?");
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $stmt->bind_result($PK_CategoryID, $Name, $ParentCategory, $Amount);
        $category = array();
        while($stmt->fetch()){
            $category['PK_CategoryID'] = $PK_CategoryID;
            $category['Name'] = $Name;
            $category['ParentCategory'] = $ParentCategory;
            $category['Amount'] = $Amount;
        }
        return $category;
    }



    // get all Budgets from the db
    public function getBudgets(){
        $stmt = $this->con->prepare("SELECT `PK_BudgetID`, `StartDate`, `EndDate`, `FK_UserID` FROM `budget`");
        $stmt->execute();
        $stmt->bind_result($PK_BudgetID, $StartDate, $EndDate, $FK_UserID);
        $budgets = array();
        while($stmt->fetch()){
            $budget = array();
            $budget['PK_BudgetID'] = $PK_BudgetID;
            $budget['startDate'] = $StartDate;
            $budget['endDate'] = $EndDate;
            $budget['FK_UserID'] = $FK_UserID;
            array_push($budgets, $budget);
        }
        return $budgets;
    } 

    
    


    // get the Budget based on start and endDate with all the  budget items that match the id and also attach the all the category where the id matches the PK_CategoryID
    public function getBudgetWithBudgetItems($startDate, $endDate){
        
        $budget["budget"] = $this->getBudget($startDate, $endDate); 

        // echo the $budget variable
       // $budgetString = json_encode($budget["budget"]);
      //  echo $budgetString;
        
        $PK_BudgetID = $budget["budget"]["PK_BudgetID"];
        //echo $PK_BudgetID;
        
        $stmt = $this->con->prepare("SELECT `PK_BudgetID`, `PK_CategoryID`, `BudgetAmount` FROM `budgetitem` WHERE `PK_BudgetID` = ?");
        $stmt->bind_param("s", $PK_BudgetID);
        $stmt->execute();
        $stmt->bind_result($PK_BudgetID, $PK_CategoryID, $BudgetAmount);
        $budgetItems = array();
        while($stmt->fetch()){
            $budgetItem = array();
            $budgetItem['PK_BudgetID'] = $PK_BudgetID;
            $budgetItem['PK_CategoryID'] = $PK_CategoryID;
            $budgetItem['BudgetAmount'] = $BudgetAmount;
            array_push($budgetItems, $budgetItem);
        }
        $budget['budgetItems'] = $budgetItems;

        $categories = array();
        // for each budget item, get the category, where the PK_CategoryID matches the PK_CategoryID from the budget item
        foreach($budget['budgetItems'] as $budgetItem){
          
            array_push($categories, $this->getCategory($budgetItem['PK_CategoryID']));
        }

         //$budget['categories'] = $this->getCategories();
        $budget['categories'] = $categories;

        return $budget;


    }


   // get the all the start and end dates from budget db
   public function getBudgetDates(){
    $stmt = $this->con->prepare("SELECT `StartDate`, `EndDate` FROM `budget`");
    $stmt->execute();
    $stmt->bind_result($StartDate, $EndDate);
    $budgetDates = array();
    while($stmt->fetch()){
        $budgetDate = array();
        $budgetDate['StartDate'] = $StartDate;
        $budgetDate['EndDate'] = $EndDate;
        array_push($budgetDates, $budgetDate);
    }
    return $budgetDates;
   }


   public function budgetExists($id){
    $stmt = $this->con->prepare("SELECT PK_BudgetID FROM budget WHERE PK_BudgetID = ?");
    $stmt->bind_param("s", $id);
    $stmt->execute();
    $stmt->store_result();
    // if the budget exists, return true
    return $stmt->num_rows > 0;
   }
   // delete budget, with all his budget items
    public function deleteBudget($id){
     // if the id does not exist, return false
     if(!$this->budgetExists($id)){
          return false;
     }
    
     // delete all the budget items that match the id
        $stmt = $this->con->prepare("DELETE FROM `budgetitem` WHERE `PK_BudgetID` = ?");
        $stmt->bind_param("s", $id);
       
        // delete the budget
        $stmt = $this->con->prepare("DELETE FROM `budget` WHERE `PK_BudgetID` = ?");
        $stmt->bind_param("s", $id);
        if($stmt->execute()){
            return true;
        }else{
            return false;
        }

    }
}