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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.pirk.encryption.ModPowAbstraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores all populated values forever, and returns them if asked.
 *
 */
public class SimpleExponentCache extends ExponentTable
{
  private static final Logger logger = LoggerFactory.getLogger(SimpleExponentCache.class);

  private BigInteger cachedModulus;
  // element -> <power, element^power mod N^2>
  private Map<BigInteger,Map<Integer,BigInteger>> expTable = new ConcurrentHashMap<>();

  public SimpleExponentCache()
  {
    // Default constructor.
  }

  @Override
  public BigInteger getExp(BigInteger value, int power, BigInteger modulus)
  {
    BigInteger cachedValue = null;
    if (cachedModulus.equals(modulus))
    {
      Map<Integer,BigInteger> powerMap = expTable.get(value);
      cachedValue = (powerMap == null) ? null : powerMap.get(power);
    }

    if (logger.isDebugEnabled())
    {
      String strResult = (cachedValue == null) ? "null" : cachedValue.toString(2);
      logger.debug("Cached result for value {}, pow {}, modulus {}, result {}", value.toString(2), power, modulus.toString(2), strResult);
    }

    return (cachedValue == null) ? super.getExp(value, power, modulus) : cachedValue;
  }

  @Override
  public void populate(Collection<BigInteger> values, int maxPower, BigInteger modulus)
  {
    logger.info("Populating cache for {} values, maxPower {}, modulus {}", values.size(), maxPower, modulus.toString(2));

    cachedModulus = modulus;

    values.parallelStream().forEach(new Consumer<BigInteger>()
    {
      @Override
      public void accept(BigInteger element)
      {
        Map<Integer,BigInteger> powMap = new HashMap<>(maxPower);
        for (int i = 0; i <= maxPower; ++i)
        {
          BigInteger value = ModPowAbstraction.modPow(element, BigInteger.valueOf(i), modulus);
          powMap.put(i, value);
        }
        expTable.put(element, powMap);
      }
    });
  }
}
