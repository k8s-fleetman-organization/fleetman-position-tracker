package com.virtualpairprogrammers.tracker.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.virtualpairprogrammers.tracker.domain.VehiclePosition;

public interface PositionRepository extends MongoRepository<VehiclePosition, String> {
	List<VehiclePosition> findByNameAndTimestampAfter(String name, Date timestamp);
	Collection<VehiclePosition> findByTimestampAfter(Date since);

	@Aggregation(pipeline = { "{ '$group': { '_id' : '$name' } }" })
        
       List<String> findDistinctNames();

        @Aggregation(pipeline = {
          "{ '$sort': { 'name': 1, 'timestamp': -1 } }",
          "{ '$group': { '_id': '$name', 'latest': { '$first': '$$ROOT' } } }",
          "{ '$replaceRoot': { 'newRoot': '$latest' } }"
})
       List<VehiclePosition> findLatestPositionsForAllVehicles();

}
