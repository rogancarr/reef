/*
 * Copyright 2013 Microsoft.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.tang.util.walk;

import com.microsoft.tang.Configuration;
import com.microsoft.tang.types.Node;
import com.microsoft.tang.types.ClassNode;
import com.microsoft.tang.types.PackageNode;
import com.microsoft.tang.types.NamedParameterNode;

/**
 * Build a Graphviz representation of the configuration graph.
 * @author sergiym
 */
public final class GraphVisitorGraphviz extends AbstractTypedNodeVisitor implements EdgeVisitor {

  /** Accumulate string representation of the graph here. */
  private final transient StringBuilder mGraphStr = new StringBuilder("digraph G {\n");

  /**
   * @return TANG configuration represented as a Graphviz DOT string.
   */
  @Override
  public String toString() {
    return this.mGraphStr.toString() + "}\n";
  }

  /**
   * Process current class configuration node.
   * @param aNode Current configuration node.
   * @return true to proceed with the next node, false to cancel.
   */
  @Override
  public boolean visit(final ClassNode aNode) {
    this.mGraphStr.append("  \"node_").append(aNode.getName())
            .append("\" [label=\"Class: ").append(aNode.getName()).append("\"];\n");
    return true;
  }

  /**
   * Process current package configuration node.
   * @param aNode Current configuration node.
   * @return true to proceed with the next node, false to cancel.
   */
  @Override
  public boolean visit(final PackageNode aNode) {
    this.mGraphStr.append("  \"node_").append(aNode.getName())
            .append("\" [label=\"Package: ").append(aNode.getName()).append("\"];\n");
    return true;
  }

  /**
   * Process current configuration node for the named parameter.
   * @param aNode Current configuration node.
   * @return true to proceed with the next node, false to cancel.
   */
  @Override
  public boolean visit(final NamedParameterNode aNode) {
    this.mGraphStr
            .append("  \"node_")
            .append(aNode.getName())
            .append("\" [label=\"")
            .append(aNode.getSimpleArgName())           // parameter type, e.g. "Integer"
            .append(' ')
            .append(aNode.getName())                    // short name, e.g. "NumberOfThreads"
            .append(" = ")
            .append(aNode.getDefaultInstanceAsString()) // default value, e.g. "4"
            .append("\"];\n");
    return true;
  }

  /**
   * Process current edge of the configuration graph.
   * @param aNodeFrom Current configuration node.
   * @param aNodeTo Destination configuration node.
   * @return true to proceed with the next node, false to cancel.
   */
  @Override
  public boolean visit(final Node aNodeFrom, final Node aNodeTo) {
    this.mGraphStr.append("  \"node_").append(aNodeFrom.getName())
                  .append("\" -> \"node_").append(aNodeTo.getName()).append("\";\n");
    return true;
  }

  /**
   * Produce a Graphviz DOT string for a given TANG configuration.
   * @param config TANG configuration object.
   * @return configuration graph represented as a string in Graphviz DOT format.
   */
  public static String getGraphvizStr(final Configuration config) {
    final GraphVisitorGraphviz visitor = new GraphVisitorGraphviz();
    Walk.preorder(visitor, visitor, config);
    return visitor.toString();
  }
}
