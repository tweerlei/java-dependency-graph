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
package de.tweerlei.analyzer.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tweerlei.analyzer.model.ClassDescriptor;

/**
 * Writer for a dot graph
 * 
 * @author Robert Wruck
 */
public class DotWriter
	{
	private static final String FONT_NAME = "Arial";
	
	/**
	 * Constructor
	 */
	public DotWriter()
		{
		}
	
	/**
	 * Write the graph
	 * @param classes Class descriptors
	 * @param os Target stream
	 * @throws IOException on error
	 */
	public void write(Map<String, ClassDescriptor> classes, OutputStream os) throws IOException
		{
		final Writer w = new OutputStreamWriter(os, "ISO-8859-1");
		try	{
			output(classes, w);
			}
		finally
			{
			w.close();
			}
		}
	
	private void output(Map<String, ClassDescriptor> classes, Writer writer) throws IOException
		{
		writer.write("digraph G {\n");
		writer.write("\tnode [shape=plaintext];\n");
		
		for (Map.Entry<String, List<ClassDescriptor>> ent : groupClasses(classes).entrySet())
			{
			if (ent.getKey() != null)
				{
				writer.write("\tsubgraph cluster");
				writer.write(ent.getKey().replace('/', '_'));
				writer.write(" {\n");
				writer.write("\tlabel=\"");
				writer.write(formatClassName(ent.getKey()));
				writer.write("\"\n");
				writer.write("\tfontname=\"");
				writer.write(FONT_NAME);
				writer.write("\"\n");
				}
			
			for (ClassDescriptor cd : ent.getValue())
				{
				final String name = formatClassName(cd.getClassName());

				writer.write("\t\"");
				writer.write(name);
				writer.write("\" [label=<<table port=\"entity\" border=\"0\" cellspacing=\"0\" cellborder=\"1\">\n");
				writer.write("\t\t<tr><td><font face=\"");
				writer.write(FONT_NAME);
				writer.write(" Bold\" point-size=\"12\">");
				writer.write(name);
				writer.write("</font></td></tr>\n");
				writer.write("\t\t</table>>];\n");
				}
			
			if (ent.getKey() != null)
				{
				writer.write("\t}\n");
				}
			}
		
		for (ClassDescriptor cd : classes.values())
			{
			final String name = formatClassName(cd.getClassName());
			
			for (String dep : cd.getInterfaces())
				{
				final ClassDescriptor d = classes.get(dep);
				if (d != null)
					{
					final String dname = formatClassName(d.getClassName());
					
					writer.write("\t\"");
					writer.write(name);
					writer.write("\":entity -> \"");
					writer.write(dname);
					writer.write("\":entity [dir=forward,style=dashed];\n");
					}
				}
			
			for (String dep : cd.getDependencies())
				{
				final ClassDescriptor d = classes.get(dep);
				if (d != null)
					{
					final String dname = formatClassName(d.getClassName());
					
					writer.write("\t\"");
					writer.write(name);
					writer.write("\":entity -> \"");
					writer.write(dname);
					writer.write("\":entity [dir=forward];\n");
					}
				}
			}
		
		writer.write("}\n");
		}
	
	private Map<String, List<ClassDescriptor>> groupClasses(Map<String, ClassDescriptor> classes)
		{
		final Map<String, List<ClassDescriptor>> ret = new HashMap<String, List<ClassDescriptor>>();

		for (ClassDescriptor cd : classes.values())
			{
			List<ClassDescriptor> l = ret.get(cd.getGroupName());
			if (l == null)
				{
				l = new ArrayList<ClassDescriptor>();
				ret.put(cd.getGroupName(), l);
				}
			l.add(cd);
			}

		return (ret);
		}
	
	private String formatClassName(String n)
		{
		return (n.replace('/', '.'));
		}
	}
