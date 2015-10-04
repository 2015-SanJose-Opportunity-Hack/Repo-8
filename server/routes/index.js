var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var PersonSchema = mongoose.model("Person");
var NotificationSchema = mongoose.model("Notifications");
var TasksSchema = mongoose.model("Tasks");
var HealthConditionSchema = mongoose.model("HealthCondition");

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
			res.json({response: false});
		}
		else{
			console.log(data);
			res.json({response: true});
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
			res.json({response: false});
		}
		else{
			console.log(data);
			res.json({response: true});
		}
	});
});

// get the list of who you care for
router.get('/lovedOne', function(req, res){

	PersonSchema.findOne({pid: req.body.pid}, function(err, data){

		if(err) {
			console.log("Error in retrieving users: " +  err);
			res.json({response: null});
		}

		console.log("Users :" + data);
		response.json({response: data});
	});
});

// update the unique token for the client already in db
router.put('/lovedOne/:phone', function(req, res){

	var query = {phone: req.params.phone};
	var update = { "$set" : { "token": req.body.token } }
	var options = {"new" : true, "upsert": true};

	PersonSchema.findOneAndUpdate(query, update, options, function(err, data){

		if(err){
			console.log("Error updating the token: " + err);
			res.json({response: null});
		}
		if(data){
			console.log("Token updated: " + data);
			res.json({response: data});
		}
		//send null if user does not exist
		res.json({response: null});
	});
});

//create a new task for your loved one
router.post('/lovedOne/tasks', function(req, res){

	var task = new TasksSchema(req.body);
	task.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding a task: ", err);
			res.json({response: false});
		}
		
		//create a notificatio n object to save in notification collecttion
		var notification = {

			msg: req.body.task,
			sender: req.body.assignee,
			receiver: req.body.assignedto,
			date: new Date(),
			notificationType: "task"
		}
		// call method to send notification
		saveNotifications(notification);

		console.log(data);
		res.json({response: true});
	});
});


// update task status
router.put("/lovedOne/tasks/:_id/status/:taskstatus", function(req, res){

	var query = {"_id": req.params._id};
	var update = { "$set": { "status": req.params.taskstatus }};

	// update the task status
	TasksSchema.findOneAndUpdate(query, update, function(err, data){

		console.log(data);

		if(err){
			console.log(err);
			res.json({response: false});
		}

		if(data){
			// create a notification and save it
			var notification = {
				msg: data.task,
				//get the senders id from the tasks metadata
				sender: data.assignedto,
				//get the receivers id from the tasks metadata
				receiver: data.assignee,
				date: new Date(),
				notificationType: "task update"
			}

			saveNotifications(notification);

			console.log("Task updated: "+data);
			res.json({response: true});			
		}
		//send false if the task does not exist
		res.json({response: false});

	});
});


//send general info about daily chores
router.post("/lovedOne/dailychores", function(req, res){

	var notification = {
		msg: "Daily chore completed",
		sender: "6693008325",
		receiver: "6694003333",
		date: new Date(),
		notificationType: "Daily chore"
	}

	saveNotifications(notification);
	res.json({response: true});
});


// post a health related issue
router.post("/lovedOne/healthissue", function(req, res){

	var healthCondition = new HealthConditionSchema(req.body);

	healthCondition.save(function(err, data){

		if(err){
			console.log("Error adding a health condtion: " + err);
			res.json({response: false});
		}
		if(data){
			console.log("Health issue added: " + data);
			//save the notification to be sent to the care taker
			var notification = {
				msg: data.description,
				sender: data.token,
				receiver: req.body.receiver,
				date: new Date(),
				notificationType: "Health Issue"
			}
			// save the notification to be the sent to the care taker
			saveNotifications(notification);
			res.json({response: data});
		}
	});
});



// save notifications to db before sending
function saveNotifications(notificationMsg){

	var receiverIds = ['fzaEygfKndE:APA91bHGtDPcEzY-al9NCF7mHEdJiCdblVg85-0LPW1zUc-AQwn-Th_yozWPmtkOyTfbUPi2sn9gHI2URx5fXsKQub8VkbWlQukYZdoFxqlYg7mkiQu5sou5lwJPm0SOaI6Sz_WUEF64'];
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



//ToDo: remove static receiver id
function sendNotification(msg, receiver){

	var receiverIds = ['fzaEygfKndE:APA91bHGtDPcEzY-al9NCF7mHEdJiCdblVg85-0LPW1zUc-AQwn-Th_yozWPmtkOyTfbUPi2sn9gHI2URx5fXsKQub8VkbWlQukYZdoFxqlYg7mkiQu5sou5lwJPm0SOaI6Sz_WUEF64'];
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
    /*sender.send(message, { registrationIds: receiver }, 2, function (err, result) {
      if(err) console.error(err);
      else    console.log(result);
    });*/
}


module.exports = router;