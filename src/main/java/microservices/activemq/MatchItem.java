package microservices.activemq;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MatchItem {
	
	private String id;
	
	private String name;
	
	private Date start;
	
	private String sportId;

	private static AtomicReference<Long> currentTime = 
            new AtomicReference<Long>(System.currentTimeMillis());
	
	public MatchItem() {
		id = currentTime.accumulateAndGet(System.currentTimeMillis(), 
                (prev, next) -> next > prev ? next : prev + 1).toString();
		
		name = "tennis";
		start = new Date();
		sportId = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getStart() {
		return start;
	}

	public String getSportId() {
		return sportId;
	}

	public void setId(String pId) {
		id = pId;
	}

	public void setName(String pName) {
		name = pName;
	}

	public void setStart(Date pStart) {
		start = pStart;
	}

	public void setSportId(String pSportId) {
		sportId = pSportId;
	}

}
