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
package de.tweerlei.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tweerlei.analyzer.model.ClassDescriptor;
import de.tweerlei.analyzer.model.ClassDescriptorFilter;
import de.tweerlei.analyzer.model.IdentityFilter;
import de.tweerlei.analyzer.model.PackageNameAggregator;
import de.tweerlei.analyzer.model.RecursiveClassReader;
import de.tweerlei.analyzer.model.SimpleNameAggregator;
import de.tweerlei.analyzer.output.DotWriter;

/**
 * Entry point
 * 
 * @author Robert Wruck
 */
public class Analyze
	{
	/**
	 * Entry point
	 * @param args Arguments
	 * @throws Exception on error
	 */
	public static void main(String[] args) throws Exception
		{
		System.exit(new Analyze().run(args));
		}
	
	private Analyze()
		{
		}
	
	private void usage()
		{
		System.err.println(
				"usage: " + getClass().getName() + " [-s] [-p] [-v] {file.class | file.jar | dir} ...\n\n"
				+" -p  Calculate package dependencies\n"
				+" -s  Print only simple class names\n"
				+" -v  Print processed file names\n\n"
				);
		}
	
	/**
	 * Entry point
	 * @param args Arguments
	 * @return Return code
	 * @throws Exception on error
	 */
	private int run(String[] args) throws Exception
		{
		boolean verbose = false;
		
		ClassDescriptorFilter filter = new IdentityFilter();
		
		final List<File> filesToRead = new ArrayList<File>();
		
		for (String s : args)
			{
			if (s.startsWith("-"))
				{
				if (s.equals("-s"))
					filter = new SimpleNameAggregator();
				else if (s.equals("-p"))
					filter = new PackageNameAggregator();
				else
					usage();
				}
			else
				filesToRead.add(new File(s));
			}
		
		final RecursiveClassReader r = new RecursiveClassReader(verbose);
		for (File f : filesToRead)
			r.readClasses(f);
		
		final Map<String, ClassDescriptor> classes = filter.filter(r.getClassDescriptors());
		
		new DotWriter().write(classes, System.out);
		
		return (0);
		}
	}
