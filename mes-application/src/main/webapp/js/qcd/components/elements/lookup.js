var QCD = QCD || {};
QCD.components = QCD.components || {};
QCD.components.elements = QCD.components.elements || {};

QCD.components.elements.Lookup = function(_element, _mainController) {
	$.extend(this, new QCD.components.elements.FormComponent(_element, _mainController));
	
	var elementPath = this.elementPath;
	
	var mainController = _mainController;
	
	var lookupWindow;
	
	var inputElement = this.input;
	
	constructor = function(_this) {
		$("#"+_this.elementPath+"_openLookupButton").click(openLookup);
		$(window.document).focus(onWindowClick);
		var elementName = elementPath.replace(/-/g,".");
		window[elementName+"_onReadyFunction"] = function() {
			lookupWindow.init();
		}
		window[elementName+"_onSelectFunction"] = function(entityId, entityString) {
			QCD.info(entityId +" -> "+ entityString);
			inputElement.val(entityString);
		}
	}
	
	this.setComponentData = function(data) {
		inputElement.val(data.value);
	}
	
	this.getComponentData = function() {
		return {
			value : inputElement.val()			
		}
	}
	
	function onWindowClick() {
		closeLookup();
	}
	
	function openLookup() {
		var elementName = elementPath.replace(/-/g,".");
		var location = mainController.getViewName()+".html?lookupComponent="+elementName;
		lookupWindow = window.open(location, 'lookup', 'width=800,height=700');
	}
	
	function closeLookup() {
		if (lookupWindow) {
			lookupWindow.close();
			lookupWindow = null;
		}
	}
	
	constructor(this);
}