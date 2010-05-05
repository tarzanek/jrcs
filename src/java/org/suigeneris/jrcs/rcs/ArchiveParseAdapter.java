/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ArchiveParseAdapter.java,v 1.1 2006/06/08 15:07:10 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import org.suigeneris.jrcs.rcs.impl.BranchNode;
import org.suigeneris.jrcs.rcs.impl.Node;
import org.suigeneris.jrcs.rcs.impl.Phrases;

public class ArchiveParseAdapter
{

    public final Archive archive;

    public ArchiveParseAdapter(Archive archive)
    {
        this.archive = archive;
    }
    
    public Phrases getPhrases()
    {
        return this.archive.getPhrases();
    }
    
    public void setHead(Version v)
    {
        this.archive.setHead(v);
    }
    
    public Node newNode(Version v)
    {
        return this.archive.newNode(v);
    }
    
    public Node getNode(Version v)
    {
        return this.archive.getNode(v);
    }
    
    public BranchNode newBranchNode(Version v)
    {
        return this.archive.newBranchNode(v);
    }

}
