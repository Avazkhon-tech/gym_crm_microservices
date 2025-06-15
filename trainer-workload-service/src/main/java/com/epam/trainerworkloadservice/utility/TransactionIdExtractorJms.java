package com.epam.trainerworkloadservice.utility;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TransactionIdExtractorJms {

    public void extractFromMessage(Message message) {
        String txId = null;
        try {
            txId = message.getStringProperty("TransactionId");
            log.debug("Transaction id extracted from JMS message: {}", txId);
        } catch (JMSException e) {
            log.error("Error while extracting transaction id from JMS message");
        }
        if (txId != null) {
            TransactionId.addTransactionId(txId);
        }
    }
}
