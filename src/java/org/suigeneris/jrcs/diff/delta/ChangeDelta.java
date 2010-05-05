/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: ChangeDelta.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
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
 * Holds an change-delta between to revisions of a text.
 * 
 * @version $Id: ChangeDelta.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Delta
 * @see Diff
 * @see Chunk
 */
public class ChangeDelta extends Delta
{

    ChangeDelta()
    {
        super();
    }

    public ChangeDelta(Chunk orig, Chunk rev)
    {
        init(orig, rev);
    }

    public void verify(List target) throws PatchFailedException
    {
        if (!original.verify(target))
        {
            throw new PatchFailedException();
        }
        if (original.first() > target.size())
        {
            throw new PatchFailedException("original.first() > target.size()");
        }
    }

    public void applyTo(List target)
    {
        original.applyDelete(target);
        revised.applyAdd(original.first(), target);
    }

    public void toString(StringBuffer s)
    {
        original.rangeString(s);
        s.append("c");
        revised.rangeString(s);
        s.append(Diff.NL);
        original.toString(s, "< ", "\n");
        s.append("---");
        s.append(Diff.NL);
        revised.toString(s, "> ", "\n");
    }

    public void toRCSString(StringBuffer s, String EOL)
    {
        s.append("d");
        s.append(original.rcsfrom());
        s.append(" ");
        s.append(original.size());
        s.append(EOL);
        s.append("a");
        s.append(original.rcsto());
        s.append(" ");
        s.append(revised.size());
        s.append(EOL);
        revised.toString(s, "", EOL);
    }

    public void accept(RevisionVisitor visitor)
    {
        visitor.visit(this);
    }
}
