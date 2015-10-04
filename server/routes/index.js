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


router.get('/caretaker/:token', function(req, res){

	PersonSchema.findOne({token: req.params.token}, function(err, data){

		if(err){
			console.log("Error getting the caretaker info: " + err);
			res.json({response: err});
		}
		if(data){
			console.log("Caretaker: " + data);
			res.json({response: data});
		}
		res.json({response: null});
	});
});


// add a loved one you want to take care of
router.post('/caretaker/:token/lovedOne', function(req, res){

	var query = { token: req.params.token };
	var update = {"$push": { seniors: req.body } };

	PersonSchema.update(query, update, function(err, data){

		if(err) {
			console.log("Error in adding a senior: " +  err);
			res.json({response: null});
		}
		if(data){
			console.log("Senior added: " + data);
			res.json({response: data});
		}

		console.log("Users :" + data);
		res.json({response: data});
	});
});

// get the list of who you care for
router.get('/lovedOneAll/:token', function(req, res){

	PersonSchema.findOne({token : req.params.token}, function(err, data){

		if(err){
			console.log("Error retrieving the seniors : " + err);
			res.json({response: null});
		}
		if(data){
			console.log(data);
			console.log(data.seniors);
			res.json({response: data.seniors});
		}
		res.json({response: null});
	});

});

// update the unique token for the client already in db
router.post('/caretaker/:token/lovedOne', function(req, res){

	PersonSchema.findOne({token: req.params.token}, function(err, data){

		if(err){
			console.log("Error updating the token: " + err);
			res.json({response: null});
		}
		if(data){
			console.log("Caretaker : " + data);
			console.log(data.seniors.length);
			
			for(var i=0; i<data.seniors.length; i++){

				if(data.seniors[i].seniorphone == req.body.seniorphone){

					data.seniors[i].token = req.body.seniortoken;
					console.log(data.seniors);
				}
			}
			//save the updated document
			data.save(function(err, data){
				if(err){
					console.log("Token update error: " + err);
				}
				if(data){
					console.log("Token updated: " + data);	
					res.json({response: true});				
				}
				res.json({response: false});
			});
		}
		//send null if user does not exist
		res.json({response: false});
	});
});


// get a senior for a caretaker using his/her token
router.get('/caretaker/:token/lovedOne/:seniortoken', function(req, res){

	PersonSchema.findOne({token: req.params.token}, function(err, data){

		if(err){
			console.log("Error retrieving the person: " + err);
			res.json({response: null});
		}
		if(data){

				for(var i=0; i<data.seniors.length; i++){
				if(data.seniors[i].token == req.params.seniortoken){

					var senior = {
						name: data.seniors[i].name,
						age: data.seniors[i].age,
						height: data.seniors[i].height,
						weight: data.seniors[i].weight,
						seniorphone: data.seniors[i].seniorphone,
						diseases: data.seniors[i].diseases,
						address: data.seniors[i].address
					}

					res.json({response: senior});
				}
			}
		}//if

		res.json({response: null});
	});
});

// get the diseases for a senior using his token 
router.get('/caretaker/:token/lovedOne/:seniortoken/diseases', function(req, res){

	PersonSchema.findOne({token: req.params.token}, function(err, data){

		if(err){
			console.log("Error retrieving the caretaker :" + err);
			res.json({response: null});
		}
		if(data){

			for(var i=0; i<data.seniors.length; i++){
				if(data.seniors[i].token == req.params.seniortoken){

					console.log(data.seniors[i].diseases);
					res.json({response: data.seniors[i].diseases});
				}
				res.json({response: null});
			}
		}

		res.json({response: null});
	});
});

