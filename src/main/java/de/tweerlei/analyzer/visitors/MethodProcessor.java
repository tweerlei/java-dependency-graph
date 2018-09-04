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
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Process class methods
 * 
 * @author Robert Wruck
 */
public class MethodProcessor extends MethodVisitor
	{
	private final DependencyCollector coll;
	private final AnnotationProcessor proc;
	
	/**
	 * Constructor
	 * @param coll DependencyCollector
	 * @param proc AnnotationProcessor
	 */
	public MethodProcessor(DependencyCollector coll, AnnotationProcessor proc)
		{
		super(Opcodes.ASM6);
		this.coll = coll;
		this.proc = proc;
		}

	@Override
	public AnnotationVisitor visitAnnotation(String name, boolean visible)
		{
		coll.addClass(name);
		return (proc);
		}

	@Override
	public AnnotationVisitor visitAnnotationDefault()
		{
		return (proc);
		}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc)
		{
		coll.addClass(owner);
		coll.addType(desc);
		}

	@Override
	public void visitLdcInsn(Object cst)
		{
		if (cst instanceof Type)
			coll.addType((Type) cst);
		}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
		{
		coll.addType(desc);
		coll.addField(signature);
		}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc)
		{
		coll.addClass(owner);
		coll.addType(desc);
		}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims)
		{
		coll.addType(desc);
		}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter, String name, boolean visible)
		{
		return (proc);
		}

	@Override
	public void visitTypeInsn(int opcode, String type)
		{
		coll.addClass(type);
		}
	}
