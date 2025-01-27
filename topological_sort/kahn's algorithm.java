//this was done for the course schedule IV in leetcode

class Solution {
    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
        // Create an adjacency list and an array to track in-degrees
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        int[] inDegree = new int[numCourses];
        
        // Build the graph and compute in-degrees
        for (int[] edge : prerequisites) {
            int course = edge[0];
            int prereq = edge[1];
            
            adjList.putIfAbsent(course, new ArrayList<>());
            adjList.get(course).add(prereq);
            inDegree[prereq]++;
        }
        
        // Kahn's Algorithm: Topological Sort (BFS)
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Set<Integer>> nodePreReqs = new HashMap<>();
        
        // Add courses with no prerequisites to the queue
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // Process nodes with BFS
        while (!queue.isEmpty()) {
            int node = queue.poll();
            
            // Process each course that depends on the current course
            if (adjList.containsKey(node)) {
                for (int adj : adjList.get(node)) {
                    // Add node as a prerequisite for adjacent node
                    nodePreReqs.putIfAbsent(adj, new HashSet<>());
                    nodePreReqs.get(adj).add(node);
                    
                    // Propagate all the prerequisites of node to adj
                    nodePreReqs.putIfAbsent(node, new HashSet<>());
                    for (int pre : nodePreReqs.get(node)) {
                        nodePreReqs.get(adj).add(pre);
                    }
                    
                    // Decrease the in-degree of adj and add to the queue if in-degree becomes 0
                    inDegree[adj]--;
                    if (inDegree[adj] == 0) {
                        queue.offer(adj);
                    }
                }
            }
        }
        
        // Prepare the result for each query
        List<Boolean> result = new ArrayList<>();
        for (int[] query : queries) {
            int prereq = query[0];
            int course = query[1];
            result.add(nodePreReqs.getOrDefault(course, new HashSet<>()).contains(prereq));
        }
        
        return result;
    }
}
