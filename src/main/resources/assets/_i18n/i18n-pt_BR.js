angular.module('app.services').
value('lang', {
	CLOSE:'Fechar',
	SAVE:'Salvar',
	ADD:'Adicionar',
	DELETE:'Excluir',
	NEW:'Novo',
	CODE:'Código',
	EDIT:'Editar',
	EXPORT : 'Exportar',
	MAX_CHAR:'Número máximo de caracteres',

	NO_ITEM:'Não há itens',
	ONE_ITEM:'1 item',
	MANY_ITEM:'@{}@ itens',
	
	NO_RESULT : 'Nada encontrado' , 
	NO_CONTENT:'Não há conteúdo', 
	
	//folder
	FOLDER : 'Pasta',
	FOLDER_NEW : 'Nova pasta',
	FOLDER_CODE:'Código da pasta',
	FOLDER_NAME:'Nome da pasta',
	FOLDER_PREFIX:'Prefixo',
	FOLDER_NROFDIG:'Número de dígitos',
	
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
