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

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Process a class
 * 
 * @author Robert Wruck
 */
public class ClassProcessor extends ClassVisitor
	{
	private String className;
	private final Set<String> ifs;
	private final DependencyCollector coll;
	private final AnnotationProcessor ap;
	private final FieldProcessor fp;
	private final MethodProcessor mp;
	
	/**
	 * Constructor
	 */
	public ClassProcessor()
		{
		super(Opcodes.ASM6);
		ifs = new HashSet<String>();
		coll = new DependencyCollector();
		ap = new AnnotationProcessor(coll);
		fp = new FieldProcessor(coll, ap);
		mp = new MethodProcessor(coll, ap);
		}
	
	/**
	 * Get the class name
	 * @return Class name
	 */
	public String getClassName()
		{
		return (className);
		}
	
	/**
	 * Get the dependencies
	 * @return Set of class names
	 */
	public Set<String> getInterfaces()
		{
		return (ifs);
		}
	
	/**
	 * Get all class names that are referenced by this class
	 * @return Class names
	 */
	public Set<String> getDependencies()
		{
		return (coll.getDependencies());
		}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
		{
		className = name;
		coll.addClassOrMethod(signature);
		ifs.add(superName);
		for (int i = 0; i < interfaces.length; i++)
			ifs.add(interfaces[i]);
		}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, boolean visible)
		{
		coll.addClass(name);
		return (ap);
		}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
		{
		coll.addType(desc);
		coll.addField(signature);
		return (fp);
		}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
		{
		coll.addType(desc);
		coll.addClassOrMethod(signature);
		return (mp);
		}
	}
