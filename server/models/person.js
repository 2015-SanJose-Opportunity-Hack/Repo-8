
var mongoose = require('mongoose');

var SeniorSchema = new mongoose.Schema({

	token: {type: String},
	name: {type: String},
	seniorphone: {type: String},
	age: {type: Number},
	height: {type: String},
	weight: {type: String},
	diseases: {type: Array},
	address: {type: String}
});

var PersonSchema = new mongoose.Schema({

	token: {type: String},
	name: {type: String},
	phone: {type: String},
	email: {type: String},
	age: {type: Number},
	//type: {type: String},
	//height: {type: Number},
	//weight: {type: Number},
	address: {type: String},
	//diseases: {type: Array},
	seniors: [SeniorSchema]
});

mongoose.model('Person', PersonSchema, 'Person');