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
import java.util.HashSet;
import java.util.Map;

/**
 * Aggregate classes to packages
 * 
 * @author Robert Wruck
 */
public class PackageNameAggregator implements ClassDescriptorFilter
	{
	private final int level;
	
	/**
	 * Constructor
	 * @param level Number of subpackages
	 */
	public PackageNameAggregator(int level)
		{
		this.level = level;
		}
	
	public Map<String, ClassDescriptor> filter(Map<String, ClassDescriptor> classes)
		{
		final Map<String, ClassDescriptor> ret = new HashMap<String, ClassDescriptor>();
		
		for (ClassDescriptor cd : classes.values())
			{
			final String packageName = getGroupedPackageName(cd.getClassName());
			ClassDescriptor pd = ret.get(packageName);
			if (pd == null)
				{
				pd = new ClassDescriptor(packageName, new HashSet<String>(), new HashSet<String>(), null);
				ret.put(packageName, pd);
				}
			for (String s : cd.getDependencies())
				pd.getDependencies().add(getGroupedPackageName(s));
			for (String s : cd.getInterfaces())
				pd.getInterfaces().add(getGroupedPackageName(s));
			
			pd.cleanup();
			}
		
		return (ret);
		}
	
	private String getGroupedPackageName(String className)
		{
		final String pn = getPackageName(className);
		
		final int i = nthIndexOf(pn,'/', level);
		if (i < 0)
			return (pn);
		else
			return (pn.substring(0, i));
		}
	
	private final String getPackageName(String className)
		{
		final int i = className.lastIndexOf('/');
		if (i < 0)
			return (className);
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
