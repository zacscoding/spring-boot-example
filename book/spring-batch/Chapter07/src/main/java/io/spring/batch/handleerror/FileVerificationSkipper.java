package io.spring.batch.handleerror;

import java.io.FileNotFoundException;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ParseException;

/**
 * {@link ParseException}에 대하여 최대 10번까지 Skip 허용
 */
public class FileVerificationSkipper implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (t instanceof FileNotFoundException) {
            return false;
        }
        return t instanceof ParseException && skipCount <= 10;
    }
}
