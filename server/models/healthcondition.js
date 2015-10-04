
var mongoose = require('mongoose');

var HealthConditionSchema = new mongoose.Schema({

	token: {type: String},
	type: {type: String},
	description: {type: String},
	severity: {type: String},
	date: {type: String}

});

mongoose.model('HealthCondition', HealthConditionSchema, 'HealthCondition');