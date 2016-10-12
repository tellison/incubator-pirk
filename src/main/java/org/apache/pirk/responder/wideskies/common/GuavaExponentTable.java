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
package org.apache.pirk.responder.wideskies.common;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.apache.pirk.cache.ExponentTable;
import org.apache.pirk.encryption.ModPowAbstraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;;

public class GuavaExponentTable extends ExponentTable
{
  private static final Logger logger = LoggerFactory.getLogger(GuavaExponentTable.class);

  private static class Key
  {
    final BigInteger value;
    final int power;
    final BigInteger modulus;

    Key(BigInteger value, int power, BigInteger modulus)
    {
      this.value = value;
      this.power = power;
      this.modulus = modulus;
    }

    @Override
    public boolean equals(Object other)
    {
      if (other == null || !(other instanceof Key))
      {
        return false;
      }
      Key otherKey = (Key) other;
      return (power == otherKey.power) && (value.equals(otherKey.value)) && (modulus.equals(otherKey.modulus));
    }

    @Override
    public int hashCode()
    {
      return value.hashCode() | power | modulus.hashCode();
    }
  }

  // Cache: <<base,exponent,NSquared>, base^exponent mod N^2>
  private LoadingCache<Key,BigInteger> cache;

  public GuavaExponentTable()
  {
    cache = CacheBuilder.newBuilder().maximumSize(10000).build(new CacheLoader<Key,BigInteger>()
    {
      @Override
      public BigInteger load(Key info) throws Exception
      {
        logger.debug("cache miss");
        return ModPowAbstraction.modPow(info.value, BigInteger.valueOf(info.power), info.modulus);
      }
    });
  }

  @Override
  public BigInteger getExp(BigInteger value, int power, BigInteger modulus)
  {
    try
    {
      return cache.get(new Key(value, power, modulus));
    } catch (ExecutionException e)
    {
      // Problem computing the value using ModPowAbstration.
      throw new RuntimeException(e);
    }
  }

  @Override
  public void putExp(BigInteger value, int power, BigInteger modulus, BigInteger result)
  {
    cache.put(new Key(value, power, modulus), result);
  }
}
