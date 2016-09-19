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
package org.apache.pirk.responder.wideskies.standalone;

import java.io.IOException;

import org.apache.pirk.query.wideskies.Query;
import org.apache.pirk.responder.spi.ResponderPlugin;
import org.apache.pirk.serialization.LocalFileSystemStore;
import org.apache.pirk.utils.PIRException;
import org.apache.pirk.utils.SystemConfiguration;

public class StandaloneResponder implements ResponderPlugin
{
  public StandaloneResponder()
  {
    // Default constructor
  }

  @Override
  public String getPlatformName()
  {
    return "Standalone";
  }

  @Override
  public void run() throws PIRException
  {
    String queryInput = SystemConfiguration.getProperty("pir.queryInput");
    Query query;
    try
    {
      query = new LocalFileSystemStore().recall(queryInput, Query.class);
      Responder pirResponder = new Responder(query);
      pirResponder.computeStandaloneResponse();
    } catch (IOException e)
    {
      throw new PIRException(e);
    }
  }
}
