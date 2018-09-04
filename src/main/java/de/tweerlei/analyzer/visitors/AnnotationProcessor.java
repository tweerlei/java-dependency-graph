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

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Process an annotation
 * 
 * @author Robert Wruck
 */
class AnnotationProcessor extends AnnotationVisitor
	{
	private final DependencyCollector coll;
	
	/**
	 * Constructor
	 * @param coll DependencyCollector
	 */
	public AnnotationProcessor(DependencyCollector coll)
		{
		super(Opcodes.ASM6);
		this.coll = coll;
		}
	
	@Override
	public void visit(String name, Object value)
		{
		if (value instanceof Type)
			coll.addType((Type) value);
		
		// else it's a simple type...
		}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc)
		{
		coll.addClass(name);
		return (this);
		}
	
	@Override
	public AnnotationVisitor visitArray(String name)
		{
		return (this);
		}
	
	@Override
	public void visitEnum(String name, String desc, String value)
		{
		coll.addType(desc);
		}
	}
