var _ = require('ramda');

// Exercise 1
//==============
// Refactor to remove all arguments by partially applying the function

var words = _.split(' ');

console.log(words("1 2 3 4 5 6 7"));

// Exercise 1a
//==============
// Use map to make a new words fn that works on an array of strings.

var sentences = _.map(words);

console.log(sentences(["blah blah", "blah2 blah2"]));


// Exercise 2
//==============
// Refactor to remove all arguments by partially applying the functions

var filterQs = function(xs) {
  return _.filter(function(x){ return _.match(/q/g, x);  }, xs);
};

console.log(filterQs("Quase lá qaralho!"));
console.log(_.match(/a/g, "abc"));
