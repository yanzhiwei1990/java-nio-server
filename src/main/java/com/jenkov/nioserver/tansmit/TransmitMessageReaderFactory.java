package com.jenkov.nioserver.tansmit;

import com.jenkov.nioserver.IMessageReader;
import com.jenkov.nioserver.IMessageReaderFactory;
import com.jenkov.nioserver.MessageBuffer;

/**
 * Created by jjenkov on 18-10-2015.
 */
public class TransmitMessageReaderFactory implements IMessageReaderFactory {

    public TransmitMessageReaderFactory() {
    }

    @Override
    public IMessageReader createMessageReader() {
        return new TransmitMessageReader();
    }
}
