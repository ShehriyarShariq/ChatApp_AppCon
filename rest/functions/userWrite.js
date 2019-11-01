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

// Create User Chats
router.put("/api/user/create_user_chats", function(req, res){
        var dummyMsgMap = {
                type : "Game or Audio or Whatever",
                sentBy : "userID",
                timeStamp : "timeStamp",
                content : "content"
        };

        var userID = req.body.userID;
        var userChats = req.body.userChats;
        firebaseAuth.getUser(userID)
        .then(function(userRecord){
                var chatIDs = Object.keys(userChats);
                
                var index = 0;
                for(var i = 0, n = chatIDs.length; i < n; i++){
                        var chatID = chatIDs[i];
                        firebaseDatabase.ref("users/" + userID + "/chats/" + chatID).set("lastMsgSeenID", function(error){
                                if(error){
                                        if(index == (n - 1)){
                                                res.json(setResult(successID, "Success!"));
                                        }

                                        index++;
                                } else {
                                        var chatID = chatIDs[index];
                                        var userChat = userChats[chatID];
                                        var convo = userChat.convo;
                                        firebaseDatabase.ref("conversations/" + chatID).set(convo,function(error){
                                                if(error){
                                                        if(index == (n - 1)){
                                                                res.json(setResult(successID, "Success!"));
                                                        }
                
                                                        index++;
                                                } else {
                                                        firebaseDatabase.ref("messages/" + chatID + "/messageID").set(dummyMsgMap, function(error){
                                                                if(error){
                                                                        if(index == (n - 1)){
                                                                                res.json(setResult(successID, "Success!"));
                                                                        }
                                
                                                                        index++;
                                                                } else {
                                                                        if(index == (n - 1)){
                                                                                res.json(setResult(successID, "Success!"));
                                                                        }
                                
                                                                        index++;
                                                                }                                                                
                                                        });
                                                }
                                        });
                                }
                        });
                }

                
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