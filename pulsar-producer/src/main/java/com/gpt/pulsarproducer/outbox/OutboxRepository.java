package com.gpt.pulsarproducer.outbox;


import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long>
{
    // Claim & mark IN_PROGRESS atomically, returning the claimed rows.
    @Query(
        value = """
            WITH cte AS (
              SELECT id
              FROM outbox_event
              WHERE status IN ('NEW','FAILED')
                AND (next_attempt_at IS NULL OR next_attempt_at <= now())
              ORDER BY id
              FOR UPDATE SKIP LOCKED
              LIMIT :batchSize
            )
            UPDATE outbox_event o
            SET status = 'IN_PROGRESS'
            FROM cte
            WHERE o.id = cte.id
            RETURNING o.*
            """, nativeQuery = true
    )
    List<OutboxEvent> claimAndMark(@Param("batchSize") int batchSize);

    // Bulk SENT
    @Modifying
    @Query(
        value = """
            UPDATE outbox_event
            SET status = 'SENT', sent_at = :sentAt
            WHERE id IN (:ids)
            """, nativeQuery = true
    )
    int markSentBulk(@Param("ids") List<Long> ids, @Param("sentAt") Instant sentAt);

    // Bulk FAILED with exp backoff + jitter calculated in SQL
    // attempts := attempts + 1; next_attempt_at := now() + seconds
    // seconds = min(300, 2^min(attempts,12)) + floor(random() * max(1, base/4))
    @Modifying
    @Query(
        value = """
            UPDATE outbox_event o
            SET attempts = o.attempts + 1,
                status   = 'FAILED',
                next_attempt_at = (
                  SELECT now()
                         + make_interval(secs => base + jitter)
                  FROM (
                    SELECT
                      LEAST(300, CAST(POW(2, LEAST(o.attempts + 1, 12)) AS bigint)) AS base,
                      FLOOR(random() * GREATEST(1, LEAST(300, CAST(POW(2, LEAST(o.attempts + 1, 12)) AS bigint)) / 4.0))::bigint AS jitter
                  ) s
                )
            WHERE o.id IN (:ids)
            """, nativeQuery = true
    )
    int markFailedBulk(@Param("ids") List<Long> ids);

    // Bulk DLQ
    @Modifying
    @Query(value = "UPDATE outbox_event SET status='DLQ' WHERE id IN (:ids)", nativeQuery = true)
    int markDlqBulk(@Param("ids") List<Long> ids);

    // --- Dashboard bits unchanged ---
    @Query(
        value = """
            SELECT topic, status, COUNT(*) AS cnt
            FROM outbox_event
            GROUP BY topic, status
            """, nativeQuery = true
    )
    List<Object[]> countsByTopicAndStatus();

    @Query(value = "SELECT COUNT(*) FROM outbox_event", nativeQuery = true)
    long totalEvents();

    @Query(value = "SELECT COUNT(*) FROM outbox_event WHERE status = 'FAILED'", nativeQuery = true)
    long totalFailed();

    @Query(value = "SELECT COUNT(*) FROM outbox_event WHERE status = 'SENT'", nativeQuery = true)
    long totalSent();
}
