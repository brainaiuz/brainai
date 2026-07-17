package com.edatasite.workforce.gwt.core.server.db.impl.fifo;

import com.edatasite.workforce.core.domain.fifo.EdsFifoManualReview;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoFailureManager;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoManualReviewManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("fifoManualReviewManager")
public class FifoManualReviewManagerImpl extends BaseManager<EdsFifoManualReview> implements FifoManualReviewManager {

    public FifoManualReviewManagerImpl() {
        super(EdsFifoManualReview.class);
    }
}
