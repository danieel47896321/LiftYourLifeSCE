const signup = function(myDb,req, res){
    console.log("signup");
    const query = { uid: req.body.uid }
    const newUser = {
        uid: req.body.uid,
        email: req.body.email,
        fullName: req.body.fullName,
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        gender: req.body.gender,
        birthDay: req.body.birthDay,
        startDate: req.body.startDate,
        height: req.body.height,
        weight: req.body.weight
    }
    collection = myDb.collection('Users')
    collection.findOne(query, (err, result) => {
        if (result == null) {
            collection.insertOne(newUser, (err, result) => {
                res.status(200).send()
            })
        } 
        else { res.status(400).send() }
    })        
};

module.exports = {signup};