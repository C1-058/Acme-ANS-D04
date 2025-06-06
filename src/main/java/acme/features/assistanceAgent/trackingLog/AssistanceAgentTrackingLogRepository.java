
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.tracking_log.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.assistanceAgent.id = :assistanceAgentId")
	Collection<TrackingLog> findAllTrackingLogs(int assistanceAgentId);

	@Query("Select c from Claim c where c.assistanceAgent.id=:agentId")
	List<Claim> findClaimsByAssistanceAgent(int agentId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.lastUpdateMoment desc")
	Optional<List<TrackingLog>> findOrderTrackingLog(Integer claimId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId and t.draftMode = false order by t.resolutionPercentage desc")
	Optional<List<TrackingLog>> findOrderTrackingLogPublished(Integer claimId);

	@Query("select b from Claim b where b.id=:claimId")
	Claim getClaimById(int claimId);

	@Query("select al.claim from TrackingLog al where al.id = :id")
	Claim findClaimByTrackingLogId(int id);

	@Query("select al from TrackingLog al where al.claim.id = :masterId")
	Collection<TrackingLog> findTrackingLogsByMasterId(int masterId);

}
