
var mongoose = require('mongoose');

var PersonSchema = new mongoose.Schema({

	token: {type: String},
	name: {type: String},
	phone: {type: String},
	email: {type: String},
	age: {type: String},
	type: {type: String},
	height: {type: Number},
	weight: {type: Number},
	address: {type: String},
	diseases: {type: Array},
	caresfor: {type: Array}
});

mongoose.model('Person', PersonSchema, 'Person');