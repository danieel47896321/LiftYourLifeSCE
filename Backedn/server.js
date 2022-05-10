const express = require('express');
const app = express();
app.use(express.json());

require('dotenv').config()
const port = process.env.PORT;
const mongodbUrl = process.env.URL;
const mongoClient = require('mongodb').MongoClient;

const moduleExercise = require('./Modules/exercise');
const moduleLogin = require('./Modules/login');
const modulePlan = require('./Modules/plan');
const moduleProfile = require('./Modules/profile');
const moduleSignup = require('./Modules/signup');
const moduleSet = require('./Modules/set');

mongoClient.connect(mongodbUrl, (err, db) => {
    if (err) {
        console.log("Error while connecting mongo client")
    } else {
        const myDb = db.db('Database')

        app.post('/signup', (req, res) => {
            moduleSignup.signup(myDb,req,res);
        })

        app.post('/login', (req, res) => {
            moduleLogin.login(myDb,req,res);
        })

        app.post('/profile', (req, res) => {
            moduleProfile.profile(myDb,req,res);
        })
        
        app.post('/setPlans', (req, res) => {
            modulePlan.setPlans(myDb,req,res);
        })

        app.post('/addPlan', (req, res) => {
            modulePlan.addPlan(myDb,req,res);
        })

        app.post('/removePlan', (req, res) => {
            modulePlan.removePlan(myDb,req,res);
        })

        app.post('/setExercises', (req, res) => {
            moduleExercise.setExercises(myDb,req,res);
        })

        app.post('/addExercise', (req, res) => {
            moduleExercise.addExercise(myDb,req,res);
        })
    
        app.post('/removeExercise', (req, res) => {
            moduleExercise.removeExercise(myDb,req,res); 
        })

        app.post('/updateSet', (req, res) => {
            moduleSet.updateSet(myDb,req,res); 
        })

        app.post('/addSet', (req, res) => {
            moduleSet.addSet(myDb,req,res); 
        })

        app.post('/setSets', (req, res) => {
            moduleSet.setSets(myDb,req,res); 
        })
    } 
})

app.listen(port, () => {
    console.log("Listening on port " + port)
})