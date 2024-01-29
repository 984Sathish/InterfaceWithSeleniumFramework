			var preElem = [];
			var i=0;
			var tableCount = 0;

function generate(pgsrc) {
            var input = {};
            var output = Object.extend(input);
            output = setDefaultValues(output);
            output = setDefinitions(output,pgsrc);
            output.url = document.location.href;
            var finalop = JSON.stringify(output);
            return finalop;
        }

Object.extend = function() {
    var key, source;
    var target = {};
    for (var i = 0; i < arguments.length; i++) {
        source = arguments[i];
        for (key in source) {
            target[key] = source[key];
        }
    }
    return target;
}

Array.filter = function(source, predicate) {
    source = source || [];
    var index = -1;
    var length = source.length;
    var target = [];
    var targetIndex = -1;

    while (++index < length) {
        var item = source[index];
        if (predicate(item, index, source)) {
            target[++targetIndex] = item;
        }
    }
    return target;
}

function setDefaultValues(input) {
        var root = this;
        root.LETTERS = { LOWER: 2, CAMEL: 3, NATURAL: 4, PROPER: 5, UPPER: 6 };
        root.VISIBILITIES = { HIDDEN: 1, VISIBLE: 2, ALL: 3 };
        // attributes
        input.attributes = {};
        input.attributes.letter = root.LETTERS.CAMEL;
        input.attributes.indent = false;
        input.attributes.separator = '\n';

        // copyright
        input.copyright = {};
        input.copyright.claimant = '';
        input.copyright.year = '';

        // fill
        input.fill = {};
        input.fill.separator = '';

        // model
        input.model =  {};
        input.model.include = false;
        input.model.name = '';
        input.model.namespace = '';
        input.model.target = '';

        // nodes
        input.nodes =  {};
        // using test framework with AngularJS locators support
        input.nodes.angular = false;
        input.nodes.root = 'body';
        input.nodes.selector = 'a,button,input,select,textarea,span,p,div,tbody' ; // Bhaskar updated 'td'
        input.nodes.visibility =  root.VISIBILITIES.VISIBLE;

        // operations
        input.operations = {};
        input.operations.extras =  {};

        // operations.extras
        input.operations.extras.fill = 1;
        input.operations.extras.fill.submit =1;
        input.operations.extras.submit =1;
        input.operations.extras.verify={};
        input.operations.extras.verify.loaded =1;
        input.operations.extras.verify.url = 1;

        input.operations.letter = root.LETTERS.CAMEL;
        input.operations.separator = '\n';

        input.timeout = 15;
        return input;
    }

var ELEMENT_NODE = 1;
var NG_PREFIXES = ['ng-','data-ng-','ng_','x-ng-','ng\\:'];
var NG_STRATEGIES = [ { handler: getNgModelName, strategy: 'model' } ];
var SHOW_COMMENT = 128;

// ========================================================================
// private functions

function getAttributeSelector(name, node) {
    var response = '';
    var value = node.getAttribute(name);

    if (value) {
        var selector = node.nodeName.toLowerCase();
        if (name === 'class') {
			//console.log(value.trim());
			try{
            selector += '.' + value.trim().split(/\s+/g).join('.');
			}
			catch(e){
				if (e.message == 'Invalid flags supplied to RegExp constructor ' +'\'' +'gg' +'\''){
					selector += '.' + value.trim().split(/\s+/).join('.');
				}
			}
        }
        else {
            value = value.replace(/\r?\n|\r/g, '');
            selector += '[' + name + '=\'' + value + '\']';
        }
        try {
            var elements = document.querySelectorAll(selector);
			//console.log(!elements.match(/?/));
            if (elements.length === 1 && value.length < 100) {
                response = selector;
            }
        } catch (ex) {
            // bad selector. skipping.
        }
    }

    return response;
}

