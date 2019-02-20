/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.playground.api.rest.security.domain.customer;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Exception to be thrown when a customer is not found.
 */
public class CustomerNotFoundException extends Exception {

  private final Long customerId;

  /**
   * @param customerId the given customerId.
   * @throws NullPointerException
   */
  public CustomerNotFoundException(final Long customerId) {
    super();
    this.customerId = notNull(customerId, "customerId must not be null");
  }

  /**
   * @param customerId the given customerId.
   * @param cause        the cause
   * @throws NullPointerException
   */
  public CustomerNotFoundException(final Long customerId, final Throwable cause) {
    super(cause);
    this.customerId = notNull(customerId, "customerId must not be null");
  }

  @Override
  public String getMessage() {
    return String.format("Customer %s was not found", customerId);
  }

  public Long getCustomerId() {
    return customerId;
  }
}
