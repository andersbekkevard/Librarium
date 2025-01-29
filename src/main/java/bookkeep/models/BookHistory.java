package bookkeep.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import bookkeep.enums.EventType;

public class BookHistory {
	private final List<BookEvent> listOfEvents;
	private BookEvent startedReading;
	private BookEvent finishedReading;
	private BookEvent review;

	/**
	 * We have a staic comparator in order to sort the history if necessary
	 */
	private static final Comparator<BookEvent> chronologicalComparator = (e1, e2) -> {
		return e1.getTimestamp().compareTo(e2.getTimestamp());
	};

	/**
	 * Static predicates are used to modularize the gathering of subsets
	 */
	private static final Predicate<BookEvent> isComment = (e) -> (e.getType() == EventType.COMMENT);

	private static final Predicate<BookEvent> isAfterThought = (e) -> (e.getType() == EventType.AFTERTHOUGHT);

	private static final Predicate<BookEvent> isQuote = (e) -> (e.getType() == EventType.QUOTE);

	private static final List<EventType> typesThatGoInListOfEvents = Arrays.asList(EventType.QUOTE, EventType.COMMENT,
			EventType.AFTERTHOUGHT);

	public BookHistory() {
		this.listOfEvents = new ArrayList<>();
	}

	public List<BookEvent> getListOfEvents() {
		return listOfEvents;
	}

	public BookEvent getStartedReading() {
		return startedReading;
	}

	public BookEvent getFinishedReading() {
		return finishedReading;
	}

	public BookEvent getReview() {
		return review;
	}

	public void setReview(BookEvent review) {
		this.review = review;
	}

	public boolean hasReview() {
		return review != null;
	}

	// Abstract method for gathering subsets
	public List<BookEvent> gatherSubset(Predicate<BookEvent> predicate) {
		return listOfEvents.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}

	// region Concrete implementations
	public List<BookEvent> getComments() {
		return gatherSubset(isComment);

	}

	public List<BookEvent> getAfterThoughts() {
		return gatherSubset(isAfterThought);
	}

	public List<BookEvent> getQuotes() {
		return gatherSubset(isQuote);
	}
	// endregion

	public void addEvent(BookEvent event) {
		EventType type = event.getType();
		if (typesThatGoInListOfEvents.contains(type)) {
			this.listOfEvents.add(event);
		}

		/**
		 * Since the states only allow for one state transition, hence only one
		 * STARTED_READING and FINISHED_READING event each, i can simply hard code
		 * the logic in this way. This might cause issues down the road, or need
		 * refactoring, but for now it is worth it to avoid overabstraction
		 */
		else if (type == EventType.STARTED_READING) {
			startedReading = event;
		} else if (type == EventType.FINISHED_READING) {
			finishedReading = event;
		} else if (type == EventType.REVIEW) {
			throw new IllegalArgumentException("Reviews should not be added through addEvent Method");
		}
	}

	public void sort() {
		listOfEvents.sort(chronologicalComparator);
	}

	@Override
	public String toString() {
		return listOfEvents.toString();
	}
}
