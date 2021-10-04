package io.spring.batch.handleerror;

import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.file.FlatFileParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerItemListener {

    @OnReadError
    public void onReadError(Exception e) {
        if (e instanceof FlatFileParseException) {
            final FlatFileParseException ffpe = (FlatFileParseException) e;
            final String message = "An error occurred while processing the "
                                   + ffpe.getLineNumber()
                                   + " line of the file. Below was the faulty input.\n"
                                   + ffpe.getInput()
                                   + "\n";
            logger.error(message);
            return;
        }
        logger.error("An error has occurred", e);
    }
}
