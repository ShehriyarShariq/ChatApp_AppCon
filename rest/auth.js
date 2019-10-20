const successID = "202";
const failureID = "402";
const failureID = "404";

var express = require("express");
var bodyParser = require("body-parser");
var admin = require("firebase-admin");


var firebaseDatabase = admin.database();
var firebaseAuth = admin.auth();
var router = express.Router();

var result = {
	"result" : "status",
	"resultID" : "404/202",
	"message" : "message"
};


router.use(bodyParser.json());
router.use(bodyParser.urlencoded({extended : true})); 

// Login User
router.put("/api/login", function(req, res){
        var phoneNum = req.body.phoneNum;
        firebaseAuth.getUserByPhoneNumber(phoneNum)
        .then(function(userRecord){
                if(userRecord.customClaims.user === true){
                        firebaseAuth.createCustomToken(userRecord.uid)
                        .then(function(customToken){
                                res.json(setResult("success", successID, customToken));
                        })
                        .catch(function(error){
                                res.json(setResult("error", failureID, "Token creation failed"));
                        });
                } else {
                        res.json(setResult("failure", failureID, "Access Denied!"));
                }
        })
        .catch(function(error){
                res.json(setResult("error", failureID, "User not found!"));
        });
});

router.put("/api/signup", function(req, res){
        var authMap = req.body.auth;
        var dbMap = req.body.db;
        firebaseAuth.createUser(authMap)
        .then(function(userRecord){
                firebaseAuth.setCustomUserClaims(userRecord.uid, {user : true})
                .then(function(){
                        var newUserRef = firebaseDatabase.ref("");
                        newUserRef.set(dbMap);
                })
                .catch(function(error){
                        res.json(setResult("error", failureID, "Failed to set user claims!"));
                });

                if(userRecord.customClaims.user === true){
                        firebaseAuth.createCustomToken(userRecord.uid)
                        .then(function(customToken){
                                res.json(setResult("success", successID, customToken));
                        })
                        .catch(function(error){
                                res.json(setResult("error", failureID, "Token creation failed"));
                        });
                } else {
                        res.json(setResult("failure", failureID, "Access Denied!"));
                }
        })
        .catch(function(error){
                res.json(setResult("error", failureID, "Failed to create user!"));
        });
});


module.exports = router;

// FUNCTIONS
function setResult(res, code, message){
	result["result"] = res;
	result["resultID"] = code;
	result["message"] = message;
}