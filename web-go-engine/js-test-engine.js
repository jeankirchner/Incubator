function TestControl() {
    this.tests = {};
    this.passed = [];
    this.failed = [];
}

TestControl.prototype = {
    constructor: TestControl,

    registerTest: function(name, fn) {
        if (!name) throw "No name specified";
        if (!name) throw "No test specified for: " + name;
        if (this.tests[name]) throw "Test: " + name + " already registered";
        this.tests[name] = {
            isTest: true, //dummy control
            name: name,
            fn: fn
        };
    },

    registerSuite: function(suite) {
        function isSuite(name) {
            return suite[name].isSuite;
        }

        function isTest(name) {
            if (typeof name != "string") return false;
            return name.indexOf("test") == 0;
        }
        for (var x in suite) {
            if (isSuite(x)) {
                this.registerSuite(suite[x]);
            } else if (isTest(x)) {
                this.registerTest(x, suite[x]);
            }
        }
    },

    run: function() {
        for (var x in this.tests) {
            var test = this.tests[x];
            if (test && test.isTest) {
                try {
                    test.fn();
                    this.pass(this.tests[x]);
                } catch (e) {
                    test.error = e;
                    this.fail(test);
                }
            }
        }
    },

    pass: function(test) {
        this.passed.push("Test: " + test.name + " suceeded!");
    },

    fail: function(test) {
        var e = test.error;
        this.failed.push("Test: " + test.name + " failed: " + e);
    }
};

TestControl.get = function() {
    if (!TestControl.instance) {
        TestControl.instance = new TestControl();
    }
    return TestControl.instance;
}

function runTests() {
    TestControl.get().run();
}

function assert(cond, msg) {
    if (!cond) {
        if (msg) {
            throw new Error(msg);
        } else {
            throw new Error("fail");
        }
    }
}

function assertEquals(_1, _2, msg) {
    assert(_1 == _2, msg);
}
