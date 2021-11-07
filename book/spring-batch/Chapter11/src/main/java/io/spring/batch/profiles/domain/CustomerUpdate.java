package io.spring.batch.profiles.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerUpdate {

    protected final long customerId;
}
