package bookkeep.models.states;

import java.time.Duration;
import java.time.Instant;

import bookkeep.enums.EventType;
import bookkeep.models.BookEvent;
import bookkeep.models.OwnedBook;

public class FinishedState extends ReadingState {

	public FinishedState(OwnedBook book) {
		super(book);
	}

	@Override
	public void startReading() {
		throw new UnsupportedOperationException("ReReading not supported yet");
	}

	@Override
	public void stopReading() {
		throw new UnsupportedOperationException("Cannot stop a book in FinishedState");
	}

	@Override
	public String getStateName() {
		return "FinishedState";
	}

	@Override
	public void handleComment(String comment) {
		// A comment in FinishedState becomes an afterthought
		BookEvent afterThoughtEvent = new BookEvent(EventType.AFTERTHOUGHT, comment, book.getPageNumber());
		book.getHistory().addEvent(afterThoughtEvent);
	}

	@Override
	public void handleQuote(String quote, int quotePageNumber) {
		BookEvent quoteEvent = new BookEvent(EventType.QUOTE, quote, quotePageNumber);
		book.getHistory().addEvent(quoteEvent);
	}

	@Override
	public void handleReview(String reviewText, int rating) {
		if (book.getHistory().hasReview()) {
			// Here logic for controlling review-overrides can be implemented
			// At this point all i do is signal that the review has changed
			reviewText = "_OVERRIDE_" + reviewText + "_OVERRIDE_";
		}

		BookEvent reviewEvent = new BookEvent(EventType.REVIEW, reviewText, rating);
		book.getHistory().setReview(reviewEvent);
	}

	@Override
	public Duration handleReadingDuration() {
		// Here we can assume that the BookHistory should have a startedReadingEvent and
		// a finishedReadingEvent
		Instant timeOfStartedReading = book.getHistory().getStartedReading().getTimestamp();
		Instant timeOfFinishedReading = book.getHistory().getFinishedReading().getTimestamp();

		return Duration.between(timeOfStartedReading, timeOfFinishedReading);

	}

	@Override
	public void handleIncrementPageNumber(int increment) {
		throw new UnsupportedOperationException("Cannot increment pagenumber of a finished book");
	}
}
