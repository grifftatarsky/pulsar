package com.gpt.pulsarconsumer.service;


import com.gpt.pulsarconsumer.domain.StationType;
import com.gpt.pulsarconsumer.repo.StationRepository;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class MetricsService
{

    private final EnumMap<StationType, AtomicLong> counts = new EnumMap<>(StationType.class);

    public MetricsService()
    {
        for (StationType t : StationType.values())
        {
            counts.put(t, new AtomicLong(0L));
        }
    }

    public void setCountsFromDb(StationRepository repo)
    {
        for (StationType t : StationType.values())
        {
            long c = repo.countByType(t);
            counts.get(t).set(c);
        }
    }

    public Map<StationType, Long> snapshot()
    {
        EnumMap<StationType, Long> snap = new EnumMap<>(StationType.class);
        for (Map.Entry<StationType, AtomicLong> e : counts.entrySet())
        {
            snap.put(e.getKey(), e.getValue().get());
        }
        return snap;
    }
}
