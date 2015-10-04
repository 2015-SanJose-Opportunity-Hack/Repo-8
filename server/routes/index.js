var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var PersonSchema = mongoose.model("Person");
var NotificationSchema = mongoose.model("Notifications");
var TasksSchema = mongoose.model("Tasks");

// incluse gcm ssender libraries 
var gcm = require('node-gcm');


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
router.post('/lovedOne', function(req, res){

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
router.post('/lovedOne/tasks', function(req, res){

	var task = new TasksSchema(req.body);
	task.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding a task: ", err);
			res.json(false);
		}
		
		//create a notificatio n object to save in notification collecttion
		var notification = {

			msg: req.body.task,
			sender: req.body.assignee,
			receiver: req.body.assignedto,
			date: new Date(),
			taskType: "task"
		}
		// call method to send notification
		saveNotifications(notification);

		console.log(data);
		res.json(true);
	});
});


// update task status
router.put("/lovedOne/tasks/:_id/status/:taskstatus", function(req, res){

	var query = {"_id": req.params._id};
	var update = { "$set": { "status": req.params.taskstatus }};

	// update the task status
	TasksSchema.findOneAndUpdate(query, update, function(err, data){

		if(err){
			console.log(err);
			res.json(false);
		}

		// create a notification and save it

		console.log("Task updated: "+data);
		res.json(true);
	});
});


var receiverIds = ['fzaEygfKndE:APA91bHGtDPcEzY-al9NCF7mHEdJiCdblVg85-0LPW1zUc-AQwn-Th_yozWPmtkOyTfbUPi2sn9gHI2URx5fXsKQub8VkbWlQukYZdoFxqlYg7mkiQu5sou5lwJPm0SOaI6Sz_WUEF64'];

//ToDo: remove static receiver id
function sendNotification(msg, receiver){

	console.log("Notiication msg: " + msg);
	// Set up the sender with you API key
	var sender = new gcm.Sender('AIzaSyAuoYqHza88PvSEl3dXs8helN9W_T5CiHE');

	var message = new gcm.Message();
    message.addData('key1', { "message" : msg, type: msg.taskType });
    
    // .... without retrying
    sender.send(message, { registrationIds: receiver }, function (err, result) {
        if(err) console.error("Error: "+err);
        else    console.log(result);
    });
    
    // ... or retrying a specific number of times 
    sender.send(message, { registrationIds: receiver }, 2, function (err, result) {
      if(err) console.error(err);
      else    console.log(result);
    });
}

// save notifications to db before sending
function saveNotifications(notificationMsg){

	var notification = new NotificationSchema(notificationMsg);
	var notificationMsg = notificationMsg;

	notification.save(function(err, data){
		
		if(err){
			console.log("Error adding a new notification: ", err);
		}

		console.log("Notification saved: " + data);
		//call method to send the notification
		sendNotification(notification, receiverIds);
		console.log(data);
	});
}


router.post('/notifyUser/:msg1',function(req,res,next){
    
    var message = new gcm.Message();
    message.addData('key1', req.params.msg1);
    
    // .... without retrying
    sender.send(message, { registrationIds: receiverIds }, function (err, result) {
        if(err) console.error("Error: "+err);
        else    console.log(result);
    });
    
    // ... or retrying a specific number of times 
    sender.send(message, { registrationIds: receiverIds }, 2, function (err, result) {
      if(err) console.error(err);
      else    console.log(result);
    });
})


module.exports = router;