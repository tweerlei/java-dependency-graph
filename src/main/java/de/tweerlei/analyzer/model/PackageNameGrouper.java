/*
 * Copyright 2018 tweerlei Wruck + Buchmeier GbR - http://www.tweerlei.de/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tweerlei.analyzer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Group by package name
 * 
 * @author Robert Wruck
 */
public class PackageNameGrouper implements ClassDescriptorFilter
	{
	private final int level;

	/**
	 * Constructor
	 * @param level Number of subpackages
	 */
	public PackageNameGrouper(int level)
		{
		this.level = level;
		}

	public Map<String, ClassDescriptor> filter(Map<String, ClassDescriptor> classes)
		{
		final Map<String, ClassDescriptor> ret = new HashMap<String, ClassDescriptor>();
		
		for (ClassDescriptor cd : classes.values())
			{
			final String groupName = getGroupName(cd.getClassName());
			ret.put(cd.getClassName(), new ClassDescriptor(cd.getClassName(), cd.getDependencies(), cd.getInterfaces(), groupName));
			}

		return (ret);
		}
	
	private final String getGroupName(String className)
		{
		final int i = nthIndexOf(className,'/', level);
		if (i < 0)
			return (null);
		else
			return (className.substring(0, i));
		}

	private final int nthIndexOf(String s, char pattern, int n)
		{
		int i = -1;
		for (int x = 0; x < n; x++)
			{
			i = s.indexOf(pattern, i + 1);
			if (i < 0)
				{
				if (x == n - 1)
					return (s.length());
				else
					return (i);
				}
			}
		return (i);
		}
	}
