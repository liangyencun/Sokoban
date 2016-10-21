package com.xc.sokoban;

/**
 * Created by Aaron on 2016-04-26.
 */
public class Maze {
    public int mazeSizeX = 10; //constants for now
    public int mazeSizeY = 15;
    public Tile[][][] mazeTiles;    //3rd dimension is a hidden layer that holds targets when boxes are on top
    public String MAZE;

    public Maze(int level){
        switch (level) { //level creation block; # = wall, T = target, B = box, P = player start
            case 1:
                MAZE =
                        "########  "
                      + "#      ###"
                      + "###      #"
                      + "#  T     #"
                      + "#    T ###"
                      + "##  B    #"
                      + "#  T  P  #"
                      + "#        #"
                      + "##   ##  #"
                      + "#    B   #"
                      + "##  ##   #"
                      + "## B   B #"
                      + "####     #"
                      + "#      T #"
                      + "##########";
                break;
            case 2:
                MAZE =
                        "##########"
                      + "#P     # #"
                      + "#### B  T#"
                      + "#  T     #"
                      + "#    T ###"
                      + "##  B   ##"
                      + "#  T     #"
                      + "#        #"
                      + "##  ###B #"
                      + "#T   B   #"
                      + "##  ###  #"
                      + "## B   B #"
                      + "### B    #"
                      + "#T     T #"
                      + "##########";
                break;
            case 3:
                MAZE =
                        "##########"
                      + "#     T  #"
                      + "#  B  B  #"
                      + "####  B ##"
                      + "##T    ###"
                      + "#  P    ##"
                      + "#      B #"
                      + "## T     #"
                      + "#     B  #"
                      + "#  T #####"
                      + "#  #######"
                      + "#      B #"
                      + "#   ###  #"
                      + "#   T   T#"
                      + "##########";
                break;
            case 4:
                MAZE =
                          "##########"
                        + "#  B     #"
                        + "#      ###"
                        + "####    ##"
                        + "##T   B  #"
                        + "#  P     #"
                        + "#  B   B #"
                        + "## T   ###"
                        + "#     ####"
                        + "#    #####"
                        + "#  #######"
                        + "#  T   B #"
                        + "#   T##  #"
                        + "### T    #"
                        + "##########";
                break;
        }
        mazeTiles = new Tile[mazeSizeX][mazeSizeY][2];
    }

    public void addMazeTile(Tile tile, int cellX, int cellY){
        mazeTiles[cellX][cellY][0] = tile;
    }

    public void deleteMazeTile(int cellX, int cellY){
        mazeTiles[cellX][cellY][0] = null;
    }

    public void moveTileDown(Tile tile){
        mazeTiles[tile.getXTile()][tile.getYTile()][0] = null;
        mazeTiles[tile.getXTile()][tile.getYTile()][1] = tile;
    }

    public void moveTileUp(Tile tile){
        mazeTiles[tile.getXTile()][tile.getYTile()][0] = tile;
        mazeTiles[tile.getXTile()][tile.getYTile()][1] = null;
    }


}
