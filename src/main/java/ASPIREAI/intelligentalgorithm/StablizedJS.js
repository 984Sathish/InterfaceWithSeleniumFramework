
function getAlgoLocators(elem) {
	// console.log("am an element type of following string: ");
	// console.log(Object.prototype.toString.call(elem))
	var arrayOfLocators = getUniqueSelector(elem);
	return arrayOfLocators;

}

function getUniqueSelector(elSrc) {
	if (!(elSrc instanceof Element)){
		//return;
		if(elSrc instanceof Array){
		elSrc = elSrc[0];
	} else
		return;
	}
	

	var bSel = [];

	/**
	 *if value is not:

	 *null
	 *undefined
	 *NaN
	 *empty string ("")
	 *false
	 *0
	
	 **/

	var locatorId = getId(elSrc);
	if (locatorId)
		bSel.push(locatorId);
	//return locatorId;

	var locatorClass = getClass(elSrc);
	if (locatorClass)
		bSel.push(locatorClass);
	//return locatorClsNtype;

	var locatorClsNtype = getClassNtype(elSrc);
	if (locatorClsNtype)
		bSel.push(locatorClsNtype);
	//return locatorClsNtype;

	var locatorNameAttr = getNameAttribute(elSrc);
	if (locatorNameAttr)
		bSel.push(locatorNameAttr);
	//return locatorNameAttr;

	var locatorNameNtype = getNameNtype(elSrc);
	if (locatorNameNtype)
		bSel.push(locatorNameNtype);
	//return locatorNtype;

	/*var locatorNtype = getNtype(elSrc)
	if (locatorNtype)
		bSel.push(locatorNtype);*/
	//return locatorNtype;

	/*var locatorNchild = getNchild(elSrc);
	if (locatorNchild)
		bSel.push(locatorNchild);*/
	// return locatorNchild;

	var locatorClsNchilid = getClassNtypeNchild(elSrc);
	if (locatorClsNchilid)
		bSel.push(locatorClsNchilid);
	//return locatorClsNchilid;

	/*var locatorNtypeNchild = getNtypeNchild(elSrc);
	if (locatorNtypeNchild)
		bSel.push(locatorNtypeNchild);*/
	//return locatorNtypeNchild;

	return bSel;

}

//To get an element with Id attribute

function getId(elem) {
	console.log(elem);
	var aSel = [];

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	if (elem.id) {
		aSel.unshift('#' + elem.id);
		if (uniqueQuery()) {
			return aSel[0];
		}
		return null;
	}

}

//To get an element with Class attribute

function getClass(elem) {
	var aSel = [];
	var sSel;
	aSel.unshift(sSel = elem.nodeName.toLowerCase());
  
	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	if (elem.className) {
		aSel[0] = sSel += '.' + elem.className.trim().replace(/ +/g, '.');
		if (uniqueQuery()) {
			return aSel[0];
		}
		return null;
	}

}

//To get an element with class and nth-type() function

