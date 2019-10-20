const functions = require('firebase-functions');
var express = require("express");
var admin = require("firebase-admin");

admin.initializeApp();
// var app = express();

var auth = express();

const authRouter = require('./auth.js');

auth.use(authRouter);

exports.auth = functions.https.onRequest(auth);