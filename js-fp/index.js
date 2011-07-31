bool = function(x){return !!x;};
array = function(args) {
  return [].slice.call(args);
}

asArray = function() {
  return array(arguments);
};

curry = function(fn) {
  var acumm = function() {
    var args = array(arguments);
    if (args.length >= fn.length) return fn.apply(this, args);

    return function() {
      return acumm.apply(this, args.concat(array(arguments)));
    };
  };
  return acumm;
};

compose = function(fnA, fnB) {
  return function(x) {
    return fnA(fnB(x));
  };
};


concat = curry(function(a,b){return a.concat(b);});

reverseArr = function(arr) {
  return array(arr).reverse();
};

reverseFn = function(fn) {
  return function() {
    return fn.apply(this, reverseArr(arguments));
  }
};

negate = function(fn) {
  return function() {
    return !fn.apply(this, arguments);
  };
}

reduce = curry(function(fn, acc, arr) { 
  var accumulator = acc;
  arr.forEach(function(it) {
    accumulator = fn(accumulator, it);
  });
  return accumulator; 
});

map2 = curry(function(fn, arr) { 
  return reduce(function(acc,it) {
    return concat(acc, fn(it));
  }, [], arr);
});

map = curry(function(f, obj) {
  return obj.map(f);
});

match = curry(function(regex, s) {
  return s.match(regex);
});

toString = function(obj) {
  return obj.toString();
};

get = curry(function(prop, obj) { return obj[prop]; });

id = function(x) {return x;}
multiply = curry(function(a,b){return a*b;});
add = curry(function(a,b){return a+b;});
gt = curry(function(a,b){return a>b;});
gte = curry(function(a,b){return a>=b;});
lt = curry(function(a,b){return a<b;});
lt = curry(function(a,b){return a<=b;});
eq = curry(function(a,b){return a==b;});
eqq = curry(function(a,b){return a===b;});
neq = negate(eq);
neqq = negate(eqq);
mod = curry(function(a,b){return a%b;});
inc = add(1);
dec = add(-1);
not = function(a){return !a;};
ternary = curry(function(a,b,c){return a?b:c;});


fork = curry(function(last, f, g, x) {
  return last(f(x), g(x));
});

slice = curry(function(from, to, it) {
  return it.slice(from, to);
});

head = slice(0,1);
tail = slice(1, undefined);

var greatest = curry(function(a,b) {
  return ternary(a>b,a,b);
});

var smallest = curry(function(a,b) {
  return ternary(a<b,a,b);
});

toUpper = function(s) {
  return s.toUpperCase();
};

capitalize = fork(add, compose(toUpper, head), tail);

function _Id(v) {
  this.val = v
};

_Id.prototype.map = function(f) {
  return Id(f(this.val));
};

_Id.prototype.toString = function() {
  return "Id(" + this.val + ")";
};

function Id(v) {
  return new _Id(v);
};

_Maybe = function(v) {
  this.val = v;
};

_Maybe.prototype.map = function(f) {
  return this.val ? Maybe(f(this.val)) : Maybe(null);
};

_Maybe.prototype.toString = function() {
  return "Maybe(" + this.val + ")";
};

function Maybe(v) {
  return new _Maybe(v);
};

//Either
function _Left(v) {
  this.val = v;
};
_Left.prototype.map = function(f) {
  return Left(this.val);
};
_Left.prototype.toString = function() { return "Left(" + this.val + ")"};
function Left(v) {return new _Left(v);};

function _Right(v) {
  this.val = v;
};
_Right.prototype.map = function(f) {
  return Right(f(this.val));
};
_Right.prototype.toString = function() {
  return "Right(" + this.val + ")";
};
function Right(v) { return new _Right(v);};

var safeGet = curry(function(x,o){ return Maybe(o[x]); });

function log() {
  map(toString, array(arguments)).forEach(function(str) {
    document.write(str);
    document.write("<br/>")
  });
  return array(arguments);
}

