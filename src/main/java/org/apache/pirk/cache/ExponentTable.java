/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pirk.cache;

import java.math.BigInteger;
import java.util.Collection;

import org.apache.pirk.encryption.ModPowAbstraction;

public abstract class ExponentTable
{
  /**
   * Returns the cached value, or computes it if it was not in the cache.
   */
  public BigInteger getExp(BigInteger value, int power, BigInteger modulus) {
    return ModPowAbstraction.modPow(value, BigInteger.valueOf(power), modulus);
  }
  
  /**
   * Gives the cache a chance to populate with all expected ('values' raised to power {0..maxPower}) mod modulus 
   */
  public void populate(Collection<BigInteger> values, int maxPower, BigInteger modulus) {
    // Optional
  }
}
