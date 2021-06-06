package scheduler;

import java.util.ArrayList;
import java.util.Collections;

public class SchedulingService {

    private static SchedulingService instance = null;

    public static SchedulingService getInstance() {
        if (instance == null)
            instance = new SchedulingService();
        return instance;
    }

    public int[][] FCFSScheduling(int firstPosition, ArrayList<Integer> diskQueue) {
        int[][] result = new int[diskQueue.size() + 1][5];
        int averageTime;
        result[0][0] = 0;
        result[0][1] = firstPosition;
        result[0][2] = 0;
        result[0][3] = 0;
        for (int i = 0; i < diskQueue.size(); i++) {
            result[i + 1][0] = i + 1;
            result[i + 1][1] = diskQueue.get(i);
            result[i + 1][2] = Math.abs(result[i + 1][1] - result[i][1]);
            result[i + 1][3] = result[i][3] + result[i + 1][2];
        }
        averageTime = result[diskQueue.size()][3] / diskQueue.size();
        for (int i = 0; i < diskQueue.size() + 1; i++) {
            result[i][4] = result[i][2] - averageTime;
        }
        return result;
    }

    public int[][] SSTFScheduling(int firstPosition, ArrayList<Integer> diskQueue) {
        ArrayList<Integer> diskQueueCopy = new ArrayList<>(diskQueue);
        int[][] result = new int[diskQueue.size() + 1][5];
        int averageTime;
        int j = 1;
        int currentPosition = firstPosition;
        int minIndex, minDistance;
        result[0][0] = 0;
        result[0][1] = firstPosition;
        result[0][2] = 0;
        result[0][3] = 0;
        while (!diskQueueCopy.isEmpty()) {
            minIndex = -1;
            minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < diskQueueCopy.size(); i++) {
                if (Math.abs(currentPosition - diskQueueCopy.get(i)) < minDistance) {
                    minIndex = i;
                    minDistance = Math.abs(currentPosition - diskQueueCopy.get(i));
                }
            }
            currentPosition = diskQueueCopy.get(minIndex);
            result[j][0] = j;
            result[j][1] = currentPosition;
            result[j][2] = minDistance;
            result[j][3] = result[j - 1][3] + result[j][2];
            j++;
            diskQueueCopy.remove(minIndex);
        }
        averageTime = result[diskQueue.size()][3] / diskQueue.size();
        for (int i = 0; i < diskQueue.size() + 1; i++) {
            result[i][4] = result[i][2] - averageTime;
        }

        return result;
    }

    public int[][] SCANScheduling(boolean startDirection, int firstPosition, ArrayList<Integer> diskQueue) {
        ArrayList<Integer> diskQueueCopy = new ArrayList<>(diskQueue);
        int[][] result = new int[diskQueue.size() + 2][5];
        int averageTime;
        int j = 1;
        int currentPosition = firstPosition;
        int minIndex, minDistance;
        boolean changeDirection = false;
        result[0][0] = 0;
        result[0][1] = firstPosition;
        result[0][2] = 0;
        result[0][3] = 0;
        while (!diskQueueCopy.isEmpty()) {
            minIndex = -1;
            minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < diskQueueCopy.size(); i++) {
                if (startDirection) {
                    if (Math.abs(currentPosition - diskQueueCopy.get(i)) < minDistance && diskQueueCopy.get(i) <= firstPosition) {
                        minIndex = i;
                        minDistance = Math.abs(currentPosition - diskQueueCopy.get(i));
                    }
                } else {
                    if (Math.abs(currentPosition - diskQueueCopy.get(i)) < minDistance && diskQueueCopy.get(i) >= firstPosition) {
                        minIndex = i;
                        minDistance = Math.abs(currentPosition - diskQueueCopy.get(i));
                    }
                }
            }
            if (minIndex == -1) {
                if (!changeDirection) {
                    if (startDirection) {
                        result[j][0] = j;
                        result[j][1] = 0;
                        result[j][2] = currentPosition;
                        result[j][3] = result[j - 1][3] + result[j][2];
                        currentPosition = 0;
                        j++;
                        changeDirection = true;
                        continue;
                    } else {
                        result[j][0] = j;
                        result[j][1] = 400;
                        result[j][2] = 400 - currentPosition;
                        result[j][3] = result[j - 1][3] + result[j][2];
                        currentPosition = 400;
                        j++;
                        changeDirection = true;
                        continue;
                    }
                }
                for (int i = 0; i < diskQueueCopy.size(); i++) {
                    if (Math.abs(currentPosition - diskQueueCopy.get(i)) < minDistance) {
                        minIndex = i;
                        minDistance = Math.abs(currentPosition - diskQueueCopy.get(i));
                    }
                }
            }
            currentPosition = diskQueueCopy.get(minIndex);
            result[j][0] = j;
            result[j][1] = currentPosition;
            result[j][2] = minDistance;
            result[j][3] = result[j - 1][3] + result[j][2];
            j++;
            diskQueueCopy.remove(minIndex);
        }
        averageTime = result[diskQueue.size() + 1][3] / diskQueue.size();
        for (int i = 0; i < diskQueue.size() + 2; i++) {
            result[i][4] = result[i][2] - averageTime;
        }

        return result;
    }

    public int[][] SLTFScheduling(int firstPosition, ArrayList<Integer> diskQueue) {
        ArrayList<Integer> diskQueueCopy = new ArrayList<>(diskQueue);
        Collections.sort(diskQueueCopy);
        int[][] result;
        int averageTime;
        int j = 1;
        int currentPosition = firstPosition;
        int minIndex, minDistance;
        if (currentPosition <= diskQueueCopy.get(0)) {
            result = new int[diskQueue.size() + 1][5];
        } else {
            result = new int[diskQueue.size() + 3][5];
        }
        result[0][0] = 0;
        result[0][1] = firstPosition;
        result[0][2] = 0;
        result[0][3] = 0;
        int t = 0;
        while (!diskQueueCopy.isEmpty()) {
            minIndex = -1;
            minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < diskQueueCopy.size(); i++) {
                if (currentPosition <= diskQueueCopy.get(i)) {
                    minIndex = i;
                    minDistance = Math.abs(currentPosition - diskQueueCopy.get(i));
                    break;
                }
            }
            if (minIndex == -1) {
                result[j][0] = j;
                result[j][1] = 400;
                result[j][2] = 400 - currentPosition;
                result[j][3] = result[j - 1][3] + result[j][2];
                j++;
                result[j][0] = j;
                result[j][1] = 0;
                result[j][2] = 0;
                result[j][3] = 0;
                j++;
                currentPosition = 0;
                continue;
            }
            currentPosition = diskQueueCopy.get(minIndex);
            result[j][0] = j;
            result[j][1] = currentPosition;
            result[j][2] = minDistance;
            result[j][3] = result[j - 1][3] + result[j][2];
            j++;
            diskQueueCopy.remove(minIndex);
        }
        averageTime = result[result.length - 1][3] / diskQueue.size();
        for (int i = 0; i < result.length; i++) {
            result[i][4] = result[i][2] - averageTime;
        }

        return result;
    }
}
