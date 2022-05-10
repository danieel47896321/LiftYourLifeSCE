const setPlans = function(myDb,req, res){
    console.log("setPlans");
    const query = { 
        Uid: req.body.Uid,
        Day: req.body.Day
    }
    collection = myDb.collection('Plans')
    collection.find(query).toArray(function(err, result) {
        var list = [];
        result.forEach(function(element){
            const obj = {
                Uid: element.Uid,
                PlanName: element.PlanName,
                Date: element.Date,
                Day: element.Day
            }
            list.push(obj)
        })
        res.status(200).send(JSON.stringify(list))
    })
};

const addPlan = function(myDb,req, res){
    console.log("addPlan");
    const query = { 
        Uid: req.body.Uid,
        Date: req.body.Date
     }
    const newPlan = {
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day
    }
    collection = myDb.collection('Plans')
    collection.findOne(query, (err, result) => {
        if (result == null) {
            collection.insertOne(newPlan, (err, result) => {
                res.status(200).send()
            })
        } 
        else { res.status(400).send() }
    })
};
const removePlan = function(myDb,req, res){
    console.log("removePlan");
    const query = { 
        Uid: req.body.Uid,
        PlanName: req.body.PlanName,
        Date: req.body.Date,
        Day: req.body.Day
    }
    collection = myDb.collection('Plans')
    collection.deleteOne(query, (err, result) => {
        if(err) {
            res.status(400).send()
        }else {
            collection2 = myDb.collection('Exercises')
            collection2.deleteMany(query, (err, result) => {
                if(err) {
                    res.status(400).send()
                }else {
                    collection3 = myDb.collection('Sets')
                    collection3.deleteMany(query, (err, result) => {
                        if(err) {
                            res.status(400).send()
                        }else {
                             res.status(200).send()
                        }
                    })
                }
            })
        }
    })
};

module.exports = {setPlans, addPlan, removePlan};