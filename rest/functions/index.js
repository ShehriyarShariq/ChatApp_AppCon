const functions = require('firebase-functions');
var express = require("express");
var admin = require("firebase-admin");

admin.initializeApp();
// var app = express();

var auth = express();
var userRead = express();
var userWrite = express();

const authRouter = require('./auth.js');
const userReadRouter = require('./userRead.js');
const userWriteRouter = require('./userWrite.js');

auth.use(authRouter);
userRead.use(userReadRouter);
userWrite.use(userWriteRouter);

exports.auth = functions.https.onRequest(auth);
exports.userRead = functions.https.onRequest(userRead);
exports.userWrite = functions.https.onRequest(userWrite);