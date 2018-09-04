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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;

import de.tweerlei.analyzer.visitors.ClassProcessor;

/**
 * Read .class files from a given base directory or JAR file
 * 
 * @author Robert Wruck
 */
public class RecursiveClassReader
	{
	private final boolean verbose;
	private final Map<String, ClassDescriptor> classes;
	
	/**
	 * Constructor
	 * @param verbose Print processed file names on System.err
	 */
	public RecursiveClassReader(boolean verbose)
		{
		this.verbose = verbose;
		this.classes = new HashMap<String, ClassDescriptor>();
		}
	
	/**
	 * Get the classes read
	 * @return Map: Class name -> ClassDescriptor
	 */
	public Map<String, ClassDescriptor> getClassDescriptors()
		{
		return (classes);
		}
	
	/**
	 * Read .class files from a given base directory or JAR file
	 * @param f File or directory to read
	 * @return this
	 * @throws IOException on error
	 */
	public RecursiveClassReader readClasses(File f) throws IOException
		{
		if (f.isFile())
			{
			if (f.getName().endsWith(".jar"))
				return (readJar(f));
			else if (f.getName().endsWith(".class"))
				return (readClass(f));
			}
		else if (f.isDirectory())
			{
			for (File ff : f.listFiles())
				readClasses(ff);
			}
		
		return (this);
		}
	
	/**
	 * Read .class files from a given JAR file
	 * @param f JAR file to read
	 * @return this
	 * @throws IOException on error
	 */
	public RecursiveClassReader readJar(File f) throws IOException
		{
		if (verbose)
			System.err.println(f.getPath());
		
		final JarFile jf = new JarFile(f);
		try	{
			for (Enumeration<JarEntry> en = jf.entries(); en.hasMoreElements(); )
				{
				final JarEntry e = en.nextElement();
				if (!e.isDirectory())
					readClass(jf.getInputStream(e));
				}
			}
		finally
			{
			jf.close();
			}
		
		return (this);
		}
	
	/**
	 * Read a single .class file
	 * @param f Class file
	 * @return this
	 * @throws IOException on error
	 */
	public RecursiveClassReader readClass(File f) throws IOException
		{
		if (verbose)
			System.err.println(f.getPath());
		
		final InputStream is = new FileInputStream(f);
		try	{
			return (readClass(is));
			}
		finally
			{
			is.close();
			}
		}
	
	/**
	 * Read a single class from an InputStream
	 * @param is InputStream to read
	 * @return this
	 * @throws IOException on error
	 */
	public RecursiveClassReader readClass(InputStream is) throws IOException
		{
		final ClassReader cr = new ClassReader(is);
		final ClassProcessor proc = new ClassProcessor();
		cr.accept(proc, 0);
		
		classes.put(proc.getClassName(), new ClassDescriptor(proc.getClassName(), proc.getDependencies(), proc.getInterfaces()));
		
		return (this);
		}
	}
