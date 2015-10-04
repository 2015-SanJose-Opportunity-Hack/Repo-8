var mongoose = require('mongoose');

var NotificationSchema = new mongoose.Schema({
	msg: {type: String},
	date: {type: String},
	sender: {type: String},
	receiver: {type: String},
	taskType: {type: String}
});

mongoose.model('Notifications', NotificationSchema, 'Notifications');