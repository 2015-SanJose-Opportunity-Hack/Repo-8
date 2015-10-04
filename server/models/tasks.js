var mongoose = require('mongoose');

var TasksSchema = new mongoose.Schema({
	
	tokenId: {type: String},
	assignee: {type: String},
	assignedto: {type: String},
	task: {type: String},
	date: {type: String},
	status: {type: String},
	deadline: {type: String},
	priority: {type: String}
});

mongoose.model('Tasks', TasksSchema, 'Tasks');