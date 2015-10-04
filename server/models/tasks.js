var mongoose = require('mongoose');

var TasksSchema = new mongoose.Schema({
	
	assignee: {type: String},
	assignedTo: {type: String},
	task: {type: String},
	date: {type: String},
	status: {type: String},
	deadline: {type: String},
	priority: {type: String}
});

mongoose.model('Tasks', TasksSchema, 'Tasks');