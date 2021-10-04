package io.spring.batch.handleerror;

import java.io.FileNotFoundException;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ParseException;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link ParseException}에 대하여 최대 4번까지 Skip 허용
 */
@Slf4j
public class FileVerificationSkipper implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (t instanceof FileNotFoundException) {
            return false;
        }
        logger.info("shouldSkip is called. throwable: {} / skipCount: {}", t.getClass().getSimpleName(), skipCount);
        return t instanceof ParseException && skipCount <= 2;
    }
}
