/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.apache.woden.internal;

import java.util.Hashtable;

import org.apache.woden.WSDLReader;

/**
 * This class contains all supported Woden reader features and stores
 * their values for individual parser configurations.
 * 
 * TODO: determine the required features (e.g. org.apache.woden.verbose) and
 * create an ID for each value.
 */
public class ReaderFeatures 
{
	
	/**
	 * This hashtable contains the values for the features.
	 */
	protected Hashtable values = new Hashtable();
	
	private Boolean on = new Boolean(true);
	private Boolean off = new Boolean(false);
	
	public ReaderFeatures()
	{
	  values.put(WSDLReader.FEATURE_VALIDATION, off);
	}
	
	/**
	 * Get the value for the given feature. 
	 * @param featureId The ID of the feature for which the value is requested.
	 * @return true if the feature is enabled, false otherwise.
	 * @throws IllegalArgumentException if the feature is not supported.
	 */
	public boolean getValue(String featureId) throws IllegalArgumentException
	{
	  Boolean value = (Boolean)values.get(featureId);
	  if(value == null)
	  {
		throw new IllegalArgumentException("The feature " + featureId + " is not supported.");
	  }
	  return value.booleanValue();
	}
	
	/**
	 * Set the value of the given feature
	 * @param featureId The ID of the feature to set.
	 * @param value The value to set for the feature.
	 * @throws IllegalArgumentException if the feature is not supported.
	 */
	public void setValue(String featureId, boolean value) throws IllegalArgumentException
	{
		// Check if the feature is supported.
		if(!values.containsKey(featureId))
		{
			throw new IllegalArgumentException("The feature " + featureId + " is not supported.");
		}
		if(value)
		{
			values.put(featureId, on);
		}
		else
		{
			values.put(featureId, off);
		}
	}
}
