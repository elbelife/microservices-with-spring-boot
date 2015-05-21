package microservices.activemq;

public class MicroservicesInfo {


	private Long numberOfPendingMessage;
	private Long numberOfConsumers;
	private Long MessagesEngueued;
	private Long messagesDequeued;


	public MicroservicesInfo() {
		
	}
	
	public Long getNumberOfPendingMessage() {
		return numberOfPendingMessage;
	}

	public void setNumberOfPendingMessage(Long numberOfPendingMessage) {
		this.numberOfPendingMessage = numberOfPendingMessage;
	}

	public Long getNumberOfConsumers() {
		return numberOfConsumers;
	}

	public void setNumberOfConsumers(Long numberOfConsumers) {
		this.numberOfConsumers = numberOfConsumers;
	}

	public Long getMessagesEngueued() {
		return MessagesEngueued;
	}

	public void setMessagesEnqueued(Long messagesEngueued) {
		MessagesEngueued = messagesEngueued;
	}

	public Long getMessagesDequeued() {
		return messagesDequeued;
	}

	public void setMessagesDequeued(Long messagesDequeued) {
		this.messagesDequeued = messagesDequeued;
	}

}