//create a new task for your loved one
router.post('/lovedOne/:seniortoken/tasks', function(req, res){

	var task = new TasksSchema(req.body);
	task.save(function(err, data){
		console.log(req.body);
		
		if(err){
			console.log("Error adding a task: ", err);
			res.json({response: false});
		}
		
		//ToDo: Get the sender from the request body
		var senderToken = "dAzNfQ-2-Gs:APA91bF5-UDBiPdXkfE8fSJWFn63qwYDXgtPDhJFniBzOPUG_gc2jSjxM7LFY6mZ_qgI5WwIMEDMpqxyXWijuYr9ElJXP1zohLN9aDhbNjkCCMQmgfg7casXGpJ3VFLtl9KTY5jnMD80";
		//create a notificatio n object to save in notification collecttion
		var notification = {

			msg: "New task: " + req.body.task,
			sender: "CareTaker",
			receiver: "cx2M2zTjvtM:APA91bHdNfd2LE063pTNr3nT2BWBWreebgo_03p4WgwKE-kW-7vPwMN9s-dw-lOzGt4TisX945e-QOtBYqIXNCmciOCiz_bNPYgk4isYeyKL2dDlv8bpFQnIEm6QMJc3J8qC59t1_Oro",
			date: new Date(),
			notificationtype: "TaskCreated"
		}
		// call method to send notification
		createTaskNotification(notification);

		console.log(data);
		res.json({response: true});
	});
});



// get the task list for a senior
router.get('/lovedOne/:token/tasks', function(req, res){

	TasksSchema.find({assignedto: req.params.token}, function(err, data){

		if(err){
			console.log("Error retrieving the task list: " + err);
			res.json({response: null});
		}
		if(data){

			console.log("Tasks : " + data);
			res.json({response: data});
		}
	});
});

// update task status
router.post("/lovedOne/:token/tasks/:_id/status/:taskstatus", function(req, res){

	var query = {assignedto: req.params.token, "_id": req.params._id};
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
				msg: "Task updated: " + data.task,
				//get the senders id from the tasks metadata
				sender: "Mom",
				//get the receivers id from the tasks metadata
				receiver: "dAzNfQ-2-Gs:APA91bF5-UDBiPdXkfE8fSJWFn63qwYDXgtPDhJFniBzOPUG_gc2jSjxM7LFY6mZ_qgI5WwIMEDMpqxyXWijuYr9ElJXP1zohLN9aDhbNjkCCMQmgfg7casXGpJ3VFLtl9KTY5jnMD80",
				date: new Date(),
				notificationtype: "TaskUpdate"
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
		notificationtype: "DailyChore"
	}

	saveNotifications(notification);
	res.json({response: true});
});


router.post("/lovedOne/:token/emergency", function(req, res){

	var healthCondition = new HealthConditionSchema(req.body);
	var token = req.params.token;

	healthCondition.save(function(err, data){

		if(err){
			console.log("Error updating emergency: " + err);
			res.json({response: false});
		}
		if(data){
			console.log("Emergency added");

			var notification = {
				msg: "Emergency situation alert!",
				sender: "Mom",
				receiver: "dAzNfQ-2-Gs:APA91bF5-UDBiPdXkfE8fSJWFn63qwYDXgtPDhJFniBzOPUG_gc2jSjxM7LFY6mZ_qgI5WwIMEDMpqxyXWijuYr9ElJXP1zohLN9aDhbNjkCCMQmgfg7casXGpJ3VFLtl9KTY5jnMD80",
				date : new Date(),
				notificationtype: "Emergency"
			}

			saveNotifications(notification);
			res.json({response: true});
		}
		res.json({response: false});
	});
});


// post a health related issue
router.post("/lovedOne/:token/healthissue", function(req, res){

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
				msg: data.description + " " + "Severity: "+data.severity,
				sender: "Mom",
				//make sure you receive a caretaker's token in the request body
				receiver: "dAzNfQ-2-Gs:APA91bF5-UDBiPdXkfE8fSJWFn63qwYDXgtPDhJFniBzOPUG_gc2jSjxM7LFY6mZ_qgI5WwIMEDMpqxyXWijuYr9ElJXP1zohLN9aDhbNjkCCMQmgfg7casXGpJ3VFLtl9KTY5jnMD80",
				date: new Date(),
				notificationtype: "HealthIssue"
			}
			// save the notification to be the sent to the care taker
			saveNotifications(notification);
			res.json({response: true});
		}
		res.json({response: false});
	});
});

