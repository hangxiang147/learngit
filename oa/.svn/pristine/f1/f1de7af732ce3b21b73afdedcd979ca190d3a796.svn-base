var commonUtils=(function (){
	var fns_obj={
		replace_fn:function (text) {
            var keys = [].slice.call(arguments, 1), i = 0
            return text.replace(/%s/g, function () {
                return (i < keys.length) ? keys[i++] : ""
            })
        },
        replaceByMark_fn: function (text, mark, array) {
            var keys = array, i = 0;
            var reg = new RegExp(mark, "g");
            return text.replace(reg, function () {
                return (i < keys.length) ? keys[i++] : ""
            })
        }
	}
	return fns_obj;
})()