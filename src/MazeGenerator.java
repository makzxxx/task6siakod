import java.util.*;

public class MazeGenerator {
    static class Edge implements Comparable<Edge> {
        int x1, y1, x2, y2, weight;

        public Edge(int x1, int y1, int x2, int y2, int weight) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return false;

            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }
    }

    public static void main(String[] args) {
        int rows = 10; // Grid rows
        int cols = 10; // Grid columns
        int[][] maze = new int[rows * 2 + 1][cols * 2 + 1];

        List<Edge> edges = new ArrayList<>();
        Random random = new Random();

        // Generate edges for the grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r < rows - 1) {
                    edges.add(new Edge(r, c, r + 1, c, random.nextInt(100)));
                }
                if (c < cols - 1) {
                    edges.add(new Edge(r, c, r, c + 1, random.nextInt(100)));
                }
            }
        }

        // Sort edges by weight
        Collections.sort(edges);

        // Apply Kruskal's algorithm
        UnionFind uf = new UnionFind(rows * cols);
        for (Edge edge : edges) {
            int cell1 = edge.x1 * cols + edge.y1;
            int cell2 = edge.x2 * cols + edge.y2;

            if (uf.union(cell1, cell2)) {
                int wallRow = edge.x1 + edge.x2 + 1;
                int wallCol = edge.y1 + edge.y2 + 1;

                maze[edge.x1 * 2 + 1][edge.y1 * 2 + 1] = 1; // Mark start cell
                maze[edge.x2 * 2 + 1][edge.y2 * 2 + 1] = 1; // Mark end cell
                maze[wallRow][wallCol] = 1;                 // Mark path
            }
        }

        // Print the maze
        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[0].length; c++) {
                System.out.print(maze[r][c] == 1 ? " " : "#");
            }
            System.out.println();
        }
    }
}
