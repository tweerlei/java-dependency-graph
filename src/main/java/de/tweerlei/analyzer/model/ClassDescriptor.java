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

import java.util.Set;

/**
 * Class descriptor
 * 
 * @author Robert Wruck
 */
public final class ClassDescriptor
	{
	private final String className;
	private final String groupName;
	private final Set<String> deps;
	private final Set<String> ifs;
	
	/**
	 * Constructor
	 * @param className Class name
	 * @param deps Class names references by this class
	 * @param ifs Superclass and interfaces
	 * @param groupName Group name
	 */
	public ClassDescriptor(String className, Set<String> deps, Set<String> ifs, String groupName)
		{
		this.className = className;
		this.deps = deps;
		this.ifs = ifs;
		this.groupName = groupName;
		
		cleanup();
		}
	
	/**
	 * Clean up self references
	 */
	public void cleanup()
		{
		this.deps.remove(className);
		this.ifs.remove(className);
		this.deps.removeAll(ifs);
		}
	
	/**
	 * Get the class name
	 * @return Class name
	 */
	public String getClassName()
		{
		return className;
		}
	
	/**
	 * Get the group name
	 * @return Group name
	 */
	public String getGroupName()
		{
		return groupName;
		}

	/**
	 * Get the class names references by this class
	 * @return Class names
	 */
	public Set<String> getDependencies()
		{
		return deps;
		}
	
	/**
	 * Get the class names implemented by this class
	 * @return Class names
	 */
	public Set<String> getInterfaces()
		{
		return ifs;
		}
	}
