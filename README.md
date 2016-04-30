# mosaic

* Read in the original File
* Find out the size of the tiles by dividing the orignial file by the selected number of horizontal tiles

              +--------------------------+              +------+------+------+------+
              | Original File            |              | o    | o    | o    | o    |
              |                          |              |      |      |      |      |
              |                          |              |      |      |      |      |
              |                          |  +-------->  +---------------------------+
              |                          |              | o    | o    | o    | o    |
              |                          |              |      |      |      |      |
              |                          |              |      |      |      |      |
              |                          |              +------+------+------+------+
              +--------------------------+



* Generate the tiles by miniturazing a bunch of images or parts of them to the size of the tiles
* Calculate the color of each tile's quadrants (t1c-t4c) and use this numbers to generate the tile files name (plus a counter)


               +------------+
             +-----------+  |               +-----------+
             |           |  |               |           |
           +-+--------+  |  |               |           |
           |          |  |  |               |           |                 +-------+   +-------+  +-------+  +-------+
           |          |  |  |   +------->   |           |   +-------->    | t     |   | t     |  | t     |  | t     |
           |          |  |  |               |           |                 |       |   |       |  |       |  |       |
           |          |  |  |               |           |                 |       |   |       |  |       |  |       |
           |          |  +--+               |           |                 +-------+   +-------+  +-------+  +-------+
           |          |  |                  +-----------+
           |          +--+
           +----------+



                      +---------------+
                      |t1c    | t2c   |
                      |       |       |
                      |       |       |
                      +---------------+         rgb(t1c)_rgb(t2c)_rgb(t3c)_rgb(t4c)_rgb(t5c)_i.jpg
                      |t3c    | t4c   |
                      |       |       |
                      |       |       |
                      +-------+-------+




 * Calculate the color of each original tile (t5c) and of its quadrants (t1c-t4c)
 * Find the closest tiles by their t1c-t4c MSD (increase allowed MSD until threshold is reached)

                       o5c
                      +-------+-------+
                      |o1c    | o2c   |
                      |       |       |
                      |       |       |                (rgb(o1c)-rgb(t1c))^2+(rgb(o2c)-rgb(t2c))^2+(rgb(o3c)-rgb(t3c))^2+(rgb(o4c)-rgb(t4c))^2 < x
                      +---------------+
                      |o3c    | o4c   |
                      |       |       |
                      |       |       |
                      +-------+-------+

 * Pixel by pixel MSD

                      +-------+-------+   +-------+-------+                                   +------+------+------+------+
                      |o1c    | o2c   |   |t1c    | t2c   |                                   | t    | t    | t    | t    |
                      |       |       |   |       |       |                                   |      |      |      |      |
                      |       |       |   |       |       |                                   |      |      |      |      |
                      +---------------+   +---------------+    +------->   MSD      +-------> +---------------------------+
                      |o3c    | o4c   |   |t3c    | t4c   |                                   | t    | t    | t    | t    |
                      |       |       |   |       |       |                                   |      |      |      |      |
                      |       |       |   |       |       |                                   |      |      |      |      |
                      +-------+-------+   +-------+-------+                                   +------+------+------+------+
* find best 5 matches
* stitch together and correct color to match average of original tile
* do not allow the same picture to be used by adjacent tiles.