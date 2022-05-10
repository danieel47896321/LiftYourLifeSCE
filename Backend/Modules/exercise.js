const setExercises = function(myDb,req, res){
    console.log("setExercises");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day
    }
    collection = myDb.collection('Exercises')
    collection.find(query).toArray(function(err, result) {
        var list = [];
        result.forEach(function(element){
            const obj = {
                Uid: element.Uid,
                PlanName: element.PlanName,
                Date: element.Date,
                Day: element.Day,
                Exercise: element.Exercise,
                Description: element.Description,
                MuscleType: element.MuscleType,
                Sets: element.Sets,
                Image: element.Image
            }
            list.push(obj)
        })
        res.status(200).send(JSON.stringify(list))
    })
};

const addExercise = function(myDb,req, res){
    console.log("addExercise");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise
     }
    const newExercis = {
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise,
        Description: req.body.Description,
        MuscleType: req.body.MuscleType,
        Sets: req.body.Sets,
        Image: req.body.Image
    }
    collection = myDb.collection('Exercises')
    collection.findOne(query, (err, result) => {
        if (result == null) {
            collection.insertOne(newExercis, (err, result) => {
                res.status(200).send()
            })
        } 
        else { res.status(400).send() }
    })
};

const removeExercise = function(myDb,req, res){
    console.log("removeExercise");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day,
        Exercise: req.body.Exercise
     }
    collection = myDb.collection('Exercises')
    collection.deleteOne(query, (err, result) => {
        if(err) {
            res.status(400).send()
        }else {
            collection2 = myDb.collection('Sets')
            collection2.deleteMany(query, (err, result) => {
                if(err) {
                    res.status(400).send()
                }else {
                     res.status(200).send()
                }
            })
        }
    })
};

module.exports = {setExercises, addExercise, removeExercise};