function getClassNtype(elem) {
	var aSel = [];
	var sSel;

	getSelector = function(elem) {
		
		if (!(elem instanceof Element)) return false;

		aSel.unshift(sSel = elem.nodeName.toLowerCase());

		if (elem.className)
			aSel[0] = sSel += '.' + elem.className.trim().replace(/ +/g, '.');

		var elChild = elem, sChild, n = 1;
		while (elChild = elChild.previousElementSibling) {
			if (elChild.nodeName === elem.nodeName)
				++n;
		}
		aSel[0] = sSel += ':nth-of-type(' + n + ')';
		if (uniqueQuery()) {
			return true;
		}

		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

//To get locator name with array of attributes 

function getNameAttribute(elem) {

	var aSel = [];
	var sSel;
	var aAttr = ['name', 'value', 'title', 'placeholder', 'data-*' ];

	getSelector = function(el) {
		
		if (!(el instanceof Element)) return false;
		
		aSel.unshift(sSel = el.nodeName.toLowerCase());

		for (var i = 0; i < aAttr.length; ++i) {
			if (aAttr[i] === 'data-*') {
				// Build array of data attributes
				var aDataAttr = [].filter.call(el.attributes, function(attr) {
					return attr.name.indexOf('data-') === 0;
				});
				for (var j = 0; j < aDataAttr.length; ++j) {
					aSel[0] = sSel += '[' + aDataAttr[j].name + '="'
							+ aDataAttr[j].value + '"]';
					if (uniqueQuery()) {
						return true;
					}
				}
			} else if (el[aAttr[i]]) {
				aSel[0] = sSel += '[' + aAttr[i] + '="' + el[aAttr[i]] + '"]';
				if (uniqueQuery()) {
					return true;
				}
			}
		}
		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

//To get an element with name and nth-type() function
function getNameNtype(elem) {

	var aSel = [];
	var sSel;
	var aAttr = ['name', 'value', 'title', 'placeholder', 'data-*' ];

	getSelector = function(el) {
		
		if (!(el instanceof Element)) return false;
		
		aSel.unshift(sSel = el.nodeName.toLowerCase());

		for (var i = 0; i < aAttr.length; ++i) {
			if (aAttr[i] === 'data-*') {
				// Build array of data attributes
				var aDataAttr = [].filter.call(el.attributes, function(attr) {
					return attr.name.indexOf('data-') === 0;
				});
				for (var j = 0; j < aDataAttr.length; ++j) {
					aSel[0] = sSel += '[' + aDataAttr[j].name + '="'
							+ aDataAttr[j].value + '"]';
					if (uniqueQuery()) {
						//return true;
					}
				}
			} else if (el[aAttr[i]]) {
				aSel[0] = sSel += '[' + aAttr[i] + '="' + el[aAttr[i]] + '"]';
				if (uniqueQuery()) {
					// return true;
				}
			}
		}

		var elChild = el, sChild, n = 1;
		while (elChild = elChild.previousElementSibling) {
			if (elChild.nodeName === el.nodeName)
				++n;
		}
		aSel[0] = sSel += ':nth-of-type(' + n + ')';
		if (uniqueQuery()) {
			return true;
		}
		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

//To get locator with nth-of-type funtion alone

function getNtype(elem) {

	var aSel = [];
	var sSel;

	getSelector = function(el) {
		
		if (!(el instanceof Element)) return false;

		aSel.unshift(sSel = el.nodeName.toLowerCase());

		var elChild = el, sChild, n = 1;
		while (elChild = elChild.previousElementSibling) {
			if (elChild.nodeName === el.nodeName)
				++n;
		}
		aSel[0] = sSel += ':nth-of-type(' + n + ')';
		if (uniqueQuery()) {
			return true;
		}
		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

//To get locator with combination of nth-child

function getNchild(elem) {

	var aSel = [];
	var sSel;

	getSelector = function(el) {
		
		if (!(el instanceof Element)) return false;

		aSel.unshift(sSel = el.nodeName.toLowerCase());

		var elChild = el, n = 1;
		while (elChild = elChild.previousElementSibling)
			++n;
		aSel[0] = sSel = sSel.replace(/:nth-of-type\(\d+\)/,
				n > 1 ? ':nth-child(' + n + ')' : ':first-child');
		if (uniqueQuery()) {
			return true;
		}

		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

//To get locator with combination of Class and nth-child & nth-type concept

function getClassNtypeNchild(elem) {
	var aSel = [];
	var sSel;

	getSelector = function(el) {
		
		if (!(el instanceof Element)) return false;

		aSel.unshift(sSel = el.nodeName.toLowerCase());

		if (el.className)
			aSel[0] = sSel += '.' + el.className.trim().replace(/ +/g, '.');

		var elChild = el, sChild, n = 1;
		while (elChild = elChild.previousElementSibling) {
			if (elChild.nodeName === el.nodeName)
				++n;
		}
		aSel[0] = sSel += ':nth-of-type(' + n + ')';
		if (uniqueQuery()) {
			//return true;
		}
		elChild = el, n = 1;
		while (elChild = elChild.previousElementSibling)
			++n;
		aSel[0] = sSel = sSel.replace(/:nth-of-type\(\d+\)/,
				n > 1 ? ':nth-child(' + n + ')' : ':first-child');
		if (uniqueQuery()) {
			return true;
		}
		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

//To get locator with combination of nth-child and nth-type 

function getNtypeNchild(elem) {
	var aSel = [];
	var sSel;

	getSelector = function(el) {
		
		if (!(el instanceof Element)) return false;

		aSel.unshift(sSel = el.nodeName.toLowerCase());

		var elChild = el, sChild, n = 1;
		while (elChild = elChild.previousElementSibling) {
			if (elChild.nodeName === el.nodeName)
				++n;
		}
		aSel[0] = sSel += ':nth-of-type(' + n + ')';
		if (uniqueQuery()) {
			//return true;
		}
		elChild = el, n = 1;
		while (elChild = elChild.previousElementSibling)
			++n;
		aSel[0] = sSel = sSel.replace(/:nth-of-type\(\d+\)/,
				n > 1 ? ':nth-child(' + n + ')' : ':first-child');
		if (uniqueQuery()) {
			return true;
		}
		return false;
	},

	uniqueQuery = function() {
		return document.querySelectorAll(aSel.join('>') || null).length === 1;
	};

	while (elem.parentNode) {
		if (getSelector(elem))
			return aSel.join(' > ');
		elem = elem.parentNode;
	}
}

return getAlgoLocators(arguments[0]);