package academy.maze.generators.kruskalGenerator;

import academy.maze.dto.Point;

/** Ребро между двумя проходами. */
public record Edge(Point from, Point to) {}
