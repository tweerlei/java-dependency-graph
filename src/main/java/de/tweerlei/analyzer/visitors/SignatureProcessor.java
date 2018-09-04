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

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Process type signatures
 * 
 * @author Robert Wruck
 */
public class SignatureProcessor extends SignatureVisitor
	{
	private final DependencyCollector coll;
	
	/**
	 * Constructor
	 * @param coll DependencyCollector
	 */
	public SignatureProcessor(DependencyCollector coll)
		{
		super(Opcodes.ASM6);
		this.coll = coll;
		}
	
	@Override
	public SignatureVisitor visitArrayType()
		{
		return (this);
		}

	@Override
	public SignatureVisitor visitClassBound()
		{
		return (this);
		}

	@Override
	public void visitClassType(String name)
		{
		coll.addClass(name);
		}

	@Override
	public SignatureVisitor visitExceptionType()
		{
		return (this);
		}

	@Override
	public SignatureVisitor visitInterface()
		{
		return (this);
		}

	@Override
	public SignatureVisitor visitInterfaceBound()
		{
		return (this);
		}

	@Override
	public SignatureVisitor visitParameterType()
		{
		return (this);
		}

	@Override
	public SignatureVisitor visitReturnType()
		{
		return (this);
		}

	@Override
	public SignatureVisitor visitSuperclass()
		{
		return (this);
		}
	}