//common API for retrieving notifications for caretaker as well as senior person
router.get("/person/:token/notifications", function(req, res){

	NotificationSchema.find({ receiver: req.params.token }, function(err, data){
		if(err){
			console.log("Error getting the notifications: " + err);
			res.json({response: null});
		}
		if(data){
			console.log("Notifications: " + data);
			res.json({response: data});
		}
		res.json({response: null});
	});
});

function createTaskNotification(notificationMsg){
	var receiverIds = ['cx2M2zTjvtM:APA91bHdNfd2LE063pTNr3nT2BWBWreebgo_03p4WgwKE-kW-7vPwMN9s-dw-lOzGt4TisX945e-QOtBYqIXNCmciOCiz_bNPYgk4isYeyKL2dDlv8bpFQnIEm6QMJc3J8qC59t1_Oro'];
	var notification = new NotificationSchema(notificationMsg);
	var notificationMsg = notificationMsg;

	console.log("Notification: " + notification);
	notification.save(function(err, data){
		
		if(err){
			console.log("Error adding a new notification: ", err);
		}

		console.log("Notification saved: " + data);
		//call method to send the notification
		sendTaskCretedNotification(notification, receiverIds);
		console.log(data);
	});
}


function sendTaskCretedNotification(msg, receiver){

	var receiverIds = ['cx2M2zTjvtM:APA91bHdNfd2LE063pTNr3nT2BWBWreebgo_03p4WgwKE-kW-7vPwMN9s-dw-lOzGt4TisX945e-QOtBYqIXNCmciOCiz_bNPYgk4isYeyKL2dDlv8bpFQnIEm6QMJc3J8qC59t1_Oro'];
	
	// Set up the sender with you API key
	var sender = new gcm.Sender('AIzaSyAuoYqHza88PvSEl3dXs8helN9W_T5CiHE');

	var message = new gcm.Message();
    message.addData('message', msg);
    
    // .... without retrying
    sender.send(message, { registrationIds: receiver }, function (err, result) {
        if(err) console.error("Error: "+err);
        else    console.log(result);
    });
}

// save notifications to db before sending
function saveNotifications(notificationMsg){

	console.log("Notification saved: " + notificationMsg);
	//var receiverIds = ['fzaEygfKndE:APA91bHGtDPcEzY-al9NCF7mHEdJiCdblVg85-0LPW1zUc-AQwn-Th_yozWPmtkOyTfbUPi2sn9gHI2URx5fXsKQub8VkbWlQukYZdoFxqlYg7mkiQu5sou5lwJPm0SOaI6Sz_WUEF64'];
	var receiverIds = ['dAzNfQ-2-Gs:APA91bF5-UDBiPdXkfE8fSJWFn63qwYDXgtPDhJFniBzOPUG_gc2jSjxM7LFY6mZ_qgI5WwIMEDMpqxyXWijuYr9ElJXP1zohLN9aDhbNjkCCMQmgfg7casXGpJ3VFLtl9KTY5jnMD80'];
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

	//var receiverIds = ['fzaEygfKndE:APA91bHGtDPcEzY-al9NCF7mHEdJiCdblVg85-0LPW1zUc-AQwn-Th_yozWPmtkOyTfbUPi2sn9gHI2URx5fXsKQub8VkbWlQukYZdoFxqlYg7mkiQu5sou5lwJPm0SOaI6Sz_WUEF64'];
	var receiverIds = ['dAzNfQ-2-Gs:APA91bF5-UDBiPdXkfE8fSJWFn63qwYDXgtPDhJFniBzOPUG_gc2jSjxM7LFY6mZ_qgI5WwIMEDMpqxyXWijuYr9ElJXP1zohLN9aDhbNjkCCMQmgfg7casXGpJ3VFLtl9KTY5jnMD80'];
	console.log("Notification msg: " + msg);
	// Set up the sender with you API key
	var sender = new gcm.Sender('AIzaSyAuoYqHza88PvSEl3dXs8helN9W_T5CiHE');

	var message = new gcm.Message();
    message.addData('message', msg);
    
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