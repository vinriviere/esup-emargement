package org.esupportail.emargement.repositories;

import java.util.Date;
import java.util.List;

import org.esupportail.emargement.domain.Campus;
import org.esupportail.emargement.domain.Context;
import org.esupportail.emargement.domain.SessionEpreuve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionEpreuveRepository extends JpaRepository<SessionEpreuve, Long>{

	Long countByNomSessionEpreuve(String nomSessionEpreuve);
	
	Page<SessionEpreuve> findByNomSessionEpreuve(String nomSessionEpreuve, Pageable pageable);
	
	List<SessionEpreuve> findSessionEpreuveByIsSessionEpreuveClosedFalseOrderByNomSessionEpreuve();
	
	List<SessionEpreuve>  findSessionEpreuveByDateExamenAndCampusEqualsAndIdNot(Date date, Campus campus, Long id);
	
	List<SessionEpreuve>  findSessionEpreuveByContext(Context context);
	
	List<SessionEpreuve> findAllByDateExamenGreaterThanEqualAndDateExamenLessThanEqual(Date startDate, Date endDate);
	
	List<SessionEpreuve> findAllByDateExamen(Date date);
	
	Long countByDateExamenGreaterThanEqual(Date date);
	
	List<SessionEpreuve> findAllByDateExamenGreaterThan(Date date);
	
	List<SessionEpreuve> findAllByDateExamenLessThan(Date date);
	
	Page<SessionEpreuve> findAllByAnneeUniv(String anneeUniv, Pageable pageable);
	
	@Query(value = "select count(*) from session_epreuve where nom_session_epreuve=:nom", nativeQuery = true)
	Long countExistingNomSessionEpreuve(String nom);
	
	//STATS
	@Query(value = "select site, count(*) as count from session_epreuve, campus where "
			+ "session_epreuve.campus_id=campus.id and session_epreuve.context_id=:context "
			+ "and is_session_epreuve_closed = 't' group by site order by count desc;", nativeQuery = true)
	List<Object> countSessionEpreuveByCampus(Long context);
	
	@Query(value = "SELECT CAST(DATE_PART('year',date_examen) AS INTEGER) AS year, CAST(DATE_PART('month', date_examen) AS INTEGER) AS month, count(*) "
			+ "AS count FROM session_epreuve  WHERE context_id=:context AND is_session_epreuve_closed = 't' GROUP BY year, month", nativeQuery = true)
	List<Object> countSessionEpreuveByYearMonth(Long context);
	
	@Query(value = "select key, CASE WHEN is_session_epreuve_closed='t' THEN 'Fermée' ELSE 'Ouverte' END AS statut, count(*) as count "
			+ "from session_epreuve, context where session_epreuve.context_id=context.id group by key, statut order by key, statut, count", nativeQuery = true)
	List<Object> countAllSessionEpreuvesByContext();

}
