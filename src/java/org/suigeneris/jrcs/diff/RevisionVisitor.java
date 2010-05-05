/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: RevisionVisitor.java,v 1.3 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff;

import org.suigeneris.jrcs.diff.delta.AddDelta;
import org.suigeneris.jrcs.diff.delta.ChangeDelta;
import org.suigeneris.jrcs.diff.delta.DeleteDelta;

/**
 * Definition of a Visitor interface for {@link Revision Revisions} See "Design
 * Patterns" by the Gang of Four
 */
public interface RevisionVisitor
{
    public void visit(Revision revision);

    public void visit(DeleteDelta delta);

    public void visit(ChangeDelta delta);

    public void visit(AddDelta delta);
}