class Databse{
    constructor(){
        this.db = null;
    }
    connect(){
        this.db = new MongoClient('mongodb://localhost:27017');
    }
    getDb(){
        return this.db;
    }
}