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
package org.apache.pirk.responder.wideskies.storm;

import org.apache.pirk.responder.spi.ResponderPlugin;
import org.apache.pirk.utils.PIRException;

public class StormResponder implements ResponderPlugin
{

  public StormResponder()
  {
    // Default constructor
  }

  @Override
  public String getPlatformName()
  {
    return "Storm";
  }

  @Override
  public void run() throws PIRException
  {
    try
    {
      PirkTopology.runPirkTopology();
    } catch (Exception e)
    {
      throw new PIRException(e);
    }
  }
}
