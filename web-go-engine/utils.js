
    function mergeProps(obj1, obj2) {
        for (x in obj2) {
            obj1[x] = obj2[x];
        }
    }

    function extends(baseClass, extension) {
        var extendedClass = function(){};
        extendedClass.prototype = new baseClass();

        mergeProps(extendedClass.prototype, extension);
        return extendedClass;
    }