//To identify xpath of a given node or element in DOM using text method of value Innertext
 function getXPATHSelector(node) {
    var response = '';

    if (node.innerText) {
		var value = node.innerText.trim();
		for (; node && node.nodeType === ELEMENT_NODE; node = node.parentNode) {
			if(node.nodeName=='TABLE'){
				selector = '\//' + '*[text()' +'\='  +'\'' +value +'\'' +']';
				//console.log(selector);
				try {
					//var element = document.evaluate( selector, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null ).singleNodeValue.innerText;
					var element = document.evaluate( selector, document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
					if(element.snapshotLength == 1){
						if (element.snapshotItem(0).innerText.trim() === value ) {
							response = selector;
						}
					}
				} catch (ex) {
					//console.log(ex)
					// bad selector. skipping.
				}
				break;
			}
		}
    }
    return response;
}

function getClosestSibling(node, siblings) {
    var copies = [].slice.call(siblings);
    copies.unshift(node);
    var copiesLength = copies.length;
    var closest = 1;

    if (closest > copiesLength) {
        closest = 0;
    }

    var nodeIndex = 0;
    var siblingIndex = closest;

    for (var i = 0; i < copiesLength; i++) {
        var delta = Math.abs(nodeIndex - i);

        if (delta > closest) {
            closest = delta;
            siblingIndex = i;
        }
    }

    return (siblingIndex === 0) ? null : copies[siblingIndex];
}

function getComments(root) {
    var comments = [];
    var index = -1;
    var walker = document.createTreeWalker(root, SHOW_COMMENT, null, false);

    while (walker.nextNode()) {
        comments[++index] = walker.currentNode;
    }

    return comments;
}

function getCSSSelector(node) {
    var selector = '';

	var debugNode = node;
	var	debugNodePar = node.parentNode;

	/*if( node.innerText.trim() === "View All Shop By License")
		{
		 console.log(debugNode);
		 console.log(debugNodePar);
		}*/

    for (; node && node.nodeType === ELEMENT_NODE; node = node.parentNode) {

		/*if(debugNode.innerText.trim()=="View All Shop By License"){
		console.log(node);
		}*/

        if (node && node.id) {
            selector = '#' + node.id + ' ' + selector;
            break;
        }
        else {
            var nodeName = node.nodeName;
            var currentSelector = nodeName.toLowerCase();
            var index = getNodeIndex(node, nodeName);

            if (index > 0) {
                // it has siblings
                currentSelector += ':nth-of-type(' + index + ')';
				selector = '>' +currentSelector +' ' + selector;  //selva
            }
            else {

				if (node.className !== '') {
					currentSelector += '[class=\'' + node.className +'\']';    //selva
                    //currentSelector += '.' + node.className.split(/\s+/g).join('.');
                }

                if (nodeName === 'INPUT') {
                    if (node.getAttribute('type')) {
                        currentSelector += '[type=\'' + node.type + '\']';
                    }
                    else if (node.getAttribute('data-type')) {
                        currentSelector += '[data-type=\'' +
                            node.getAttribute('data-type') + '\']';
                    }
                }
				 selector = currentSelector + ' ' + selector;   //selva
            }


        }
    }
	/*if(debugNode.innerText.trim()=="View All Shop By License"){
	console.log(selector);
	}*/

    return selector.replace(/^html[^\b]*\bbody\b/, '').trim();
}

function getValidVariableName(name) {
    if (name.length && !/[$_a-zA-Z]/.test(name[0])) {
        //return '_' + name;
		return name;
    }
    return name;
}

function getDefinition(input) {
    input = input || {};
    var actionLowered = input.action.toLowerCase();
    var buffer = Object.extend(input.buffer);
    // deep copy
    buffer.attribute = Object.extend(buffer.attribute);
    buffer.operation = Object.extend(buffer.operation);
    var suffixes = {
        action: (actionLowered === 'click') ? ' on' : '',
        label: (actionLowered === 'set') ? ' Field' : ''
    };
    suffixes.documentation = ' ' + getLetter(input.fullText || input.text, LETTERS.NATURAL) +
        ' ' + (input.label + suffixes.label.toLowerCase()) + '.';
    suffixes.name = ' ' + input.text + ' ' + input.label + suffixes.label;

    if (input.negate) {
        input.action = 'Un' + actionLowered;
        buffer.negate = 1;
    }

    if (input.hasArgument) {
        suffixes.action = (input.negate) ? ' value from' : ' value to';
        buffer.argument = {};
        buffer.argument.documentation = input.action + ' default' +
            suffixes.action + suffixes.documentation;
        buffer.argument.key = getLetter(input.text, LETTERS.UPPER);
        buffer.argument.name = getLetter(input.text + ' value',
            input.letters.attribute);
    }

	//To remove duplicate attribute name by appending string "SecondReference"
	if (i==0){
	preElem[i] = getLetter(input.text, input.letters.attribute);
	i = preElem.length;
	}else{
		if((preElem.indexOf(getLetter(input.text, input.letters.attribute))) > -1){
		//console.log("duplicate name found is " +getLetter(input.text, input.letters.attribute));
		var curName = getLetter(input.text, input.letters.attribute);
			if ((preElem.indexOf(curName.concat("SecondReference"))) > -1){
				preElem[preElem.length] = curName.concat("ThirdReference");
			}else{
			preElem[preElem.length] = curName.concat("SecondReference");
			}
		}else{
			preElem[preElem.length]=getLetter(input.text, input.letters.attribute)
		}
	}

	//logic to append parentElement name to child elements name
	var lnode = input.node;
	for (; lnode && lnode.nodeType === ELEMENT_NODE; lnode = lnode.parentNode) {
		var nodeName = lnode.nodeName;
        var currentSelector = nodeName.toLowerCase();
		if (currentSelector=="a"){
			lnode = lnode.parentNode;
			nodeName = lnode.nodeName;
			currentSelector = nodeName.toLowerCase();
			if (currentSelector=="li"){

			}
		}


	}


    buffer.attribute.name = getValidVariableName(preElem[preElem.length-1]);
    buffer.operation.documentation = input.action + suffixes.action +
        suffixes.documentation;
    buffer.operation.name = getLetter(input.action + suffixes.name,
        input.letters.operation, input.action);

    return buffer;
}

function getFileName(path) {
    return path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
}

function getHiddens(cloned, original) {
    var clones = cloned.getElementsByTagName('*');
    var originals = original.getElementsByTagName('*');
    var hiddens = (cloned) ? Array.filter(cloned.querySelectorAll(
        '*:not(br):not(img):not(input):not(link):not(option):not(script):not(select):not(style)'
    ), function(item, index) {
        var sourceIndex = [].indexOf.call(clones, item);
        return originals[sourceIndex].offsetHeight < 1 || !isElementInViewport(item);
    }) : [];
    return hiddens;
}

function getSpecialNodeText(node) {

	var text = '';
	var regex = /^[A-Za-z0-9]+$/;
	if(node.innerText){
		if (node.getAttribute('class')=='x-btn-wrap x-btn-wrap-gw-top-menu-toolbar-small x-btn-split x-btn-split-right'){
			text = node.innerText || node.textContent;
			text = text +'drpdwnArrow';
			}
		else{
			text = node.innerText || node.textContent;
		}
	}

	if (node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small g-unsavedwork-icon '){
		text = 'saveIcon';
		}
	else if(node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-prev '){
		text = 'prevPageArrow';
		}
	else if(node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-first '){
		text = 'firstPageArrow';
		}
	else if(node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-next '){
		text = 'nextPageArrow';
		}
	else if(node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-last '){
		text = 'lastPageArrow';
		}
	else if(node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small g-preferences-icon '){
		text = 'preferenceIcon';
		}
	else{
	}

	text.trim();
	if (!text.match(/\w/)){
		text = '';
	}
	return text;

}

function getLabelText(node) {
    var text = '';

    if (node && node.id) {
        text = getLabelTextFor(node, 'id');
    }

    if (text === '' && node && node.name) {
        // non-standard, but it happens
        text = getLabelTextFor(node, 'name');
    }


    if (text === '') {
        // find label from siblings
        // TODO: should use more aggressive collector
        var labels = (node && node.parentNode) ?
            node.parentNode.querySelectorAll('label') : [];
        var label = getClosestSibling(node, labels);

        if (label) {
            text = label.textContent || label.innerText || '';
            text = text.trim();
        }
    }

    return text;
}

function getLabelTextFor(node, attribute) {
    var identifier = node.getAttribute(attribute) || node[attribute] || '';
    var text = '';

    if (identifier) {
        var label = document.querySelector('label[for="' + identifier + '"]');

        if (label) {
            text = label.textContent || label.innerText || '';
            text = text.trim();
        }

        if (text === '') {
            var identifierLowered = identifier.toLowerCase();
            var labels = Array.filter(document.querySelectorAll('label[for]'), function(item) {
                return item.getAttribute('for').toLowerCase() === identifierLowered;
            });

            label = getClosestSibling(node, labels);

            if (label) {
                text = label.textContent || label.innerText || '';
                text = text.trim();
            }
        }

		if(node.getAttribute('value')){
			text = node.getAttribute('value');
		}

		if(node.getAttribute('placeholder')){
			text = node.getAttribute('placeholder');
		}

		if(node.getAttribute('title')){
			text = node.getAttribute('title');
		}
    }

    return text;
}

function getLetter(value, type, action) {
    action = action || '';
    type = type || LETTERS.CAMEL;
    type = parseInt(type);
    value = value || '';

    if (type !== LETTERS.NATURAL) {
        // move number prefix to the end of the value
        var oldValue = value.replace(action, '').trim();
        var numberPrefix = /^([\d.]+)/.exec(oldValue);
        if (numberPrefix) {
            value = value.replace(numberPrefix[0], '') + ' ' + numberPrefix[0];
        }
    }

    switch (type) {
        case LETTERS.LOWER:
        case LETTERS.UPPER:
            value = value.replace(/\./g, '_').replace(/\s+|__/g, '_').replace(/^_|_$/g, '');
            value = (type === LETTERS.LOWER) ? value.toLowerCase() : value.toUpperCase();
            break;
        case LETTERS.CAMEL:
        case LETTERS.PROPER:
            value = value.replace(/\./g, ' ').trim().replace(/\s\s+/g, ' ').
                replace(/\w\S*/g, function(word) {
                    return word.charAt(0).toUpperCase() + word.substr(1).toLowerCase();
                }).replace(/\s+/g, '');
            if (type === LETTERS.CAMEL) {
                value = value.charAt(0).toLowerCase() + value.substr(1);
            }
            break;
        case LETTERS.NATURAL:
            value = value.trim().replace(/\s\s+/g, ' ').replace(/\w\S*/g, function(word) {
                    return word.charAt(0).toUpperCase() + word.substr(1).toLowerCase();
                });
            break;
    }

    return value;
}

function getLinkText(node) {
    var image = node.querySelector('img');
    var text = '';

    if (image) {
        text = image.alt || getFileName(image.src);
    }

    return text.trim();
}

function getLocator(node, angular) {
    var response = {};

    if (angular) {
        response = getNgLocator(node);
    }

	// Added by Bhaskar
    if (node.nodeName == "TBODY"){
		tableCount ++        	

        if (!response.value && node.parentNode.id) {
            response.strategy = 'id';
			response.value = node.parentNode.id;
			return response;
        }
        else if (!response.value && node.parentNode.name) {
            response.strategy = 'name';
			response.value = node.parentNode.name;
			return response;
        }
        else {
		response.strategy = 'xpath';
		response.value = "//table[" + tableCount + "]/tbody";    // value = "//table[1]/tbody"
		return response;
        }
    }
    
    if (node.parentNode.nodeName == "TR" || node.parentNode.nodeName == "TD"){
    	return response;
    }
	//

    if (!response.strategy) {

		// Commented by Bhaskar for table					  
		//if(!response.value){
		//	response.strategy = 'xpath';
		//	response.value = //getXPATHSelector(node);
		//	}

        if (!response.value && node.id) {
            response.strategy = 'id';
			response.value = node.id;
			response.secondStrategy = 'css'; //Added by Sudha
			response.secondValue = getCSSSelector(node); // Added by Sudha
        }

        else if (!response.value && node.name) {
            response.strategy = 'name';
			response.value = node.name;
			response.secondStrategy = 'css'; //Added by Sudha
			response.secondValue = getCSSSelector(node); // Added by Sudha
        }

        else {

            if (!response.value && node.getAttribute('class')) {
				response.strategy = 'css';
                response.value = getAttributeSelector('class', node);
            }

            if (!response.value && node.title) {
				response.strategy = 'css';
                response.value = getAttributeSelector('title', node);
            }

            if (!response.value && node.getAttribute('href')) {
				response.strategy = 'css';
                response.value = getAttributeSelector('href', node);
            }

            if (!response.value) {
				response.strategy = 'css';
                response.value = getCSSSelector(node);
            }
        }
    }

    return response;
}

function getLongestName(name, longest) {
    name = name || '';
    var length = name.length;

    if (length > longest) {
        longest = length;
    }

    return longest;
}

function getNgLocator(node) {
    var response = {};

    for (var i = 0, j = NG_STRATEGIES.length; i < j; i++) {
        var item = NG_STRATEGIES[i];
        var value = item.handler(node);

        if (value) {
            response.strategy = item.strategy;
            response.value = value;
        }
    }

    return response;
}

function getNgModelName(node) {
    var name = '';

    for (var i = 0, j = NG_PREFIXES.length; i < j; i++) {
        name = node.getAttribute(NG_PREFIXES[i] + 'model') || '';

        if (name) {
            break;
        }
    }

    return name.trim();
}

function getNodeIndex(node, nodeName) {
    nodeName = nodeName || node.nodeName;

    var siblings = (node && node.parentNode) ?
        Array.filter(node.parentNode.children, function(item, index) {
            return item.nodeName === nodeName;
        }) : [];

    var index = [].indexOf.call(siblings, node);
    // convert to 1-based index
    index++;

	/*if(node.innerText.trim()=="View All Shop By License"){
	console.log(siblings);
	console.log(index);
	}*/

    return (index === 1 && index === siblings.length) ? 0 : index;
}

function getNodeText(node) {

    var text = getLabelText(node);

	if (text === '') {
		var parentNode = (node) ? node.parentNode : null;

		if (parentNode) {
			var clonedParentNode = parentNode.cloneNode(true);
			var clonedNode = clonedParentNode.querySelector(
				node.nodeName.toLowerCase());
			clonedNode.parentNode.removeChild(clonedNode);
			clonedParentNode = sanitizeNode(clonedParentNode, parentNode);

			text = clonedParentNode.textContent || clonedParentNode.innerText || '';
			text = getSentences(text.trim())[0] || '';
		}
	}

	if (text === '') {
		text = (node) ? getNodeText(node.parentNode) : 'Randon';
	}

    return text;
}

function getNodeTextFromOuterHTML(node){
    node.getAttribute('value')
}

function getPageVisibleHTML(original) {
    original = original || document.body;
    var cloned = original.cloneNode(true);
    cloned = sanitizeNode(cloned, original);
    return cloned.outerHTML;
}

function getSanitizedText(text, max) {
	try{
    var texts = (text || '').split(/\s+/g);
	}catch(e){
				if (e.message == 'Invalid flags supplied to RegExp constructor ' +'\'' +'gg' +'\''){
					var texts = (text || '').split(/\s+/);
				}
			}

    if (max) {
        texts = texts.slice(0, max);
    }

    return texts.join(' ').trim().replace(/[^a-zA-Z0-9\. ]/g, '');
}

function getSentences(text, minimumWords) {
    minimumWords = minimumWords || 5;
    var index = -1;
    var response = [];
    var sentences = text.match(/[^\r\n.!?]+/gi) || [];

    for (var i = 0, j = sentences.length; i < j; i++) {
        var sentence = sentences[i].trim();

        if (sentence !== '') {
            var words = (sentence.match(/\b\w+\b/gi) || []).length;
            if (words >= minimumWords) {
                // faster array push
                response[++index] = sentence;
            }
        }
    }

    return response;
}

function getSentenceFrequency(sentences, words) {
    if ({}.toString.call(sentences) !== '[object Array]') {
        return [];
    }

    var items = (words.tops.length) ? words.tops : words;
    sentences = sentences.slice(0);
    sentences.frequencies = {};

    for (var i = 0, j = sentences.length; i < j; i++) {
        var sentence = sentences[i];
        var sentenceLowered = sentence.toLowerCase();
        sentences.frequencies[sentence] = 0;

        for (var k = 0, l = items.length; k < l; k++) {
            if (sentenceLowered.indexOf(items[k]) > -1) {
                sentences.frequencies[sentence]++;
            }
        }
    }

    // desc
    sentences.sort(function(a, b) {
        return sentences.frequencies[b] - sentences.frequencies[a];
    });

    return sentences;
}

function getWordFrequency(text) {
    var index = -1;
    var topIndex = -1;
    var words = [];
    words.frequencies = {};
    words.tops = [];

    text.toLowerCase().split(/[\s*\.*\,\;\+?\#\|:\-\/\\\[\]\(\)\{\}$%&0-9*]/).
        map(function(k, v) {
            if (k && k.length > 1) {
                words.frequencies[k]++ || (words.frequencies[k] = 1);
            }
        });

    for (var word in words.frequencies) {
        words[++index] = word;

        if (words.frequencies[word] > 1) {
            words.tops[++topIndex] = word;
        }
    }

    // desc
    words.sort(function(a, b) { return words.frequencies[b] - words.frequencies[a]; });
    words.tops.sort(function(a, b) { return words.frequencies[b] - words.frequencies[a]; });

    return words;
}

function isElementInViewport(el) {
    if (typeof(jQuery) !== 'undefined' && el instanceof jQuery) {
        el = el[0];
    }

    var rect = el.getBoundingClientRect();
    var windowHeight = (window.innerHeight || document.documentElement.clientHeight);
    var windowWidth = (window.innerWidth || document.documentElement.clientWidth);

    return ((rect.left > -1) && (rect.top > -1) &&
        ((rect.left + rect.width) <= windowWidth) &&
        ((rect.top + rect.height) <= windowHeight));
}

function removeNodes(nodes) {
    var type = {}.toString.call(nodes);

    if (type !== '[object Array]' &&
            !(type === '[object NodeList]' || type === '[object Object]')) {
        return;
    }

    var index = -1;
    var length = nodes.length;

    while(++index < length) {
        var node = nodes[index];
        if (node) {
            (node.parentNode || { removeChild: function() {} }).removeChild(node);
        }
    }
}

function sanitizeNode(clonedNode, originalNode) {
    var comments = getComments(clonedNode);
    var excludes = (clonedNode) ?
        clonedNode.querySelectorAll('img,input,link,option,script,select,style') : [];
    var hiddens = getHiddens(clonedNode, originalNode);
    removeNodes(comments);
    removeNodes(excludes);
    var excludedNode = clonedNode.cloneNode(true);
    removeNodes(hiddens);
    // ng:view template doesn't have height,
    // hence it will considered as hidden
    if ((clonedNode.textContent || '').trim() === '') {
        clonedNode = excludedNode;
    }
    return clonedNode;
}

function setDefinitions(input,pgsrc) {
    //var root = document.implementation.createHTMLDocument('');
    //root.body.innerHTML=document;
    var parser = new DOMParser();
    var htmldoc = parser.parseFromString(pgsrc, "text/html");
    //window.open('http://www.amazon.com','_blank');
    var definitions = [];
    var root = document.querySelector(input.nodes.root) || document;
    var nodes = (root) ? root.querySelectorAll(input.nodes.selector) : [];
    var type = {}.toString.call(nodes);

    if (!(type === '[object NodeList]' || type === '[object Object]')) {
        input.definitions = definitions;
        return input;
    }

    var firsts = {};
    var hasField = false;
    var index = -1;
    var longestName = 0;
    var submit = { label: '', text: '' };
    var tags = document.getElementsByTagName('*');
    var texts = {};
    var unsets = {};
    var visibleOnly = (parseInt(input.nodes.visibility) === VISIBILITIES.VISIBLE);

    for (var i = 0, j = nodes.length; i < j; i++) {
        var buffer = { attribute: {}, operation: {} };
        var definition = {};
        var node = nodes[i];

		var tdParse = true;

		// Updated by Bhaskar "TBODY"
		if (node.nodeName == "TBODY" || node.nodeName == "TD" || node.nodeName == "SPAN" || node.nodeName == "DIV" || node.nodeName == "P"){
			/*if(node.innerText=="My Activities" && node.nodeName == "SPAN"){
			console.log("span node found")
			}*/

			if (node.childNodes.length == 1){
				if (node.firstChild.nodeName != '#text'){
					tdParse =  false;
				}
			}

			if (node.childNodes.length > 1){
					tdParse =  false;
			}

			for (; node && node.nodeType === ELEMENT_NODE; node = node.parentNode) {
				if(node.parentNode.nodeName=='A' || node.parentNode.nodeName=='INPUT' || node.parentNode.nodeName=='BUTTON' || node.parentNode.nodeName=='SELECT'){
					tdParse = false;
					break;
				}
			}
			node = nodes[i];

			if(( node.nodeName == "SPAN" || node.nodeName == "DIV" )  && (node.parentNode.nodeName == "SPAN" || node.parentNode.nodeName == "DIV" || node.parentNode.nodeName == "P")){
				var prntFlag = false;
				var children = node.parentNode.childNodes;
				for (child in children)
				{
					if (children[child].nodeName=='#text'){
						node = node.parentNode;
						prntFlag = true;
						break;
					}
				}
				if(!prntFlag){
					node = nodes[i];
				}
			}

			if (node.nodeName == 'SPAN' && (node.getAttribute('class')=='x-btn-wrap x-btn-wrap-gw-top-menu-toolbar-small x-btn-split x-btn-split-right' ||
			node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small g-unsavedwork-icon ' ||
			node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-prev ' ||
			node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-first ' ||
			node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-next ' ||
			node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small x-tbar-page-last ' ||
			node.getAttribute('class')=='x-btn-icon-el x-btn-icon-el-plain-toolbar-small g-preferences-icon ')){
				tdParse = true;
			}

			// Added by Bhaskar
            if (node.nodeName == "TBODY"){
            	tdParse = true;
            }

		}


        if (visibleOnly && node.offsetHeight > 0 && tdParse) {
            var action = '';
            var hasArgument = false;
            var hasUnset = false;
            var label = '';
			var text = node.textContent || node.innerText || '';
            var locator = getLocator(node, input.nodes.angular);

			// Added by Bhaskar
            if (node.parentNode.nodeName == "TR" || node.parentNode.nodeName == "TD"){
				break;
			}

            buffer.attribute.strategy = locator.strategy;
            buffer.attribute.value = locator.value;
			buffer.attribute.secondStrategy = locator.secondStrategy; //Added by Sudha
            buffer.attribute.secondValue = locator.secondValue; //Added by Sudha							 
            buffer.sourceIndex = node.sourceIndex || [].indexOf.call(tags, node);

            switch(node.nodeName) {
                case 'A':
                    action = 'Click';
                    buffer.type = 'link';
                    label = 'Link';
                    text = text || getLinkText(node);

                    if (submit.text === '' && text.toLowerCase().indexOf('submit') > -1) {
                        submit.label = label;
                        submit.text = text;
                    }
                    break;
                case 'BUTTON':
                    action = 'Click';
                    buffer.type = 'button';
                    label = 'Button';

                    if (submit.text === '' && ((node.type || '').toLowerCase() === 'submit' ||
                            text.toLowerCase().indexOf('submit') > -1)) {
                        submit.label = label;
                        submit.text = text;
                    }
                    break;
                case 'INPUT':
                    var inputType = node.type || '';

                    if ('|button|image|submit|'.indexOf('|' + inputType + '|') > -1) {
                        action = 'Click';
                        buffer.type = 'button';
                        label = 'Button';
                        text = text || node.value || getNodeText(node);

                        if (inputType === 'submit') {
                            submit.label = label;
                            submit.text = text;
                        }
                        else if (submit.text === '' && text.toLowerCase().
                            indexOf('submit') > -1) {
                                submit.label = label;
                                submit.text = text;
                            }
                    }
                    else {
                        if (inputType === 'hidden') {
                            break;
                        }
                        else if (inputType === 'checkbox') {
                            hasUnset = true;
                        }
                        else if ('|email|number|password|radio|search|tel|text|url|'.
                                indexOf('|' + inputType + '|') > -1) {
                            hasArgument = true;
                        }

                        label = getLetter(inputType, LETTERS.PROPER);
                        text = text || getNodeText(node);

                        if (inputType === 'radio') {
                            label = 'Radio Button';
                            if (buffer.attribute.strategy !== 'name' && node.id) {
                                buffer.attribute.strategy = 'id';   //Selva
                                buffer.attribute.value = node.id;   //Selva
                            }

                            /* var radioValueBuffer = {
                                attribute: {
									strategy: 'id',   //Selva
                                    name: getLetter(getSanitizedText(text, 6) + ' Value',
                                        input.attributes.letter),
                                    value: node.id   //Selva
                                },
                                operation: {},
                                sourceIndex: -1,
                                type: 'radio'   //Selva
                            };

                            // faster array push
                            definitions[++index] = radioValueBuffer;

                            longestName = getLongestName(radioValueBuffer.attribute.name,
                                longestName);  */
                        }

                        if ('|email|number|password|search|tel|url|'.
                                indexOf('|' + inputType + '|') > -1) {
                            inputType = 'text';
                        }

                        action = 'Set';
                        buffer.type = inputType;
                    }
                    break;
                case 'SELECT':
                    action = 'Set';
                    buffer.type = 'select';
                    hasArgument = true;
                    hasUnset = true;
                    label = 'Drop Down List';
                    text = getNodeText(node);
                    break;
                case 'TEXTAREA':
                    action = 'Set';
                    buffer.type = 'label';
                    hasArgument = true;
                    label = 'Textarea';
                    text = getNodeText(node);
                    break;
				case 'TBODY':   //Bhaskar	
                    action = 'None';
                    buffer.type = 'table';
                    hasArgument = true;
                    label = 'Table';
                    //text = getSpecialNodeText(node);
                    text = "Table" + tableCount
                    break;
                case 'TD':   //Selva
                    action = 'None';
                    buffer.type = 'label';
                    hasArgument = true;
                    label = 'Label';
                    text = getSpecialNodeText(node);
                    break;
				case 'SPAN':   //Selva
                    action = 'None';
                    buffer.type = 'label';
                    hasArgument = true;
                    label = 'Label';
                    text = getSpecialNodeText(node);
                    break;
				case 'DIV':   //Selva
                    action = 'None';
                    buffer.type = 'label';
                    hasArgument = true;
                    label = 'Label';
                    text = getSpecialNodeText(node);
                    break;
				case 'P':   //Selva
                    action = 'None';
                    buffer.type = 'label';
                    hasArgument = true;
                    label = 'Label';
                    text = getSpecialNodeText(node);
                    break;

            }

            var fullText = getSanitizedText(text);
            text = getSanitizedText(text, 10);

            if (text !== '') {
                if (texts[text]) {
                    texts[text]++;

                    if (texts[text] === 2) {
                        var firstText = text + ' 1';

                        // need to adjust the first entry and make it as part of the group
                        definition = getDefinition({
                            action: action,
                            buffer: definitions[firsts[text]],
                            fullText: fullText,
                            hasArgument: hasArgument,
                            label: label,
                            letters: {
                                attribute: input.attributes.letter,
                                operation: input.operations.letter
                            },
                            text: firstText,
							node
                        });

                        definitions[firsts[text]] = definition;

                        if (hasUnset) {
                            definition = getDefinition({
                                action: action,
                                buffer: definitions[unsets[text]],
                                fullText: fullText,
                                hasArgument: hasArgument,
                                label: label,
                                letters: {
                                    attribute: input.attributes.letter,
                                    operation: input.operations.letter
                                },
                                negate: hasUnset,
                                text: firstText,
								node
                            });

                            definitions[unsets[text]] = definition;
                        }
                    }

                    text = text + ' ' + texts[text];
                }
                else {
                    firsts[text] = index + 1;
                    texts[text] = 1;

                    if (hasUnset) {
                        unsets[text] = index + 2;
                    }
                }

                definition = getDefinition({
                    action: action,
                    buffer: buffer,
                    fullText: fullText,
                    hasArgument: hasArgument,
                    label: label,
                    letters: {
                        attribute: input.attributes.letter,
                        operation: input.operations.letter
                    },
                    text: text,
					node
                });

                // faster array push
                definitions[++index] = definition;

                longestName = getLongestName(definition.attribute.name, longestName);

                if (hasUnset) {
                    definition = getDefinition({
                        action: action,
                        buffer: buffer,
                        fullText: fullText,
                        hasArgument: hasArgument,
                        label: label,
                        letters: {
                            attribute: input.attributes.letter,
                            operation: input.operations.letter
                        },
                        negate: hasUnset,
                        text: text,
						node
                    });

                    // faster array push
                    definitions[++index] = definition;
                }

                if (!hasField && action === 'Set') {
                    hasField = true;
                }
            }
        }
    }

    // operation extras
    if (hasField && input.operations.extras.fill) {
        var buffer = {
            attribute: {},
            operation: {
                documentation: 'Fill every fields in the page.',
                name: getLetter('Fill', input.operations.letter)
            },
            negate: true,
            sourceIndex: -1,
            type: 'fill'
        };

        // faster array push
        definitions[++index] = buffer;
    }

    if (hasField && submit.text !== '' && input.operations.extras['fill.submit']) {
        var buffer = {
            attribute: {},
            operation: {
                documentation: 'Fill every fields in the page and submit it to target page.',
                name: getLetter('Fill And Submit', input.operations.letter)
            },
            negate: true,
            sourceIndex: -1,
            target: {
                modelName: input.model.target
            },
            type: 'fill.submit'
        };

        // faster array push
        definitions[++index] = buffer;
    }

    if (submit.text !== '' && input.operations.extras.submit) {
        var buffer = {
            attribute: {},
            operation: {
                documentation: 'Submit the form to target page.',
                name: getLetter('Submit', input.operations.letter)
            },
            negate: true,
            sourceIndex: -1,
            target: {
                modelName: input.model.target,
                name: getLetter('Click ' + getSanitizedText(submit.text) + ' ' +
                    submit.label, input.operations.letter)
            },
            type: 'submit'
        };

        // faster array push
        definitions[++index] = buffer;
    }

    if (input.operations.extras['verify.loaded']) {
        var sourceText = getPageVisibleHTML();
        sourceText = sourceText.replace(/(<([^>]+)>)/gi, '\n');
        var sentences = getSentences(sourceText);
        var words = getWordFrequency(sourceText);
        sentences = getSentenceFrequency(sentences, words);
        var sentence = sentences[0] || '';

        // !robot
        if (input.attributes.letter !== LETTERS.LOWER && input.attributes.indent !== 1 &&
                input.attributes.separator !== '') {
            //sentence = sentence.replace(/"/g, '\\"');
            sentence = sentence.replace('"', '\\"')
        }

        var buffer = {
            attribute: {
                name: getLetter('Page Loaded Text', input.attributes.letter),
                value: sentence
            },
            operation: {
                documentation: 'Verify that the page loaded completely.',
                name: getLetter('Verify Page Loaded', input.operations.letter)
            },
            sourceIndex: -1,
            type: 'verify.loaded'
        };

        // faster array push
        definitions[++index] = buffer;
    }

    if (input.operations.extras['verify.url']) {
        // it's better to generate more information than less
        var uri = location.href.replace(document.location.origin, '');

        var buffer = {
            attribute: {
                name: getLetter('Page Url', input.attributes.letter),
                value: uri
            },
            operation: {
                documentation: 'Verify that current page URL matches the expected URL.',
                name: getLetter('Verify Page Url', input.operations.letter)
            },
            sourceIndex: -1,
            type: 'verify.url'
        };

        // faster array push
        definitions[++index] = buffer;
    }

    input.attributes.longestName = longestName;

    input.definitions = definitions;
	input = rmvDuplctAtribVal(input);
	
    return input;
}

function rmvDuplctAtribVal(input){       //Selva
    var lclDef = new Array();
    lclDef = input.definitions;
    var lclDefLen = input.definitions.length;
    for (var vc = 0; vc < lclDefLen - 3; vc++) {

           for (var vc2 = vc + 1; vc2 < lclDefLen - 2; vc2++) {

                 //console.log("iteration no1 "+vc +" no2 " +vc2);
                 var attV1 = Object.extend(lclDef[vc].attribute);
                 var attV2 = Object.extend(lclDef[vc2].attribute);
                 var aval1 = attV1.value;
                 var aval2 = attV2.value;
                                  var attName2 = input.definitions[vc2].attribute.name;
                 if (aval1 == aval2) {              //Updated by Sudha
                        //console.log("duplicate attr val is "+aval2);
                        if (attName2.includes("SecondReference")) {
                               input.definitions.splice(vc2, 1);
                               vc2--;
                               lclDefLen--;
                        } else if (input.definitions[vc].attribute.secondValue !== 'undefined' && input.definitions[vc2].attribute.secondValue !== 'undefined') {
                               input.definitions[vc].attribute.strategy = input.definitions[vc].attribute.secondStrategy;
                               input.definitions[vc].attribute.value = input.definitions[vc].attribute.secondValue;
                               input.definitions[vc2].attribute.strategy = input.definitions[vc2].attribute.secondStrategy;
                               input.definitions[vc2].attribute.value = input.definitions[vc2].attribute.secondValue;
                        } 
                 } //
           }
    }
    return input;
}	

return generate(arguments[0]);