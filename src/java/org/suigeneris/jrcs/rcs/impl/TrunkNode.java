/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: TrunkNode.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.impl;

import java.util.List;

import org.suigeneris.jrcs.rcs.Archive;
import org.suigeneris.jrcs.rcs.BranchNotFoundException;
import org.suigeneris.jrcs.rcs.InvalidFileFormatException;
import org.suigeneris.jrcs.rcs.InvalidTrunkVersionNumberException;
import org.suigeneris.jrcs.rcs.Version;

/**
 * Represents a node on the trunk or main branch of a version control Archive.
 * 
 * <p>
 * A {@link TrunkNode TrunkNode} stores the deltas between the node's revision
 * and the previous revision; that is, when the deltas are applied to the
 * current revision, the text of the previous revision is obtained. The
 * {@link Node#rcsnext rcsnext} field of a TrunkNode points to the node
 * corresponding to the previous revision.
 * </p>
 * This class is NOT thread safe.
 * 
 * @see Node
 * @see Archive
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: TrunkNode.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 */
public class TrunkNode extends Node
{

    /**
     * Create a TrunkNode bu copying another TrunkNode.
     */
    TrunkNode(TrunkNode other)
    {
        super(other);
    }

    /**
     * Create a TrunkNode. The next field in a TrunkNode points to the immediate
     * previos revision or parent.
     */
    public TrunkNode(Version vernum, TrunkNode next)
            throws InvalidTrunkVersionNumberException
    {
        super(vernum, next);
        if (vernum.size() > 2)
        {
            throw new InvalidTrunkVersionNumberException(vernum);
        }
    }

    /**
     * Set the next node in the RCS logical hierarcy. Update the _parent and
     * _child node accordingly. For a TrunkNode, the RCS-next is the immediate
     * parent.
     */
    public void setRCSNext(Node node)
    {
        super.setRCSNext(node);
        if (this.getParent() != null)
        {
            this.getParent().child = null;
        }
        this.parent = node;
        if (this.getParent() != null)
        {
            this.getParent().child = this;
        }
    }

    public Node deltaRevision()
    {
        return (getChild() != null ? getChild() : this);
    }

    public Node nextInPathTo(Version vernum, boolean soft)
            throws NodeNotFoundException
    {
        Version branchPoint = vernum.getBase(2);
        if (this.version.isLessThan(branchPoint))
        {
            if (soft)
            {
                return null;
            }
            else
            {
                throw new NodeNotFoundException(vernum);
            }
        }

        Version thisBase = this.version.getBase(branchPoint.size());
        if (thisBase.isGreaterThan(branchPoint))
        {
            return getParent();
        }
        else if (vernum.size() > this.version.size())
        {
            Node branch = getBranch(vernum.at(this.version.size()));
            if (branch != null || soft)
            {
                return branch;
            }
            else
            {
                throw new BranchNotFoundException(vernum.getBase(this.version
                        .size() + 1));
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Provide the initial patch. Used only for head nodes.
     * 
     * @param original
     *            Where to add the patch to.
     * @param annotate
     *            True if the lines should be annotated with version numbers.
     */
    protected void patch0(List original, boolean annotate)
            throws InvalidFileFormatException, NodeNotFoundException,
            org.suigeneris.jrcs.diff.PatchFailedException
    {
        Object[] lines = getText();
        Node root = this.root();
        for (int it = 0; it < lines.length; it++)
        {
            original.add(new Line(root, lines[it]));
        }
        if (annotate && getParent() != null)
        {
            getParent().pathTo(root.version).patch(original, true);
        }
    }
}
