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

import java.util.List;

/**
 * A repository that provides access to {@link Customer} entities.
 */
public interface CustomerRepository {

  Customer create(Customer customer);

  void delete(Customer customer);

  Customer find(Long id) throws CustomerNotFoundException;

  List<Customer> findAll();

  Customer update(Customer customer);
}