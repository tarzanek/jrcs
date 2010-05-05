/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: AddDelta.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff.delta;

import java.util.List;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.PatchFailedException;
import org.suigeneris.jrcs.diff.RevisionVisitor;

/**
 * Holds an add-delta between to revisions of a text.
 * 
 * @version $Id: AddDelta.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Delta
 * @see Diff
 * @see Chunk
 */
public class AddDelta extends Delta
{

    AddDelta()
    {
        super();
    }

    public AddDelta(int origpos, Chunk rev)
    {
        init(new Chunk(origpos, 0), rev);
    }

    public void verify(List target) throws PatchFailedException
    {
        if (original.first() > target.size())
        {
            throw new PatchFailedException("original.first() > target.size()");
        }
    }

    public void applyTo(List target)
    {
        revised.applyAdd(original.first(), target);
    }

    public void toString(StringBuffer s)
    {
        s.append(original.anchor());
        s.append("a");
        s.append(revised.rangeString());
        s.append(Diff.NL);
        revised.toString(s, "> ", Diff.NL);
    }

    public void toRCSString(StringBuffer s, String EOL)
    {
        s.append("a");
        s.append(original.anchor());
        s.append(" ");
        s.append(revised.size());
        s.append(EOL);
        revised.toString(s, "", EOL);
    }

    public void Accept(RevisionVisitor visitor)
    {
        visitor.visit(this);
    }

    public void accept(RevisionVisitor visitor)
    {
        visitor.visit(this);
    }
}
