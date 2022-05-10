const setSets = function(myDb,req, res){
    console.log("setSets");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise
    }
    collection = myDb.collection('Sets')
    collection.find(query).toArray(function(err, result) {
        var list = [];
        result.forEach(function(element){
            const obj = {
                Uid: element.Uid,
                PlanName: element.PlanName,
                Date: element.Date,
                Day: element.Day,
                Exercise: element.Exercise,
                SetNumber: element.SetNumber,
                Weight: element.Weight,
                Reps: element.Reps,
                Finish: element.Finish
            }
            list.push(obj)
        })
        res.status(200).send(JSON.stringify(list))
    })
          
};

const updateSet = function(myDb,req, res){
    console.log("updateSet");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise,
        SetNumber: req.body.SetNumber
    }
    const newSet = {
        $set: {
            Uid: req.body.Uid,
            PlanName: req.body.PlanName,
            Date: req.body.Date,
            Day: req.body.Day,
            Exercise: req.body.Exercise,
            SetNumber: req.body.SetNumber,
            Weight: req.body.Weight,
            Reps: req.body.Reps,
            Finish: req.body.Finish
        }
    }
    myDb.collection('Sets').updateOne(query,newSet,(err, result) => {
        if(err){
            res.status(400).send()
        }else{
            res.status(200).send()
        }
    })  
};

const addSet = function(myDb,req, res){
    console.log("addSet");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise,
        SetNumber: req.body.SetNumber
     }
    const newSet = {
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise,
        SetNumber: req.body.SetNumber,
        Weight: req.body.Weight,
        Reps: req.body.Reps,
        Finish: req.body.Finish
    }
    collection = myDb.collection('Sets')
    collection.findOne(query, (err, result) => {
        if (result == null) {
            collection.insertOne(newSet, (err, result) => {
                res.status(200).send()
            })
        } 
        else { res.status(400).send() }
    })
};

module.exports = {updateSet, addSet, setSets};