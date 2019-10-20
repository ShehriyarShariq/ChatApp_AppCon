const successID = "202";
const failureID = "402";

var express = require("express");
var bodyParser = require("body-parser");
var admin = require("firebase-admin");


var firebaseDatabase = admin.database();
var firebaseAuth = admin.auth();
var router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({extended : true})); 

// Login User
router.put("/api/auth/login", function(req, res){
        var phoneNum = req.body.phoneNum;
        firebaseAuth.getUserByPhoneNumber(phoneNum)
        .then(function(userRecord){
                if(userRecord.customClaims.user === true){
                        firebaseAuth.createCustomToken(userRecord.uid)
                        .then(function(customToken){
                                res.json(setResult(successID, customToken));
                        })
                        .catch(function(error){
                                console.log(error);
                                res.json(setResult(failureID, "Token creation failed"));
                        });
                } else {
                        res.json(setResult(failureID, "Access Denied!"));
                }
        })
        .catch(function(error){
                res.json(setResult(failureID, "User not found!"));
        });
});

router.put("/api/auth/signup", function(req, res){
        var authMap = req.body.auth;
        var dbMap = req.body.db;
        firebaseAuth.createUser(authMap)
        .then(function(userRecord){
                firebaseAuth.setCustomUserClaims(userRecord.uid, {user : true})
                .then(function(){
                        var newUserRef = firebaseDatabase.ref("users/" + userRecord.uid);
                        newUserRef.set(dbMap, function(error){
                                if(error){
                                        res.json(setResult(failureID, "Failed to create user in database!"));
                                } else {
                                        res.json(setResult(successID, "Creation success!"));
                                }
                        });
                })
                .catch(function(error){
                        res.json(setResult(failureID, "Failed to set user claims!"));
                });
        })
        .catch(function(error){
                res.json(setResult(failureID, "Failed to create user!"));
        });
});


module.exports = router;

// FUNCTIONS
function setResult(code, message){
	var result = {
		"resultID" : code,
		"result" : {
			"message" : message
		}
	}

	return result;
}