package robot;

import map.Map;
import robot.RobotConstants.DIRECTION;

/**
 * Represents a sensor mounted on the robot.
 *
 *latest
 */

public class Sensor {
    private final int lowerRange;
    private final int upperRange;
    private int sensorPosRow;
    private int sensorPosCol;
    private DIRECTION sensorDir;
    private final String id;
    private int sensorAccuracy;

    public Sensor(int lowerRange, int upperRange, int row, int col, DIRECTION dir, String id) {
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.sensorPosRow = row;
        this.sensorPosCol = col;
        this.sensorDir = dir;
        this.id = id;
    }

    public void setSensor(int row, int col, DIRECTION dir) {
        this.sensorPosRow = row;
        this.sensorPosCol = col;
        this.sensorDir = dir;
    }

    /**
     * Returns the number of cells to the nearest detected obstacle or -1 if no obstacle is detected.
     */
    public int sense(Map exploredMap, Map realMap) {
        switch (sensorDir) {
            case NORTH:
                return getSensorVal(exploredMap, realMap, 1, 0);
            case EAST:
                return getSensorVal(exploredMap, realMap, 0, 1);
            case SOUTH:
                return getSensorVal(exploredMap, realMap, -1, 0);
            case WEST:
                return getSensorVal(exploredMap, realMap, 0, -1);
        }
        return -1;
    }

    /**
     * Sets the appropriate obstacle cell in the map and returns the row or column value of the obstacle cell. Returns
     * -1 if no obstacle is detected.
     */
    private int getSensorVal(Map exploredMap, Map realMap, int rowInc, int colInc) {
    
    	
        // Check if starting point is valid for sensors with lowerRange > 1.
        if (lowerRange > 1) {
            for (int i = 1; i < this.lowerRange; i++) {
                int row = this.sensorPosRow + (rowInc * i);
                int col = this.sensorPosCol + (colInc * i);

                if (!exploredMap.checkValidCoordinates(row, col)) return i;
                if (realMap.getCell(row, col).getIsObstacle()) return i;
            }
        }

        // Check if anything is detected by the sensor and return that value.
        for (int i = this.lowerRange; i <= this.upperRange; i++) {
            int row = this.sensorPosRow + (rowInc * i);
            int col = this.sensorPosCol + (colInc * i);

            if (!exploredMap.checkValidCoordinates(row, col)) return i;

            exploredMap.getCell(row, col).setIsExplored(true);

            if (realMap.getCell(row, col).getIsObstacle()) {
                exploredMap.setObstacleCell(row, col, true);
                return i;
            }
        }

        // Else, return -1.
        return -1;
    }

    /**
     * Uses the sensor direction and given value from the actual sensor to update the map.
     */
    public void senseReal(Map exploredMap, int sensorVal) {
        switch (sensorDir) {
            case NORTH:
                processSensorVal(exploredMap, sensorVal, 1, 0);
                break;
            case EAST:
                processSensorVal(exploredMap, sensorVal, 0, 1);
                break;
            case SOUTH:
                processSensorVal(exploredMap, sensorVal, -1, 0);
                break;
            case WEST:
                processSensorVal(exploredMap, sensorVal, 0, -1);
                break;
        }
    }

