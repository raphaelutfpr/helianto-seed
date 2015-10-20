angular.module('app.services').
value('lang', {
	CLOSE:'Close',
	SAVE:'Save',
	ADD:'Add',
	DELETE:'Delete',
	NEW:'New',
	CODE:'Code',
	EDIT:'Edit',
	EXPORT : 'Export',
	MAX_CHAR:'Maximum number of charcaters',

	NO_ITEM:'There are no items',
	ONE_ITEM:'1 item',
	MANY_ITEM:'@{}@ items',
	
	NO_RESULT : 'Nothing found' , 
	NO_CONTENT:'No content', 
	
	//folder
	FOLDER : 'Folder',
	FOLDER_NEW : 'New folder',
	FOLDER_CODE:'Folder code',
	FOLDER_NAME:'Folder name',
	FOLDER_PREFIX:'Prefix',
	FOLDER_NROFDIG:'Number of digits',
	
	_getLocalizationKeys: function() {
//		Returns an object that has as properties the same properties of this object.
//		The values of these properties is equal to the name of each properties.
		var keys = {};
		for (var k in this) {
			keys[k] = k;
		}
		return keys;
	}
});
