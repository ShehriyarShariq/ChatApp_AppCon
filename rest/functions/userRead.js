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

// Get Valid Contacts
router.put("/api/user/get_valid_contacts", function(req, res){
        var userID = req.body.userID;
        var allLocalContacts = req.body.allContacts; // Must not be empty
        var allValidContacts = [];
        firebaseAuth.getUser(userID)
        .then(function(userRecord){
                var index = 0;
                var j = 0;
                for(var i = 0, n = allLocalContacts.length; i < n; i++){
                        firebaseAuth.getUserByPhoneNumber(allLocalContacts[i])
                        .then(function(userRecord){
                                allValidContacts[j++] = {
                                        phoneNum : userRecord.phoneNumber,
                                        id : userRecord.uid                                
                                }

                                if(index == (n - 1)){
                                        res.json(setResult(successID, allValidContacts));
                                }
                                
                                index++;
                        })
                        .catch(function(error){
                                if(index == (n - 1)){
                                        res.json(setResult(successID, allValidContacts));
                                }
                                
                                index++;
                        });
                }
        })
        .catch(function(error){
                res.json(setResult(failureID, "Access Denied!"));
        });
});

// Get user conversations
router.put("/api/user/get_conversations", function(req, res){
        var userID = req.body.userID;
        firebaseAuth.getUser(userID)
        .then(function(userRecord){
                var userChatsRef = firebaseDatabase.ref("users/" + userID + "/chats");
                userChatsRef.once("value", function(dataSnapshot){
                        if(dataSnapshot.exists()){
                                var chats = dataSnapshot.val();
                                delete chats["chatID"];
                                
                                res.json(setResult(successID, chats));
                        } else {
                                res.json(setResult(failureID, "Error in User Creations"));
                        }
                });        
        })
        .catch(function(error){
                res.json(setResult(failureID, "Access Denied!"));
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