    /**
     * Sets the correct cells to explored and/or obstacle according to the actual sensor value.
     */
    private void processSensorVal(Map exploredMap, int sensorVal, int rowInc, int colInc) {
    	
    	int row, col;
    	int curBlockPos;
    	// Check Whether the sensorVal is within range
    	
    	
    	// Check if -1 and LR 
    	if (sensorVal == -1 && id.equals("LRL"))
    		return;
    	
    	else if (sensorVal == -1) {
    		row = this.sensorPosRow + rowInc;
    		col = this.sensorPosCol + rowInc;
			exploredMap.getCell(row,col).setConfidence(Math.pow(10, 4));
			exploredMap.setObstacleCell(row,col,true);
        	System.out.println("Row : " + row);
            System.out.println("Col: " + col);
            System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
            System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
            return;
    	}

    	for (curBlockPos = lowerRange; curBlockPos <= upperRange; curBlockPos ++) {
    		// Check whether the sensorVal is at curBlockPos, otherwise assume no block 
    		row = this.sensorPosRow + rowInc * curBlockPos;
    		col = this.sensorPosCol + colInc * curBlockPos;
    		
    		if (!exploredMap.checkValidCoordinates(row, col)) return;
            
    		exploredMap.getCell(row, col).setIsExplored(true);
			exploredMap.getCell(row, col).setAccurate(true);
			
			System.out.println("ROW: " + row);
			System.out.println("COL: " + col);
			System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
			
			int d;
			if (id == "LRL") {
        		if (curBlockPos == 7) {
            		d = 1;
            	}
            	else if (curBlockPos == 6) {
            		d = 1;
            	}
            	else if (curBlockPos == 5) {
            		d = 1;
            	}
            	else if (curBlockPos == 4) {
            		d = 2;
            	}
            	else {
            		d = 3;
            	}
        	} 
			else {
				d = 5 - curBlockPos;
			}
			
			if (curBlockPos == sensorVal) {
				exploredMap.getCell(row,col).setConfidence(Math.pow(10, d));
				exploredMap.setObstacleCell(row,col,true);
            	System.out.println("Row : " + row);
                System.out.println("Col: " + col);
                System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
                System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
				return;
			}
			else {
				exploredMap.getCell(row, col).setConfidence(-Math.pow(10, d));
				exploredMap.setObstacleCell(row,col,true);
            	System.out.println("Row : " + row);
                System.out.println("Col: " + col);
                System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
                System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
			}
    		
    		
    	}

//	    		
//    		
//        // If above fails, check if starting point is valid for sensors with lowerRange > 1.
//        for (int i = 1; i < this.lowerRange; i++) {
//            int row = this.sensorPosRow + (rowInc * i);
//            int col = this.sensorPosCol + (colInc * i);
//
//            if (!exploredMap.checkValidCoordinates(row, col)) return;
//            if (exploredMap.getCell(row, col).getIsObstacle()) {
//            	double d = 0;
//            	
//            	if (i == 4) {
//            		d = 1;
//            	}
//            	else if(i == 3) {
//            		d = 2;
//            	}
//            	else if (i == 2) {
//            		d = 3;
//            	}
//            	else if (i == 1) {
//            		d = 4;
//            	}
//            	else if (i == -1) {
//            		d = 4;
//            	}
//            	if(sensorVal == i) {
//            		exploredMap.getCell(row, col).setConfidence(Math.pow(10, d));
//                	exploredMap.getCell(row,col).setIsObstacle(true);
//                	System.out.println("Row : " + row);
//                    System.out.println("Col: " + col);
//                    System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
//                    System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
//            	} 
//            	else {
//            		exploredMap.getCell(row, col).setConfidence(-Math.pow(10, d));
//                	exploredMap.getCell(row,col).setIsObstacle(true);
//                	System.out.println("Row : " + row);
//                    System.out.println("Col: " + col);
//                    System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
//                    System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
//            	}
//            	
//            	return;
//            }
//        }
//
//        // Update map according to sensor's value.
//        for (int i = this.lowerRange; i <= this.upperRange; i++) {
//            int row = this.sensorPosRow + (rowInc * i);
//            int col = this.sensorPosCol + (colInc * i);
//      if (!exploredMap.checkValidCoordinates(row, col)) continue;
//
////            if (exploredMap.getCell(row, col).getIsExplored() && exploredMap.getCell(row, col).isAccurate()) {
////                // if it is already explored and accurate
////                // break for the long range left since it is not accurate
////                if (id.equals("LRL")) {
////                    break;
////                }
////            }
//
//            exploredMap.getCell(row, col).setIsExplored(true);
//            // set the accuracy for LRL to false and others to true
//            // we trust our FRONT and RIGHT sensors
//            exploredMap.getCell(row, col).setAccurate(true);
//            System.out.println(id.toString() + ":" + sensorVal);
//            if (sensorVal == i) {
//            	double d = 0;
//            	if(id.equals("LRL")) {
//            		if (i == 7) {
//                		d = 1;
//                	}
//                	else if (i == 6) {
//                		d = 1;
//                	}
//                	else if (i == 5) {
//                		d = 1;
//                	}
//                	else if (i == 4) {
//                		d = 2;
//                	}
//                	else if(i == 3) {
//                		d = 3;
//                	}
//            	}
//            	else {
//	            	if(i == 3) {
//	            		d = 1;
//	            	}
//	            	else if (i == 2) {
//	            		d = 2;
//	            	}
//	            	else if (i == 1) {
//	            		d = 3;
//	            	}
//	            	else if (i == -1) {
//	            		d = 3;
//	            	}
//            	}
//                exploredMap.getCell(row, col).setConfidence(Math.pow(10, d));
//                exploredMap.setObstacleCell(row, col, true);
//                System.out.println("Row : " + row);
//                System.out.println("Col: " + col);
//                System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
//                System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
//                break;
//            }
//            else {
//            	double d = 0;
//            	if(id.equals("LRL")) {
//            		if (i == 7) {
//                		d = 1;
//                	}
//                	else if (i == 6) {
//                		d = 1;
//                	}
//                	else if (i == 5) {
//                		d = 2;
//                	}
//                	else if (i == 4) {
//                		d = 3;
//                	}
//                	else if(i == 3) {
//                		d = 4;
//                	}	
//            	}
//            	else {
//            		if(i == 3) {
//	            		d = 1;
//	            	}
//	            	else if (i == 2) {
//	            		d = 2;
//	            	}
//	            	else if (i == 1) {
//	            		d = 3;
//	            	}
//	            	else if (i == -1) {
//	            		d = 3;
//	            	}
//            	}
//            	
//            	exploredMap.getCell(row,  col).setConfidence(-Math.pow(10, d));
//            	exploredMap.setObstacleCell(row, col, true);
//            	System.out.println("Row : " + row);
//                System.out.println("Col: " + col);
//                System.out.println("Confidence: " + (exploredMap.getCell(row,col).getConfidence()));
//                System.out.println("Obstacle: " + exploredMap.getCell(row,col).getIsObstacle());
//            }
//
//            // Override previous obstacle value if front sensors detect no obstacle.
//            if (exploredMap.getCell(row, col).getIsObstacle()) {
//                if (id.equals("h")) {
//                    exploredMap.setObstacleCell(row, col, false);
//                } else {
//                    break;
//                }
//            }
      
    }
}
