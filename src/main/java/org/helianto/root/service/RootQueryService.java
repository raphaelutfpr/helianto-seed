package org.helianto.root.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.core.internal.QualifierAdapter;
import org.helianto.core.internal.SimpleCounter;
import org.helianto.root.repository.RootRepository;
import org.helianto.security.internal.UserAdapter;
import org.helianto.security.repository.EntityStatsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Entity network query service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class RootQueryService {
	
	@Inject
	private EntityStatsRepository entityStatsRepository;

	@Inject
	private RootRepository rootRepository;

	/**
	 * Lista qualificadores.
	 * 
	 * @param entityId
	 */
	public List<QualifierAdapter> qualifier(int entityId) {
		List<QualifierAdapter> qualifierList 
			= QualifierAdapter.qualifierAdapterList(InternalEntityType.values());
		
		// realiza a contagem
		qualifierCount(entityId, qualifierList);

		return qualifierList;
	}

	/**
	 * Método auxiliar para contar os qualificadores.
	 * 
	 * @param entityId
	 * @param qualifierList
	 */
	protected void qualifierCount(int entityId, List<QualifierAdapter> qualifierList) {
		
		// TODO Today default operator is  = 1
		//
		
		// conta o entidades agrupados por categoria
		List<SimpleCounter> counterListAll 
			= entityStatsRepository.countActiveEntitiesGroupByType(1);
		
		// para cada qualificador preenchemos as contagens
		for (QualifierAdapter qualifier: qualifierList) {
			qualifier
			.setCountItems(counterListAll);
		}
					
	}
	
	/**
	 * Página de entidades vinculadas ao usuário.
	 * 
	 * @param identityId
	 * @param entityType
	 */
	public Page<UserAdapter> listUser(int identityId, char entityType) {
		Pageable page = new PageRequest(0, 100, Direction.DESC, "lastEvent");
		Page<UserAdapter> userAdaptaterPage = 
				rootRepository.findByIdentityIdAndEntityTypeOrderByLastEventDesc(
						identityId, entityType, 'A', 'A', page);
		return userAdaptaterPage;
	}
	
	/**
	 * Search.
	 * 
	 * @param entityId
	 * @param searchString
	 * @param pageNumber
	 */
	public Page<UserAdapter> search(Integer entityId, String searchString, Integer pageNumber) {
		Pageable paged = new PageRequest(pageNumber, 10, Direction.DESC, "issueDate");
		return rootRepository.findByIdentityIdAndEntityOrderByLastEventDesc(entityId, "%"+searchString+"%", paged);
	}
	
	/**
	 * Find entity by user.
	 * 
	 * @param userId
	 */
	public UserAdapter entity(int userId) {
		Entity entity = rootRepository.findByUserId(userId);
		return new UserAdapter(userId, entity);
	}
	
	/**
	 * Enumeração interna para definir tipos
	 * de entidade para este contexto de aplicação.
	 * 
	 * @author mauriciofernandesdecastro
	 */
	static enum InternalEntityType implements KeyNameAdapter {
		
		STARTUPS('C', "Startups"),
		SPINOFFS('F', "Spinoffs"),
		MENTORING('M', "Mentorias"),
		INVESTORS('I', "Investidores");
		
		private char value;
		private String desc;
		
		/**
		 * Construtor.
		 * 
		 * @param value
		 */
		private InternalEntityType(char value, String desc) {
			this.value = value;
			this.desc = desc;
		}
		
		public Serializable getKey() {
			return this.value;
		}
		
		@Override
		public String getCode() {
			return value+"";
		}
		
		@Override
		public String getName() {
			return desc;
		}
		
	}

}
