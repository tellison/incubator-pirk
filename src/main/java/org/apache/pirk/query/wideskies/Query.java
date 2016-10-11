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
package org.apache.pirk.query.wideskies;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.apache.pirk.serialization.Storable;

import com.google.gson.annotations.Expose;

/**
 * Class to hold the PIR query vectors
 */
public class Query implements Storable
{
  public static final long querySerialVersionUID = 1L;

  // So that we can serialize the version number in gson.
  @Expose public final long queryVersion = querySerialVersionUID;

  @Expose private final QueryInfo queryInfo; // holds all query info

  @Expose private final SortedMap<Integer,BigInteger> queryElements; // query elements - ordered on insertion

  // File based lookup table for modular exponentiation
  // element hash -> filename containing it's <power, element^power mod N^2> modular exponentiations
  @Expose private Map<Integer,String> expFileBasedLookup = new HashMap<>();

  @Expose private final BigInteger N; // N=pq, RSA modulus for the Paillier encryption associated with the queryElements

  @Expose private final BigInteger NSquared;

  public Query(QueryInfo queryInfo, BigInteger N, SortedMap<Integer,BigInteger> queryElements)
  {
    this(queryInfo, N, N.pow(2), queryElements);
  }

  public Query(QueryInfo queryInfo, BigInteger N, BigInteger NSquared, SortedMap<Integer,BigInteger> queryElements)
  {
    this.queryInfo = queryInfo;
    this.N = N;
    this.NSquared = NSquared;
    this.queryElements = queryElements;
  }

  public QueryInfo getQueryInfo()
  {
    return queryInfo;
  }

  public SortedMap<Integer,BigInteger> getQueryElements()
  {
    return queryElements;
  }

  public BigInteger getQueryElement(int index)
  {
    return queryElements.get(index);
  }

  public BigInteger getN()
  {
    return N;
  }

  public BigInteger getNSquared()
  {
    return NSquared;
  }

  public Map<Integer,String> getExpFileBasedLookup()
  {
    return expFileBasedLookup;
  }

  public String getExpFile(int i)
  {
    return expFileBasedLookup.get(i);
  }

  public void setExpFileBasedLookup(Map<Integer,String> expInput)
  {
    expFileBasedLookup = expInput;
  }
}
