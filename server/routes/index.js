var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var PersonSchema = mongoose.model("Person");
var NotificationSchema = mongoose.model("Notifications");
var TasksSchema = mongoose.model("Tasks");

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});


//create a care taker
router.post('/caretaker', function(req, res){

	var careTaker = new PersonSchema(req.body);
	careTaker.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding the care taker: ", err);
			res.json(false);
		}
		else{
			console.log(data);
			res.json(true);
		}
	});
});


// add a loved one you want to take care of
router.post('/lovedone', function(req, res){

	var lovedOne = new PersonSchema(req.body);
	lovedOne.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding a loved one: ", err);
			res.json(false);
		}
		else{
			console.log(data);
			res.json(true);
		}
	});
});


//create a new task for your loved one
router.post('/lovedone/tasks', function(req, res){

	var task = new TasksSchema(req.body);
	task.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding a task: ", err);
			res.json(false);
		}
		else{
			console.log(data);
			res.json(true);
		}
	});
});


//create a new notification
router.post('/notifications', function(req, res){

	var notification = new NotificationSchema(req.body);
	notification.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding a new notification: ", err);
			res.json(false);
		}
		else{
			console.log(data);
			res.json(true);
		}
	});
});


module.exports = router;