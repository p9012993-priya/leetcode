import java.util.*;

class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        if (n <= 1) return 0;

        // Check if it's a rotation of increasing order [0, 1, ..., n-1]
        int minOps = Integer.MAX_VALUE;
        
        // Let's check increasing cyclic check
        boolean isInc = true;
        int zeroIdx = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) zeroIdx = i;
            if (nums[i] != (nums[(i - 1 + n) % n] + 1) % n) {
                isInc = false;
            }
        }

        // Let's check decreasing cyclic check
        boolean isDec = true;
        for (int i = 0; i < n; i++) {
            if (nums[i] != (nums[(i - 1 + n) % n] - 1 + n) % n) {
                isDec = false;
            }
        }

        if (!isInc && !isDec) {
            return -1;
        }

        if (isInc) {
            // Option 1: Just rotate left until 0 is at index 0 -> zeroIdx operations
            // Option 2: Reverse (1 op), then rotate left, then reverse (1 op)
            minMath: {
                int cost1 = zeroIdx;
                int cost2 = 1 + (n - zeroIdx) % n + 1; // Reverse, rotate right/left, reverse
                minOps = Math.min(cost1, cost2);
            }
        } else {
            // If it's decreasing, we need to reverse first or use BFS/state transitions
            // Let's simulate the shortest path using a queue for the (type, shift) state space (2 * n states)
        }

        // General BFS for exact state transitions (Type 0 or Type 1 with shift)
        return solveBfs(nums, n);
    }

    private int solveBfs(int[] nums, int n) {
        // State: encoded as type * n + shift
        // type 0: rotation of original, type 1: rotation of reversed original
        int targetType = -1, targetShift = -1;
        
        // Find if sorted increasing is reachable
        // Increasing order: type 0, shift 0 has nums[0] == 0 and strictly increasing
        // Let's use BFS over the 2*n states.
        
        Queue<int[]> queue = new LinkedList<>();
        // Distance array: dist[type][shift]
        int[][] dist = new int[2][n];
        for (int i = 0; i < 2; i++) Arrays.fill(dist[i], -1);

        // Find initial state of nums
        // Check which shift and type matches nums
        int initType = -1, initShift = -1;
        
        int[] original = new int[n];
        for (int i = 0; i < n; i++) original[i] = i;
        
        int[] reversed = new int[n];
        for (int i = 0; i < n; i++) reversed[i] = n - 1 - i;

        for (int shift = 0; shift < n; shift++) {
            if (matches(nums, original, shift)) {
                initType = 0;
                initShift = shift;
                break;
            }
            if (matches(nums, reversed, shift)) {
                initType = 1;
                initShift = shift;
                break;
            }
        }

        if (initType == -1) return -1;

        queue.add(new int[]{initType, initShift});
        dist[initType][initShift] = 0;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int t = curr[0];
            int s = curr[1];

            // Target condition: type 0 and shift 0 means sorted increasing order [0, 1, ..., n-1]
            if (t == 0 && s == 0) {
                return dist[t][s];
            }

            // Operation 1: Rotate Left by One
            int nextT1 = t;
            int nextS1 = (s + 1) % n;
            if (dist[nextT1][nextS1] == -1) {
                dist[nextT1][nextS1] = dist[t][s] + 1;
                queue.add(new int[]{nextT1, nextS1});
            }

            // Operation 2: Reverse the entire array
            // A reverse toggles type, and changes shift to (n - shift) % n
            int nextT2 = 1 - t;
            int nextS2 = (n - s) % n;
            if (dist[nextT2][nextS2] == -1) {
                dist[nextT2][nextS2] = dist[t][s] + 1;
                queue.add(new int[]{nextT2, nextS2});
            }
        }

        return -1;
    }

    private boolean matches(int[] nums, int[] base, int shift) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] != base[(i + shift) % n]) {
                return false;
            }
        }
        return true;
    }
}