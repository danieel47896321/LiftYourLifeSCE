const login = function(myDb,req, res){
    console.log("login");
    const query = { uid: req.body.uid }
    collection = myDb.collection('Users')
    collection.findOne(query, (err, result) => {
        if (result != null) {
            const objToSend = {
                Uid: result.uid,
                Email: result.email,
                FullName: result.fullName,
                FirstName: result.firstName,
                lastName: result.lastName,
                Gender: result.gender,
                BirthDay: result.birthDay,
                StartDate: result.startDate,
                Height: result.height,
                Weight: result.weight
            }
            res.status(200).send(JSON.stringify(objToSend))
        } else {
            res.status(400).send()
        }
    })            
};

module.exports = {login};