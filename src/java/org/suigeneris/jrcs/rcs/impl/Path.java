/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: Path.java,v 1.2 2006/06/08 18:17:11 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.suigeneris.jrcs.diff.PatchFailedException;
import org.suigeneris.jrcs.rcs.Archive;
import org.suigeneris.jrcs.rcs.InvalidFileFormatException;

/**
 * A path from the head revision to a given revision in an Archive. Path
 * collaborates with Node in applying the set of deltas contained in archive
 * nodes to arrive at the text of the revision corresponding to the last node in
 * the path. This class is NOT thread safe.
 * 
 * @see Archive
 * @see Node
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: Path.java,v 1.2 2006/06/08 18:17:11 juanca Exp $
 */
public class Path
{
    private List path = new LinkedList();

    /**
     * Creates an empty Path
     */
    public Path()
    {
    }

    /**
     * Add a node to the Path.
     * 
     * @param node
     *            The Node to add.
     */
    public void add(Node node)
    {
        path.add(node);
    }

    /**
     * The size of the Path.
     * 
     * @return The size of the Path
     */
    public int size()
    {
        return path.size();
    }

    /**
     * Return the last node in the path or null if the path is empty.
     * 
     * @return the last node in the path or null if the path is empty.
     */
    public Node last()
    {
        if (size() == 0)
        {
            return null;
        }
        else
        {
            return (Node) path.get(size() - 1);
        }
    }

    /**
     * Return the node before the last in the path.
     * 
     * @return the node before the last in the path or null unavailable.
     */
    public Node beforeLast()
    {
        if (size() == 0)
        {
            return null;
        }
        else
        {
            return (Node) path.get(size() - 1);
        }
    }

    /**
     * Returns the text that corresponds to applying the patches in the list of
     * nodes in the Path. Assume that the text of the first node is plaintext
     * and not deltatext.
     * 
     * @return The resulting text after the patches
     */
    public List patch() throws InvalidFileFormatException,
            PatchFailedException, NodeNotFoundException
    {
        return patch(false);
    }

    /**
     * Returns the text that corresponds to applying the patches in the list of
     * nodes in the Path. Assume that the text of the first node is plaintext
     * and not deltatext.
     * 
     * @param annotate
     *            if true, then each text line is a {@link Line Line} with the
     *            original text annotated with the revision in which it was last
     *            changed or added.
     * @return The resulting text after the patches
     */
    public List patch(boolean annotate) throws InvalidFileFormatException,
            PatchFailedException, NodeNotFoundException
    {
        return patch(new Lines(), annotate);
    }

    /**
     * Returns the text that corresponds to applying the patches in the list of
     * nodes in the Path. Assume that the text of the first node is plaintext
     * and not deltatext.
     * 
     * @param lines
     *            The list to where the text must be added and the patches
     *            applied. {@link Line Line} with the original text annotated
     *            with the revision in which it was last changed or added.
     * @return The resulting text after the patches
     */
    public List patch(List lines) throws InvalidFileFormatException,
            PatchFailedException, NodeNotFoundException
    {
        return patch(lines, false);
    }

    /**
     * Returns the text that corresponds to applying the patches in the list of
     * nodes in the Path. Assume that the text of the first node is plaintext
     * and not deltatext.
     * 
     * @param lines
     *            The list to where the text must be added and the patches
     *            applied.
     * @param annotate
     *            if true, then each text line is a {@link Line Line} with the
     *            original text annotated with the revision in which it was last
     *            changed or added.
     * @return The resulting text after the patches
     */
    public List patch(List lines, boolean annotate)
            throws InvalidFileFormatException, PatchFailedException,
            NodeNotFoundException
    {
        Iterator p = path.iterator();

        // get full text of first node
        TrunkNode head = (TrunkNode) p.next();
        head.patch0(lines, annotate);

        // the rest are patches
        while (p.hasNext())
        {
            Node n = (Node) p.next();
            n.patch(lines, annotate);
        }
        return lines;
    }
}
