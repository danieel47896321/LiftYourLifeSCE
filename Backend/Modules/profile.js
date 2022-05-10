const profile = function(myDb,req, res){
    console.log("profile");
    const query = { uid: req.body.uid }
    const newUser = { 
        $set: {
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
    }
    myDb.collection('Users').updateOne(query,newUser,(err, result) => {
        if(err){
            res.status(400).send()
        }else{
            res.status(200).send()
        }
    })        
};

module.exports = {profile};