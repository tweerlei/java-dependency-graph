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
package de.tweerlei.analyzer.visitors;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;

/**
 * Collect dependencies
 * 
 * @author Robert Wruck
 */
class DependencyCollector
	{
	private final Set<String> deps;
	
	/**
	 * Constructor
	 */
	public DependencyCollector()
		{
		this.deps = new HashSet<String>();
		}
	
	/**
	 * Get the dependencies
	 * @return Set of class names
	 */
	public Set<String> getDependencies()
		{
		return (deps);
		}
	
	/**
	 * Add a simple class name
	 * @param name Class name
	 */
	public void addClass(String name)
		{
		deps.add(name);
		}
	
	/**
	 * Add a type dependency
	 * @param type Type
	 */
	public void addType(Type type)
		{
		if (type.getSort() == Type.ARRAY)
			addType(type.getElementType());
		else if (type.getSort() == Type.OBJECT)
			deps.add(type.getInternalName());
		else if (type.getSort() == Type.METHOD)
			{
			addType(type.getReturnType());
			for (Type a : type.getArgumentTypes())
				addType(a);
			}
		}
	
	/**
	 * Add a type dependency, given as type descriptor
	 * @param desc Type descriptor
	 */
	public void addType(String desc)
		{
		addType(Type.getType(desc));
		}
	
	/**
	 * Add a type dependency, given as type signature
	 * @param signature Type or method signature
	 */
	public void addClassOrMethod(String signature)
		{
		if (signature != null)
			{
			final SignatureProcessor proc = new SignatureProcessor(this);
			final SignatureReader r = new SignatureReader(signature);
			r.accept(proc);
			}
		}
	
	/**
	 * Add a type dependency, given as type signature
	 * @param signature Field signature
	 */
	public void addField(String signature)
		{
		if (signature != null)
			{
			final SignatureProcessor proc = new SignatureProcessor(this);
			final SignatureReader r = new SignatureReader(signature);
			r.acceptType(proc);
			}
		}
	}
