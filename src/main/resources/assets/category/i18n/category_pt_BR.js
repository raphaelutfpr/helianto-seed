angular.module('app.services').
value('lang', {
	SAVE :'Salvar', 
	CLOSE :'Fechar', 
	ITEMS :'Itens', 
	CATEGORY_CODE :'Código', 
	CATEGORY_NAME : 'Nome', 
	CATEGORY :'Categoria', 
	CATEGORIES :'Categorias', 
	CATEGORY_EXISTS :'Categoria já existe'
	
	//tipos
	, CATEGORY_NOT_DEFINED:'Indefinido'
	, CATEGORY_DOCUMENT:'Documentos'
	, CATEGORY_UNIT:'Unidades'
	, CATEGORY_INSTRUMENT:'Instrumentos'
	, CATEGORY_MAINTENANCE:'Manutenção'
	, CATEGORY_PROCESS:'Processos'
	, CATEGORY_STOCK:'Estoques'
	, CATEGORY_ORDER:'Ordens'
	, CATEGORY_PRODUCT:'Produtos'
	, CATEGORY_SERVICE:'Serviços'
	, CATEGORY_REQUEST:'Solicitações'
	, CATEGORY_PROJECT:'Projeto'
	, CATEGORY_PLANNING:'Planejamento'
	, CATEGORY_COMPETENCE:'Competências'
	, CATEGORY_INDICATOR:'Indicadores', 
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
